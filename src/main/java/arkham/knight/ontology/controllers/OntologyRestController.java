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


    @GetMapping("/individuals/reasoner/{fatherClassName}")
    @Operation(summary = "Get All Individuals Name By Class Name Reasoner Way", description = "Retorna los lemas pertenecientes a la clase indicada")
    public ResponseEntity<List<String>> getAllIndividualNameByClassNameWithReasoner(@PathVariable String fatherClassName) {

        List<String> individualSet = ontologyService.getAllIndividualNameByClassNameWithReasoner(fatherClassName);

        return new ResponseEntity<>(individualSet, HttpStatus.OK);
    }


    @PostMapping("/classes/{fatherClassName}/{subClassName}")
    @Operation(summary = "Create FatherClass And SubClass", description = "Creacion de clases padre y clase hijo")
    public ResponseEntity<String> createClasses(@PathVariable String fatherClassName, @PathVariable String subClassName) {

        String response = ontologyService.saveFatherClassAndSubClass(fatherClassName, subClassName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/classes/{className}")
    @Operation(summary = "Create Class", description = "Creacion de una clase")
    public ResponseEntity<String> createClass(@PathVariable String className) {

        Word defaultTestWord = new Word("prueba","definition","example", className,"individualNameRae", "synonims", "0", "0");

        ontologyService.saveIndividual(defaultTestWord.getLema(), defaultTestWord);

        return new ResponseEntity<>("Class Saved", HttpStatus.OK);
    }


    @GetMapping("/individuals")
    @Operation(summary = "Get All Individuals", description = "Retorna una lista con todas las individuales")
    public ResponseEntity<List<HashMap<String, String>>> getAllIndividuals() {

        List<HashMap<String, String>> individualList = new ArrayList<>();

        List<Individual> allIndividuals = ontologyConnectionService.getAllIndividuals();

        for (Individual individual : allIndividuals) {

            HashMap<String, String> individualToSave = new HashMap<>();

            individualToSave.put("domain", individual.getOntClass().getLocalName());
            individualToSave.put("name", individual.getLocalName());

            individualList.add(individualToSave);
        }

        return new ResponseEntity<>(individualList, HttpStatus.OK);
    }


    @PostMapping("/individuals")
    @Operation(summary = "Create Individual", description = "Creacion de individual")
    public ResponseEntity<String> saveIndividual(@RequestBody Word wordToSave) {

        String response = ontologyService.saveIndividual(wordToSave.getLema(), wordToSave);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/individuals/{individualName}")
    @Operation(summary = "Delete Individual", description = "Elimina el individual cuyo nombre sea especificado")
    public ResponseEntity<String> deleteIndividual(@PathVariable String individualName) {

        boolean response = ontologyService.deleteIndividual(individualName);

        if (!response)
            return new ResponseEntity<>("Individual Not Found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>("Individual Deleted", HttpStatus.OK);
    }


    @PutMapping("/individuals/{originalIndividualName}")
    @Operation(summary = "Edit Individual", description = "Edita el individual cuyo nombre sea especificado")
    public ResponseEntity<String> editIndividual(@PathVariable String originalIndividualName, @RequestParam(defaultValue = "") String individualName, @RequestParam(defaultValue = "") String individualNameRAE, @RequestParam(defaultValue = "") String fatherClassName, @RequestParam(defaultValue = "") String definition, @RequestParam(defaultValue = "") String example, @RequestParam(defaultValue = "") String synonyms) {

        Word wordDataToSave = new Word(individualName, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        Word wordToEdit = wordService.getWordByLemma(originalIndividualName);

        Word filteredWord = wordService.editionWordFilter(wordToEdit, wordDataToSave);

        String response = ontologyService.saveIndividual(originalIndividualName, filteredWord);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/classes")
    @Operation(summary = "Get All Classes", description = "Retorna una lista con todas las clases")
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


    @GetMapping("/datatype-properties")
    @Operation(summary = "Get All Datatype Properties", description = "Retorna una lista con todas las propiedades")
    public ResponseEntity<List<HashMap<String, String>>> getAllDatatypeProperties() {

        List<HashMap<String, String>> propertyList = new ArrayList<>();

        List<DatatypeProperty> allDataTypeProperties = ontologyConnectionService.getAllDataTypeProperties();

        for (DatatypeProperty datatypeProperty : allDataTypeProperties) {

            HashMap<String, String> propertyToSave = new HashMap<>();

            propertyToSave.put("domain", datatypeProperty.getDomain().getLocalName());
            propertyToSave.put("property", datatypeProperty.getLocalName());
            propertyToSave.put("datatype", datatypeProperty.getRange().getLocalName());

            propertyList.add(propertyToSave);
        }

        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }


    @GetMapping("/ontology")
    @Operation(summary = "Get Ontology Data", description = "Retorna datos de la ontologia")
    public ResponseEntity<HashMap<String, String>> getOntology() {

        HashMap<String, String> ontologyData = new HashMap<>();

        ontologyData.put("name", ontologyConnectionService.getOntology().getLocalName());
        ontologyData.put("uri", ontologyConnectionService.getOntology().getURI());
        ontologyData.put("namespace", ontologyConnectionService.getOntology().getNameSpace());
        ontologyData.put("version", ontologyConnectionService.getOntology().getVersionInfo());

        return new ResponseEntity<>(ontologyData, HttpStatus.OK);
    }
}
