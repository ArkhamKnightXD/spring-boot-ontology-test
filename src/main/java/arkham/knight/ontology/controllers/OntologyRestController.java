package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.URIService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class OntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private URIService uriService;


    @GetMapping("/find")
    public List<Word> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "morirsoñando") String tweet) {

        List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(tweet));

        Property definition = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.definitionURI);

        Property example = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.exampleURI);

        Property grammarMark = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.grammarMarkURI);

        Property marcaNivelSocioCultural = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.marcaNivelSocioCulturalURI);

        Property marcaVariacionEstilistica = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.marcaVariacionEstilisticaURI);

        Property locution = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.locutionURI);

        Property locutionType = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.locutionTypeURI);


        return ontologyService.saveAllPropertiesValueInAWordList(individualList, definition, example, grammarMark, marcaNivelSocioCultural, marcaVariacionEstilistica, locution, locutionType);
    }


    @GetMapping("/properties")
    public List<JSONObject> getIndividualPropertiesAndValues(@RequestParam("individualName") String individualName) {

        List<JSONObject> list = new ArrayList<>();

        String individualURI = uriService.ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);


        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(uriService.grammarMarkURI);


        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);



        JSONObject jsonObject = new JSONObject();

        jsonObject.put("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
        jsonObject.put("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            jsonObject.put("ejemplo", examplePropertyValue.toString());


        list.add(jsonObject);

        return list;
    }


    @PostMapping("/createClass")
    public String createClasses(@RequestParam("className") String className, @RequestParam("className2") String className2) {

        ontologyService.saveClasses(className, className2);

        return "classes Saved";
    }


    @PostMapping("/createIndividual")
    public String createIndividual(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName) {

        ontologyService.saveIndividual(individualName, fatherClassName);

        return "individual Saved";
    }


    @GetMapping("/individuals")
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


    @GetMapping("/classes")
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


    @GetMapping("/datatype")
    public List<JSONObject> getAllDatatypeProperties() {

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
