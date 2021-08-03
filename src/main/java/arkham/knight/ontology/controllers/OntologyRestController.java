package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RequestMapping("/api/v1/")
@RestController
public class OntologyRestController {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    private final OntologyService ontologyService;

    private final WordService wordService;

    public OntologyRestController(OntologyService ontologyService, WordService wordService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
    }


//    @GetMapping("/ontology/individuals/reasoner/{fatherClassName}")
//    @Operation(summary = "Get All Individuals Name By Class Name Reasoner Way", description = "Retorna una lista con los lemas pertenecientes a la clase indicada, haciendo uso del reasoner")
//    public ResponseEntity<List<String>> getAllIndividualNameByClassNameWithReasoner(@PathVariable String fatherClassName) {
//
//        List<String> individualSet = ontologyService.getAllIndividualNameByClassNameWithReasoner(fatherClassName);
//
//        return new ResponseEntity<>(individualSet, HttpStatus.OK);
//    }


    @PostMapping("/ontology/classes/{fatherClassName}/{subClassName}")
    @Operation(summary = "Create FatherClass And SubClass", description = "Creacion de una clase padre y su clase hijo")
    public ResponseEntity<String> createClasses(@PathVariable String fatherClassName, @PathVariable String subClassName) {

        String response = ontologyService.saveFatherClassAndSubClass(fatherClassName, subClassName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/ontology/classes/{className}")
    @Operation(summary = "Create Father Class", description = "Creacion de una clase padre")
    public ResponseEntity<String> createClass(@PathVariable String className) {

        Word defaultTestWord = new Word("prueba","definition","example", className,"individualNameRae", "synonims", "0", "0");

        ontologyService.saveIndividual(defaultTestWord.getLema(), defaultTestWord);

        return new ResponseEntity<>("Class Saved", HttpStatus.OK);
    }


    @PostMapping("/ontology/individuals")
    @Operation(summary = "Create Individual", description = "Creará una individual con los datos que sean especificados")
    public ResponseEntity<String> saveIndividual(@RequestBody Word wordToSave) {

        String response = ontologyService.saveIndividual(wordToSave.getLema(), wordToSave);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/ontology/individuals/{individualName}")
    @Operation(summary = "Delete Individual", description = "Eliminará el individual cuyo nombre sea especificado")
    public ResponseEntity<String> deleteIndividual(@PathVariable String individualName) {

        boolean response = ontologyService.deleteIndividual(individualName);

        if (!response)
            return new ResponseEntity<>("Individual Not Found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>("Individual Deleted", HttpStatus.OK);
    }


    @PutMapping("/ontology/individuals/{individualName}")
    @Operation(summary = "Edit Individual", description = "Editará el individual cuyo nombre sea especificado")
    public ResponseEntity<String> editIndividual(@PathVariable String individualName, @RequestParam(defaultValue = "") String individualNameToEdit, @RequestParam(defaultValue = "") String individualNameRAE, @RequestParam(defaultValue = "") String fatherClassName, @RequestParam(defaultValue = "") String definition, @RequestParam(defaultValue = "") String example, @RequestParam(defaultValue = "") String synonyms) {

        Word wordDataToSave = new Word(individualNameToEdit, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        Word wordToEdit = wordService.getWordByLemma(individualName);

        Word filteredWord = wordService.editionWordFilter(wordToEdit, wordDataToSave);

        return new ResponseEntity<>(ontologyService.saveIndividual(individualName, filteredWord), HttpStatus.OK);
    }


    @GetMapping("/ontology/search/{sentence}")
    @Operation(summary = "Search For Any Word In The Ontology", description = "Buscara las distintas palabras dominicanas en la ontología de cualquier oración que se digite")
    public ResponseEntity<List<Word>> getAllIndividualPropertiesByName(@PathVariable String sentence) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, "tweet");

        List<Word> wordList = ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);

        return new ResponseEntity<>(wordService.evaluateWordsAndReturnCleanWordList(wordList), HttpStatus.OK);
    }


    @GetMapping("/ontology/individuals")
    @Operation(summary = "Get All Individuals", description = "Retorna una lista con todas las individuales contenidas en la ontología")
    public ResponseEntity<List<HashMap<String, String>>> getAllIndividuals() {

        List<HashMap<String, String>> individualList = new ArrayList<>();

        List<Individual> allIndividuals = ontologyConnectionService.getAllIndividuals();

        for (Individual individual : allIndividuals) {

            HashMap<String, String> individualToSave = new HashMap<>();

            individualToSave.put("fatherClass", individual.getOntClass().getLocalName());
            individualToSave.put("name", individual.getLocalName());
            individualToSave.put("uri", individual.getURI());

            individualList.add(individualToSave);
        }

        return new ResponseEntity<>(individualList, HttpStatus.OK);
    }


    @GetMapping("/ontology/classes")
    @Operation(summary = "Get All Classes", description = "Retorna una lista con todas las clases contenidas en la ontología")
    public ResponseEntity<List<HashMap<String, String>>> getAllClasses() {

        List<HashMap<String, String>> classList = new ArrayList<>();

        List<OntClass> allClasses = ontologyConnectionService.getAllClasses();

        for (OntClass ontClass : allClasses) {

            HashMap<String, String> classToSave = new HashMap<>();

            classToSave.put("name", ontClass.getLocalName());
            classToSave.put("uri", ontClass.getURI());

            classList.add(classToSave);
        }

        return new ResponseEntity<>(classList, HttpStatus.OK);
    }


    @GetMapping("/ontology/datatype-properties")
    @Operation(summary = "Get All Datatype Properties", description = "Retorna una lista con todas las propiedades de datos contenidas en la ontología")
    public ResponseEntity<List<HashMap<String, String>>> getAllDatatypeProperties() {

        List<HashMap<String, String>> propertyList = new ArrayList<>();

        List<DatatypeProperty> allDataTypeProperties = ontologyConnectionService.getAllDataTypeProperties();

        for (DatatypeProperty datatypeProperty : allDataTypeProperties) {

            HashMap<String, String> propertyToSave = new HashMap<>();

            propertyToSave.put("domain", datatypeProperty.getDomain().getLocalName());
            propertyToSave.put("property", datatypeProperty.getLocalName());
            propertyToSave.put("uri", datatypeProperty.getURI());
            propertyToSave.put("datatype", datatypeProperty.getRange().getLocalName());

            propertyList.add(propertyToSave);
        }

        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }


    @GetMapping("/ontology")
    @Operation(summary = "Get Ontology Data", description = "Retorna datos generales de la ontología")
    public ResponseEntity<HashMap<String, String>> getOntologyData() {

        HashMap<String, String> ontologyData = new HashMap<>();

        ontologyData.put("name", ontologyConnectionService.getOntology().getLocalName());
        ontologyData.put("uri", ontologyConnectionService.getOntology().getURI());
        ontologyData.put("namespace", ontologyConnectionService.getOntology().getNameSpace());
        ontologyData.put("version", ontologyConnectionService.getOntology().getVersionInfo());

        return new ResponseEntity<>(ontologyData, HttpStatus.OK);
    }
}
