package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.json.simple.JSONObject;
import org.apache.jena.ontology.Ontology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class OntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    private final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";


    @RequestMapping("/properties")
    public List<JSONObject> getIndividualPropertiesAndValues(@RequestParam("individualName") String individualName) throws FileNotFoundException {

        List<JSONObject> list = new ArrayList<>();

        String individualURI = ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        String definitionURI = ontologyURI.concat("definicion");

        String exampleURI = ontologyURI.concat("ejemplo");

        String grammarMarkURI = ontologyURI.concat("marca_gramatical");

        String markSocialCulturalURI = ontologyURI.concat("marca_gramatical");

        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);

        Property markSocialCulturalProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(markSocialCulturalURI);

        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);

        RDFNode markSocialCulturalPropertyValue = individual.getPropertyValue(markSocialCulturalProperty);


        JSONObject jsonObject = new JSONObject();

        jsonObject.put("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
        jsonObject.put("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            jsonObject.put("ejemplo", examplePropertyValue.toString());

        /*if (markSocialCulturalPropertyValue!= null)
            jsonObject.put("ejemplo", markSocialCulturalPropertyValue.toString());*/

        list.add(jsonObject);

        return list;
    }


    @RequestMapping("/createClass")
    public String createClasses(@RequestParam("className") String className, @RequestParam("className2") String className2) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {

        ontologyService.saveClasses(className, className2);

        return "classes Saved";
    }


    @RequestMapping("/createIndividual")
    public String createIndividual(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {

        ontologyService.saveIndividual(individualName, fatherClassName);

        return "individual Saved";
    }


    @RequestMapping("/individuals")
    public List<JSONObject> getIndividuals() throws FileNotFoundException {

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
    public List<JSONObject> getOntology() throws FileNotFoundException {

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
    public List<JSONObject> getClasses() throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {

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
    public List<JSONObject> getAllDatatypeProperties() throws FileNotFoundException {

        List<JSONObject> list = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyService.readOntologyFileAndReturnTheModel().listDatatypeProperties();


        while (propertyIterator.hasNext()) {

            DatatypeProperty nextProperty = propertyIterator.next();

            JSONObject jsonObject = new JSONObject();

//            jsonObject.put("domain", nextProperty.getDomain().getLocalName());
            jsonObject.put("property", nextProperty.getLocalName());
//            jsonObject.put("datatype", nextProperty.getRange().getLocalName());

            list.add(jsonObject);
        }

        return list;
    }
}
