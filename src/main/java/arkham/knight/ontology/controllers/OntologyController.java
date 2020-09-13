package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.URIService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
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
    private URIService uriService;


    @RequestMapping("/")
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "morirsoñando") String individualName) {

        List<Word> wordList;

        List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(individualName));


        Property definition = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.definitionURI);

        Property example = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.exampleURI);

        Property grammarMark = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.grammarMarkURI);

        Property markCulturalLevel = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.markCulturalLevelURI);

        Property markStyleValue = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.markStyleValueURI);

        Property locution = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.locutionURI);

        Property locutionType = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.locutionTypeURI);

        wordList = ontologyService.saveAllPropertiesValueInAWordList(individualList, definition, example, grammarMark, markCulturalLevel, markStyleValue, locution, locutionType);


        model.addAttribute("words", wordList);

        return "/freemarker/summary";
    }


    @RequestMapping("/individuals")
    public String showAllIndividuals(Model model) {

        List<String> individualListNames = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            individualListNames.add(individual.getLocalName());
        }

        model.addAttribute("individuals", individualListNames);

        return "/freemarker/individuals";
    }


    @RequestMapping("/creation")
    public String creationPage(Model model) {

        List<String> classListNames = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();

            classListNames.add(nextClass.getLocalName());
        }

        model.addAttribute("classes", classListNames);
        model.addAttribute("title","Welcome");

        return "/freemarker/createIndividual";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName, @RequestParam("definition") String definition, @RequestParam("example") String example, @RequestParam("mark") String mark) {

        ontologyService.saveIndividual(individualName, fatherClassName, definition, example, mark);

        return "redirect:/words/";
    }


    @RequestMapping("/edition")
    public String getIndividualByName(Model model, @RequestParam("individualName") String individualName)  {

        List<String> classListNames = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();

            classListNames.add(nextClass.getLocalName());
        }

        String individualURI = uriService.ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);


        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.grammarMarkURI);


        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);


        model.addAttribute("fatherClass", individual.getOntClass().getLocalName());
        model.addAttribute("classes", classListNames);
        model.addAttribute("lema", individual.getLocalName());
        model.addAttribute("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
            model.addAttribute("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());

        return "/freemarker/editIndividual";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam("individualName") String individualName, @RequestParam("definition") String definition, @RequestParam("example") String example, @RequestParam("mark") String mark,  @RequestParam("fatherClassName") String fatherClassName) {

        ontologyService.saveIndividual(individualName, fatherClassName, definition, example, mark);

        return "redirect:/words/individuals";
    }


    @RequestMapping("/delete")
    public String deleteIndividual(@RequestParam("individualName") String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/words/individuals";
    }
}
