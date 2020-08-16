package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
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
import org.springframework.web.bind.annotation.GetMapping;
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

    private final String definitionURI = ontologyURI.concat("definicion");

    private final String exampleURI = ontologyURI.concat("ejemplo");

    private final String grammarMarkURI = ontologyURI.concat("marca_gramatical");

    private final String marcaNivelSocioCulturalURI = ontologyURI.concat("marca_nivel_sociocultural");

    private final String marcaVariacionEstilisticaURI = ontologyURI.concat("marca_variacion_estilistica");

    private final String locutionURI = ontologyURI.concat("locucion");

    private final String locutionTypeURI = ontologyURI.concat("tipo_locucion");


    @GetMapping("/find")
    public List<Word> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "morirsoñando") String tweet) throws FileNotFoundException {

        List<Word> wordList;

        List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(tweet));

        Property definition = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property example = ontologyService.readOntologyFileAndReturnTheModel().getProperty(exampleURI);

        Property grammarMark = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);

        Property marcaNivelSocioCultural = ontologyService.readOntologyFileAndReturnTheModel().getProperty(marcaNivelSocioCulturalURI);

        Property marcaVariacionEstilistica = ontologyService.readOntologyFileAndReturnTheModel().getProperty(marcaVariacionEstilisticaURI);

        Property locution = ontologyService.readOntologyFileAndReturnTheModel().getProperty(locutionURI);

        Property locutionType = ontologyService.readOntologyFileAndReturnTheModel().getProperty(locutionTypeURI);

        wordList = ontologyService.saveAllPropertiesValueInAWordList(individualList, definition, example, grammarMark, marcaNivelSocioCultural, marcaVariacionEstilistica, locution, locutionType);

        return wordList;
    }


    @GetMapping("/properties")
    public List<JSONObject> getIndividualPropertiesAndValues(@RequestParam("individualName") String individualName) throws FileNotFoundException {

        List<JSONObject> list = new ArrayList<>();

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



        JSONObject jsonObject = new JSONObject();

        jsonObject.put("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
        jsonObject.put("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            jsonObject.put("ejemplo", examplePropertyValue.toString());


        list.add(jsonObject);

        return list;
    }


    @RequestMapping("/createClass")
    public String createClasses(@RequestParam("className") String className, @RequestParam("className2") String className2) throws OWLOntologyCreationException, OWLOntologyStorageException {

        ontologyService.saveClasses(className, className2);

        return "classes Saved";
    }


    @RequestMapping("/createIndividual")
    public String createIndividual(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName) throws OWLOntologyCreationException, OWLOntologyStorageException {

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


    @GetMapping("/ontology")
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


    @GetMapping("/classes")
    public List<JSONObject> getClasses() throws FileNotFoundException {

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
