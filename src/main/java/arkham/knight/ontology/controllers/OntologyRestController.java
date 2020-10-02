package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.URIService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
public class OntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private URIService uriService;


    @GetMapping("/find")
    public List<Word> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "morirsoñando") String tweet, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(tweet), searchType);


        Property definition = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.definitionURI);

        Property example = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.exampleURI);

        Property grammarMark = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.grammarMarkURI);

        Property markCulturalLevel = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.markSocialCulturalLevelURI);

        Property markStyleValue = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.markStyleVariationURI);

        Property locution = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.locutionURI);


        return ontologyService.saveAllPropertiesValueInAWordList(individualList, definition, example, grammarMark, markCulturalLevel, markStyleValue, locution);
    }


    @PostMapping("/createClass")
    public String createClasses(@RequestParam("className") String className, @RequestParam("className2") String className2) {

        ontologyService.saveClasses(className, className2);

        return "classes Saved";
    }


    @PostMapping("/createIndividual")
    public String createIndividual(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName, @RequestParam("definition") String definition, @RequestParam("example") String example, @RequestParam("mark") String mark) {

        ontologyService.saveIndividual(individualName, fatherClassName, definition, example, mark);

        return "individual Saved";
    }


    @GetMapping("/individuals")
    public List<HashMap<String, String>> getIndividuals() {

        List<HashMap<String, String>> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            HashMap<String, String> individualToSave = new HashMap<>();

            individualToSave.put("domain", individual.getOntClass().getLocalName());
            individualToSave.put("name", individual.getLocalName());

            individualList.add(individualToSave);
        }

        return individualList;
    }


    @GetMapping("/classes")
    public List<HashMap<String, String>> getClasses() {

        List<HashMap<String, String>> classList = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();

            HashMap<String, String> classToSave = new HashMap<>();

            classToSave.put("name", nextClass.getLocalName());
            classToSave.put("uri", nextClass.getURI());

            classList.add(classToSave);
        }

        return classList;
    }


    @GetMapping("/datatype")
    public List<HashMap<String, String>> getAllDatatypeProperties() {

        List<HashMap<String, String>> propertyList = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyService.readOntologyFileAndReturnTheModel().listDatatypeProperties();


        while (propertyIterator.hasNext()) {

            DatatypeProperty nextProperty = propertyIterator.next();

            HashMap<String, String> propertyToSave = new HashMap<>();

            propertyToSave.put("domain", nextProperty.getDomain().getLocalName());
            propertyToSave.put("property", nextProperty.getLocalName());
          //  propertyToSave.put("datatype", nextProperty.getRange().getLocalName());

            propertyList.add(propertyToSave);
        }

        return propertyList;
    }
}
