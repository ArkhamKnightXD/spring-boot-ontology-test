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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Word>> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "morirsoñando") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        return new ResponseEntity<>(ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList), HttpStatus.OK);
    }


    @GetMapping("/getIndividual")
    @Operation(summary = "Get a individual by name", description = "Retornara el individual del lema indicado")
    public ResponseEntity<Word> findIndividualByName(@RequestParam() String individualName) {

        return new ResponseEntity<>(wordService.getWordByLemma(individualName), HttpStatus.OK);
    }


    @PostMapping("/createClass")
    @Operation(summary = "Create Class", description = "Creacion de clases padre y clase hijo")
    public ResponseEntity<String> createClasses(@RequestParam() String className, @RequestParam() String className2) {

        ontologyService.saveClasses(className, className2);

        return new ResponseEntity<>("classes Saved", HttpStatus.OK) ;
    }


    @PostMapping("/createIndividual")
    @Operation(summary = "Create Individual", description = "Creacion de individual")
    public ResponseEntity<String> createIndividual(@RequestParam() String fatherClassName, @RequestBody Word word) {

        ontologyService.saveIndividual(word.getLema(), word.getLema(), fatherClassName, word.getDefinicion(), word.getEjemplo());

        return new ResponseEntity<>("individual Saved", HttpStatus.OK);
    }


    @DeleteMapping("/deleteIndividual")
    @Operation(summary = "Delete Individual", description = "Elimina el individual cuyo nombre sea especificado")
    public ResponseEntity<String> deleteIndividual(@RequestParam() String individualName) {

        ontologyService.deleteIndividual(individualName);

        return new  ResponseEntity<>("Individual Deleted", HttpStatus.OK);
    }


    @PutMapping("/editIndividual")
    @Operation(summary = "Edit Individual", description = "Edita el individual cuyo nombre sea especificado")
    public ResponseEntity<String> editIndividual(@RequestParam() String originalIndividualName, @RequestParam(required = false) String individualName, @RequestParam(required = false) String fatherClassName, @RequestParam(required = false) String definition, @RequestParam(required = false) String example) {

        //tengo que ver como poner a funcionar correctamente este edit
        Word wordToEdit = wordService.getWordByLemma(originalIndividualName);

        ontologyService.saveIndividual(originalIndividualName, individualName, fatherClassName, definition, example);

        return new ResponseEntity<>("individual Saved", HttpStatus.OK);
    }


    @GetMapping("/individuals")
    @Operation(summary = "Get Individuals", description = "Retorna una lista con todos los individuales")
    public ResponseEntity<List<HashMap<String, String>>> getIndividuals() {

        List<HashMap<String, String>> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            HashMap<String, String> individualToSave = new HashMap<>();

            individualToSave.put("domain", individual.getOntClass().getLocalName());
            individualToSave.put("name", individual.getLocalName());

            individualList.add(individualToSave);
        }

        return new ResponseEntity<>(individualList, HttpStatus.OK);
    }


    @GetMapping("/classes")
    @Operation(summary = "Get Classes", description = "Retorna una lista con todas las clases")
    public ResponseEntity<List<HashMap<String, String>>> getClasses() {

        List<HashMap<String, String>> classList = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();

            HashMap<String, String> classToSave = new HashMap<>();

            classToSave.put("name", nextClass.getLocalName());
            classToSave.put("uri", nextClass.getURI());

            classList.add(classToSave);
        }

        return new ResponseEntity<>(classList, HttpStatus.OK);
    }


    @GetMapping("/datatype")
    @Operation(summary = "Get All Datatype Properties", description = "Retorna una lista con todas las propiedades")
    public ResponseEntity<List<HashMap<String, String>>> getAllDatatypeProperties() {

        List<HashMap<String, String>> propertyList = new ArrayList<>();

        Iterator<DatatypeProperty> propertyIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listDatatypeProperties();


        while (propertyIterator.hasNext()) {

            DatatypeProperty nextProperty = propertyIterator.next();

            HashMap<String, String> propertyToSave = new HashMap<>();

            propertyToSave.put("domain", nextProperty.getDomain().getLocalName());
            propertyToSave.put("property", nextProperty.getLocalName());
            propertyToSave.put("datatype", nextProperty.getRange().getLocalName());

            propertyList.add(propertyToSave);
        }

        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }
}
