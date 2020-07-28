package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.simple.JSONObject;
import org.apache.jena.ontology.Ontology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

        Individual individual = ontologyService.readOntologyFileAndReturn().getIndividual(individualURI);

        Iterator properties = individual.listProperties();

        while (properties.hasNext()) {

            //Aqui es que falla intento castear de statement a property
            Property property = (Property) properties.next();
            JSONObject jsonObject = new JSONObject();

            RDFNode value = individual.getPropertyValue(property);

            jsonObject.put("name", property.getLocalName());
            jsonObject.put("uri", property.getURI());

            list.add(jsonObject);
        }

        return list;
    }

    @RequestMapping("/ontology")
    public List<JSONObject> getOntology() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<Ontology> ontologyIterator = ontologyService.readOntologyFileAndReturn().listOntologies();


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

        Iterator<OntClass> classesIterator = ontologyService.readOntologyFileAndReturn().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name", nextClass.getLocalName());
            jsonObject.put("uri", nextClass.getURI());

            list.add(jsonObject);
        }

        return list;
    }


    @RequestMapping("/individuals")
    public List<JSONObject> getIndividuals() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyService.readOntologyFileAndReturn().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name", individual.getLocalName());
            jsonObject.put("uri", individual.getURI());

            list.add(jsonObject);
        }

        return list;
    }


    @RequestMapping("/datatype")
    public List<JSONObject> getAllDatatypeProperties() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyService.readOntologyFileAndReturn().listDatatypeProperties();


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
