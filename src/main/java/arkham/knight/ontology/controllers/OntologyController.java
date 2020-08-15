package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/words")
@Controller
public class OntologyController {

    @Autowired
    private OntologyService ontologyService;

    private final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";


    @RequestMapping("/")
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "Despues que le di morirsoñando a fulana me di cuenta que ella es una robamaridos porque ella no se digno a siquiera ser una lavaplatos para asi ser una rompehuelgas y no ser una entretiempo") String individualName) throws FileNotFoundException {

        List<String> lemaList = new ArrayList<>();

        List<String> definitionsList = new ArrayList<>();

        List<String> examplesList = new ArrayList<>();

        List<String> grammarMarkList = new ArrayList<>();

      //  String individualURI = ontologyURI.concat(individualName);

        //Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(individualName));

        String definitionURI = ontologyURI.concat("definicion");

        String exampleURI = ontologyURI.concat("ejemplo");

        String grammarMarkURI = ontologyURI.concat("marca_gramatical");


        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);


        for (Individual individual: individualList) {

            lemaList.add(individual.getLocalName());

            RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

            definitionsList.add(definitionPropertyValue.toString());

            //RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

            //examplesList.add(examplePropertyValue.toString());

            //RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);

            //grammarMarkList.add(grammarMarkPropertyValue.toString());
        }



        model.addAttribute("lemas", lemaList);

        model.addAttribute("definitions", definitionsList);

       // if (grammarMarkPropertyValue != null)
          //  model.addAttribute("marca_gramaticales", grammarMarkList);

       // if (examplePropertyValue != null)
          //  model.addAttribute("examples", examplesList);


        return "/freemarker/summary";
    }


    @RequestMapping("/creation")
    public String creationPage(Model model) throws FileNotFoundException {

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


    @RequestMapping("/individuals")
    public String showAllIndividuals(Model model) throws FileNotFoundException {

        List<String> individualListNames = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            individualListNames.add(individual.getLocalName());
        }

        model.addAttribute("individuals", individualListNames);

        return "/freemarker/individuals";
    }


    @RequestMapping("/edition")
    public String getIndividualByName(Model model, @RequestParam("individualName") String individualName) throws FileNotFoundException {

        String individualURI = ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        String definitionURI = ontologyURI.concat("definicion");

        String exampleURI = ontologyURI.concat("ejemplo");

        String grammarMarkURI = ontologyURI.concat("marca_gramatical");


        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);


        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);


        model.addAttribute("lema", individual.getLocalName());

        model.addAttribute("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
            model.addAttribute("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());


        return "/freemarker/editIndividual";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(name = "individualName") String individualName, @RequestParam(name = "fatherClassName") String fatherClassName) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {

        ontologyService.saveIndividual(individualName, fatherClassName);

        return "redirect:/words/";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam(name = "individualName") String individualName, @RequestParam(name = "fatherClassName") String fatherClassName) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {

        ontologyService.saveIndividual(individualName, fatherClassName);

        return "redirect:/words/";
    }


    @RequestMapping("/delete")
    public String deleteIndividual(@RequestParam(name = "individualName") String individualName) throws OWLOntologyCreationException, OWLOntologyStorageException {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/words/individuals";
    }
}
