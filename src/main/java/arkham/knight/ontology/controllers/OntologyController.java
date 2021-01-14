package arkham.knight.ontology.controllers;

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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "morirsoñando") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        model.addAttribute("words", ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList));

        return "/freemarker/summary";
    }


    @RequestMapping(value = "/individuals", method = RequestMethod.GET)
    public String showAllIndividuals(Model model) {

        List<Individual> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            individualList.add(individual);
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
    public String create(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName, @RequestParam("definition") String definition, @RequestParam("example") String example) {

        ontologyService.saveIndividual(individualName, individualName, fatherClassName, definition, example);

        return "redirect:/words/individuals";
    }


    @RequestMapping(value = "/edition", method = RequestMethod.GET)
    public String getIndividualByName(Model model, @RequestParam("individualName") String individualName)  {

        String individualURI = ontologyConnectionService.ontologyURI.concat(individualName);

        Individual individual = ontologyConnectionService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        Property definitionProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.definitionURI);

        Property exampleProperty = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.exampleURI);

        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);


        model.addAttribute("fatherClass", individual.getOntClass().getLocalName());
        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("lema", individual.getLocalName());
        model.addAttribute("definicion", definitionPropertyValue.toString());

        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());

        return "/freemarker/editIndividual";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam("originalIndividualName") String originalIndividualName, @RequestParam("individualName") String individualName, @RequestParam("definition") String definition, @RequestParam("example") String example, @RequestParam("fatherClassName") String fatherClassName) {

        ontologyService.saveIndividual(originalIndividualName, individualName, fatherClassName, definition, example);

        return "redirect:/words/individuals";
    }


    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showIndividual(Model model, @RequestParam("lemma") String lemma) {

        model.addAttribute("word", wordService.getWordByLemma(lemma));

        return "/freemarker/show";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteIndividual(@RequestParam("individualName") String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/words/individuals";
    }
}
