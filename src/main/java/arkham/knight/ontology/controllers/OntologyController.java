package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OntologyController {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    private final OntologyService ontologyService;

    private final WordService wordService;

    public OntologyController(OntologyService ontologyService, WordService wordService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        List<Word> wordList = ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);

        model.addAttribute("words", wordService.evaluateWordsAndReturnCleanWordList(wordList));

        return "/freemarker/ontology/summary";
    }


    @RequestMapping(value = "/individuals", method = RequestMethod.GET)
    public String showAllIndividuals(Model model, @RequestParam(defaultValue = "") String sentence, @RequestParam(defaultValue = "word-search") String searchType) {

        List<Individual> individualList = new ArrayList<>();

        if (sentence.length() !=0){

            List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);
            individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);
        }

        else{

            var individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().listIndividuals();

            while (individualsIterator.hasNext()) {

                var individual = individualsIterator.next();

                individualList.add(individual);
            }
        }

        model.addAttribute("individuals", individualList);

        return "/freemarker/ontology/individuals";
    }


    @RequestMapping(value = "/creation", method = RequestMethod.GET)
    public String creationPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/ontology/createIndividual";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam() String individualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam() String fatherClassName, @RequestParam() String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam(defaultValue = "N/A") String synonyms) {

        var wordToSave = new Word(individualName, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        ontologyService.saveIndividual(individualName, wordToSave);

        return "redirect:/individuals";
    }


    @RequestMapping(value = "/edition", method = RequestMethod.GET)
    public String getIndividualByName(Model model, @RequestParam String individualName)  {

        var individualURI = ontologyConnectionService.ontologyURI.concat(individualName);

        var individual = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().getIndividual(individualURI);

        var definitionPropertyValue = individual.getPropertyValue(ontologyConnectionService.definitionProperty);
        var examplePropertyValue = individual.getPropertyValue(ontologyConnectionService.exampleProperty);
        var lemmaRAEPropertyValue = individual.getPropertyValue(ontologyConnectionService.lemmaRAEProperty);
        var synonymsPropertyValue = individual.getPropertyValue(ontologyConnectionService.synonymsProperty);

        model.addAttribute("fatherClass", individual.getOntClass().getLocalName());
        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("lema", individual.getLocalName());
        model.addAttribute("definicion", definitionPropertyValue.toString());

        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());

        if (lemmaRAEPropertyValue != null)
            model.addAttribute("lemmaRAE", lemmaRAEPropertyValue.toString());

        if (synonymsPropertyValue != null)
            model.addAttribute("sinonimos", synonymsPropertyValue.toString());

        return "/freemarker/ontology/editIndividual";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam() String originalIndividualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam String individualName, @RequestParam String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms) {

        var wordDataToSave = new Word(individualName, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        var wordToEdit = wordService.getWordByLemma(originalIndividualName);

        var filteredWord = wordService.editionWordFilter(wordToEdit, wordDataToSave);

        ontologyService.saveIndividual(originalIndividualName, filteredWord);

        return "redirect:/individuals";
    }


    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showIndividual(Model model, @RequestParam String lemma) {

        var wordToShow = wordService.getWordByLemma(lemma);

        model.addAttribute("word", wordToShow);
        model.addAttribute("percentageAgreement", String.format("%.2f", wordService.calculateWordPercentageAgreement(wordToShow)));
        model.addAttribute("percentageAgreementOfAbsents", String.format("%.2f", wordService.calculateWordPercentageAgreementOfPresenceOrAbsents(wordToShow, false)));
        model.addAttribute("percentageAgreementOfPresences", String.format("%.2f", wordService.calculateWordPercentageAgreementOfPresenceOrAbsents(wordToShow, true)));
        model.addAttribute("meanPercentageAgreement", String.format("%.2f", wordService.calculateWordMeanPercentageAgreement(wordToShow)));

        return "/freemarker/ontology/show";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteIndividual(@RequestParam String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/individuals";
    }


    @RequestMapping(value = "/class-creation", method = RequestMethod.GET)
    public String creationClassPage() {

        return "/freemarker/ontology/createClass";
    }


    @RequestMapping(value = "/class-create", method = RequestMethod.POST)
    public String createClass(@RequestParam String fatherClassName, @RequestParam(defaultValue = "") String subClass) {

        if (subClass.length() == 0) {

            var defaultTestWord = new Word("prueba","definition","example", fatherClassName,"individualNameRae", "synonims", "0", "0");

            ontologyService.saveIndividual(defaultTestWord.getLema(), defaultTestWord);
        }

        else
            ontologyService.saveFatherClassAndSubClass(fatherClassName, subClass);

        return "redirect:/individuals";
    }
}
