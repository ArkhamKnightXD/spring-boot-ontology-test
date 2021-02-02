package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/words")
@Controller
public class OntologyController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private OntologyConnectionService ontologyConnectionService;

    @Autowired
    private WordService wordService;

    @Autowired
    private DRAEConnectionService draeConnectionService;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        model.addAttribute("words", ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList));

        return "/freemarker/summary";
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchPageDRAE(Model model, @RequestParam(defaultValue = "diccionario") String sentence) {

        List<DRAEObject> wordList = draeConnectionService.getTheWordDataFromDRAE(restTemplate, sentence);

        model.addAttribute("words", wordList);

        return "/freemarker/searchDRAE";
    }


    @RequestMapping(value = "/individuals", method = RequestMethod.GET)
    public String showAllIndividuals(Model model, @RequestParam(defaultValue = "") String sentence, @RequestParam(defaultValue = "word-search") String searchType) {

        List<Individual> individualList = new ArrayList<>();

        if (sentence.length() !=0){

            List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);
            individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);
        }

        else{

            Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();

            while (individualsIterator.hasNext()) {

                Individual individual = individualsIterator.next();

                individualList.add(individual);
            }
        }

        model.addAttribute("individuals", individualList);

        return "/freemarker/individuals";
    }


    @RequestMapping(value = "/creation", method = RequestMethod.GET)
    public String creationPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/createIndividual";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam() String individualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam() String fatherClassName, @RequestParam() String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam(defaultValue = "N/A") String synonyms) {

        ontologyService.saveIndividual(individualName, individualName, fatherClassName, definition, example, individualNameRAE, synonyms);

        return "redirect:/words/individuals";
    }


    @RequestMapping(value = "/edition", method = RequestMethod.GET)
    public String getIndividualByName(Model model, @RequestParam() String individualName)  {

        String individualURI = ontologyConnectionService.ontologyURI.concat(individualName);

        Individual individual = ontologyConnectionService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);
        Property definitionProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.definitionURI);
        Property exampleProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.exampleURI);
        Property lemmaRAEProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.lemmaRAEURI);
        Property synonymsProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.synonymsURI);

        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);
        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);
        RDFNode lemmaRAEPropertyValue = individual.getPropertyValue(lemmaRAEProperty);
        RDFNode synonymsPropertyValue = individual.getPropertyValue(synonymsProperty);

        model.addAttribute("fatherClass", individual.getOntClass().getLocalName());
        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("lema", individual.getLocalName());
        model.addAttribute("definicion", definitionPropertyValue.toString());
        model.addAttribute("lemmaRAE", lemmaRAEPropertyValue.toString());
        model.addAttribute("sinonimos", synonymsPropertyValue.toString());

        //quitar estos if de todo el proyecto, pues las propiedades siempre tendran el valor n/a
        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());

        return "/freemarker/editIndividual";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam() String originalIndividualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam() String individualName, @RequestParam() String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam() String fatherClassName, @RequestParam(defaultValue = "N/A") String synonyms) {

        ontologyService.saveIndividual(originalIndividualName, individualName, fatherClassName, definition, example, individualNameRAE, synonyms);

        return "redirect:/words/individuals";
    }


    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showIndividual(Model model, @RequestParam() String lemma) {

        model.addAttribute("word", wordService.getWordByLemma(lemma));

        return "/freemarker/show";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteIndividual(@RequestParam() String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/words/individuals";
    }
}
