package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class OntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private OntologyConnectionService ontologyConnectionService;

    @Autowired
    private WordService wordService;


    @GetMapping("/getAllIndividuals")
    @Operation(summary = "Get All Individuals Properties By Name", description = "Buscara las distintas palabras dominicanas de cualquier oracion que se digite")
    public List<Word> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "morirsoñando") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        return ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);
    }


    @GetMapping("/getIndividual")
    @Operation(summary = "Get a individual by name", description = "Retornara el individual del lema indicado")
    public Word findIndividualByName(@RequestParam("individualName") String individualName) {

        return wordService.getWordByLemma(individualName);
    }


    @PostMapping("/createClass")
    @Operation(summary = "Create Class", description = "Creacion de clases padre y clase hijo")
    public String createClasses(@RequestParam("className") String className, @RequestParam("className2") String className2) {

        ontologyService.saveClasses(className, className2);

        return "classes Saved";
    }


    @PostMapping("/createIndividual")
    @Operation(summary = "Create Individual", description = "Creacion de individual")
    public String createIndividual(@RequestParam("individualName") String individualName, @RequestParam("fatherClassName") String fatherClassName, @RequestParam("definition") String definition, @RequestParam(required = false, defaultValue = "N/A") String example) {

        ontologyService.saveIndividual(individualName, individualName, fatherClassName, definition, example);

        return "individual Saved";
    }


    @DeleteMapping("/deleteIndividual")
    @Operation(summary = "Delete Individual", description = "Elimina el individual cuyo nombre sea especificado")
    public String deleteIndividual(@RequestParam("individualName") String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "Individual Deleted";
    }


    @PutMapping("/editIndividual")
    @Operation(summary = "Edit Individual", description = "Edita el individual cuyo nombre sea especificado")
    public String editIndividual(@RequestParam("originalIndividualName") String originalIndividualName, @RequestParam(required = false) String individualName, @RequestParam(required = false) String fatherClassName, @RequestParam(required = false) String definition, @RequestParam(required = false) String example) {

        ontologyService.saveIndividual(originalIndividualName, individualName, fatherClassName, definition, example);

        return "Individual Saved";
    }


    @GetMapping("/individuals")
    @Operation(summary = "Get Individuals", description = "Retorna una lista con todos los individuales")
    public List<HashMap<String, String>> getIndividuals() {

        List<HashMap<String, String>> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();


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
    @Operation(summary = "Get Classes", description = "Retorna una lista con todas las clases")
    public List<HashMap<String, String>> getClasses() {

        List<HashMap<String, String>> classList = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listClasses();


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
    @Operation(summary = "Get All Datatype Properties", description = "Retorna una lista con todas las propiedades")
    public List<HashMap<String, String>> getAllDatatypeProperties() {

        List<HashMap<String, String>> propertyList = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listDatatypeProperties();


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
