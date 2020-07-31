package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.json.simple.JSONObject;
import org.apache.jena.ontology.Ontology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class OntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    private final String ontologyURI = "http://www.semanticweb.org/karvi/ontologies/2020/6/untitled-ontology-3#";


    @RequestMapping("/properties")
    public List<JSONObject> getIndividualPropertiesAndValues(@RequestParam("individualName") String individualName) {

        List<JSONObject> list = new ArrayList<>();

        String individualURI = ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        String definitionURI = ontologyURI.concat("definicion");

        String wordURI = ontologyURI.concat("lema");

        String grammarMarkURI = ontologyURI.concat("marca_gramatical");

        String markSocialCulturalURI = ontologyURI.concat("marca_nivel_sociocultural");

        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property wordProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(wordURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);

        Property markSocialCulturalProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(markSocialCulturalURI);

        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode wordPropertyValue = individual.getPropertyValue(wordProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);

        RDFNode markSocialCulturalPropertyValue = individual.getPropertyValue(markSocialCulturalProperty);


        JSONObject jsonObject = new JSONObject();

        jsonObject.put("definicion", definitionPropertyValue.toString());
        jsonObject.put("lema", wordPropertyValue.toString());
        jsonObject.put("marca_gramatical", grammarMarkPropertyValue.toString());

        if (markSocialCulturalPropertyValue!= null)
            jsonObject.put("marca_nivel_sociocultural", markSocialCulturalPropertyValue.toString());

        list.add(jsonObject);

        return list;
    }


    @RequestMapping("/createInstance")
    public String createInstance(@RequestParam("className") String className, @RequestParam("individualName") String individualName) throws IOException {

        String classURI = ontologyURI.concat(className);

        String individualURI = ontologyURI.concat(individualName);

        OntClass ontClass = ontologyService.readOntologyFileAndReturnTheModel().getOntClass(classURI);

        Individual individualToCreate = ontClass.createIndividual(individualURI);

       // ontologyService.saveOntologyFile(ontologyService.readOntologyFileAndReturnTheModel(), individualToCreate);

        return individualToCreate.getURI();
    }


    @RequestMapping("/individuals")
    public List<JSONObject> getIndividuals() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name", individual.getLocalName());

            list.add(jsonObject);
        }

        return list;
    }


    @RequestMapping("/ontology")
    public List<JSONObject> getOntology() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<Ontology> ontologyIterator = ontologyService.readOntologyFileAndReturnTheModel().listOntologies();


        while (ontologyIterator.hasNext()) {

            Ontology ontology = ontologyIterator.next();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name",ontology.getLocalName());
            jsonObject.put("uri",ontology.getURI());

            list.add(jsonObject);
        }

        return list;
    }


    @RequestMapping("/classes")
    public List<JSONObject> getClasses() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name", nextClass.getLocalName());
            jsonObject.put("uri", nextClass.getURI());

            list.add(jsonObject);
        }

        return list;
    }


    @RequestMapping("/datatype")
    public List<JSONObject> getAllDatatypeProperties() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyService.readOntologyFileAndReturnTheModel().listDatatypeProperties();


        while (propertyIterator.hasNext()) {

            DatatypeProperty nextProperty = propertyIterator.next();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("domain", nextProperty.getDomain().getLocalName());
            jsonObject.put("property", nextProperty.getLocalName());
            jsonObject.put("datatype", nextProperty.getRange().getLocalName());

            list.add(jsonObject);
        }

        return list;
    }
}
