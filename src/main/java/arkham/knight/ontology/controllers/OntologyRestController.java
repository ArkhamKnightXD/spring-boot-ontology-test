package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Statement;
import org.json.simple.JSONObject;
import org.apache.jena.ontology.Ontology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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


    @RequestMapping("/ontology")
    public List<JSONObject> getOntology() {

        List<JSONObject> list = new ArrayList<>();

        Iterator<Ontology> ontologyIterator = ontologyService.readOntologyFileAndReturn().listOntologies();


        while (ontologyIterator.hasNext()) {

            Ontology ontology = ontologyIterator.next();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name",ontology.getLocalName());
            jsonObject.put("space",ontology.getNameSpace());
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
            jsonObject.put("namespace" , nextClass.getNameSpace());

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
            jsonObject.put("namespace" , individual.getNameSpace());

            list.add(jsonObject);
        }

        return list;
    }


    @GetMapping("/getIndividualProperty")
    public List<Statement> getClassProperty(@RequestParam("individualName") String individualName) {

        String individualURI = "http://www.semanticweb.org/karvi/ontologies/2020/6/untitled-ontology-3#".concat(individualName);
        Individual individual = ontologyService.readOntologyFileAndReturn().getIndividual(individualURI);

        List<Statement> propertiesList = individual.listProperties().toList();

        /*while (subIter.hasNext()) {

            OntProperty property = (OntProperty) subIter.next();
            JSONObject obj = new JSONObject();
            obj.put("propertyName",property.getLocalName());

            obj.put("propertyType",property.getRDFType().getLocalName());

            if(property.getDomain()!=null)
                obj.put("domain", property.getDomain().getLocalName());
            if(property.getRange()!=null)
                obj.put("range",property.getRange().getLocalName());

            list.add(obj);
        }*/

        return propertiesList;
    }
}
