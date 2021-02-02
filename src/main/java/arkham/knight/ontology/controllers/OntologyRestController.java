package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.DRAEConnectionService;
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
import org.springframework.web.client.RestTemplate;
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

    @Autowired
    private DRAEConnectionService draeConnectionService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/search-DRAE")
    @Operation(summary = "Search For Any Word In The DRAE Endpoint", description = "Retorna la definicion de una palabra con respecto al diccionario de la RAE")
    public ResponseEntity<List<DRAEObject>> searchWordDRAEAPI(@RequestParam() String wordToSearch) {

        List<DRAEObject> wordsResponse = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordToSearch);

        return new ResponseEntity<>(wordsResponse, HttpStatus.OK);
    }


    @GetMapping("/getAllWords")
    @Operation(summary = "Get All Words", description = "Retorna las distintas palabras almacenadas en la ontologia")
    public ResponseEntity<List<Word>> getAllWords() {

        List<Word> wordList = wordService.getAllWords();

        return new ResponseEntity<>(wordList, HttpStatus.OK);
    }


    @GetMapping("/getAllWordsByClassName")
    @Operation(summary = "Get All Words By Father Class Name", description = "Retorna una lista con todas las individuales de la clase indicada")
    public ResponseEntity<List<Word>> getAllIndividualsByClasses(@RequestParam() String fatherClassName) {

        return new ResponseEntity<>(wordService.getAllWordsByFatherClassName(fatherClassName), HttpStatus.OK);
    }


    @GetMapping("/search")
    @Operation(summary = "Get All Individuals Properties By Name", description = "Buscara las distintas palabras dominicanas de cualquier oracion que se digite")
    public ResponseEntity<List<Word>> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        return new ResponseEntity<>(ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList), HttpStatus.OK);
    }


    @GetMapping("/getIndividual")
    @Operation(summary = "Get A Individual By Name", description = "Retornara el individual del lema indicado")
    public ResponseEntity<Word> findIndividualByName(@RequestParam() String individualName) {

        return new ResponseEntity<>(wordService.getWordByLemma(individualName), HttpStatus.OK);
    }


    @PostMapping("/createClass")
    @Operation(summary = "Create Class", description = "Creacion de clases padre y clase hijo")
    public ResponseEntity<String> createClasses(@RequestParam() String fatherClassName, @RequestParam() String subClassName) {

        ontologyService.saveFatherClassAndSubClass(fatherClassName, subClassName);

        return new ResponseEntity<>("classes Saved", HttpStatus.OK) ;
    }


    @PostMapping("/createIndividual")
    @Operation(summary = "Create Individual", description = "Creacion de individual")
    public ResponseEntity<String> createIndividual(@RequestBody Word word) {

        ontologyService.saveIndividual(word.getLema(), word.getLema(), word.getClasePadre(), word.getDefinicion(), word.getEjemplo(), word.getLemaRAE(), word.getSinonimos());

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
    public ResponseEntity<String> editIndividual(@RequestParam() String originalIndividualName, @RequestParam(defaultValue = "") String individualName, @RequestParam(defaultValue = "") String individualNameRAE, @RequestParam(defaultValue = "") String fatherClassName, @RequestParam(defaultValue = "") String definition, @RequestParam(defaultValue = "") String example, @RequestParam(defaultValue = "") String synonyms) {

        Word wordToEdit = wordService.getWordByLemma(originalIndividualName);

        if (individualName.length() != 0)
            wordToEdit.setLema(individualName);

        if (definition.length() !=0)
            wordToEdit.setDefinicion(definition);

        if (example.length() != 0)
            wordToEdit.setEjemplo(example);

        if (fatherClassName.length() != 0)
            wordToEdit.setClasePadre(fatherClassName);

        if (individualNameRAE.length() != 0)
            wordToEdit.setLemaRAE(individualNameRAE);

        if (synonyms.length() != 0)
            wordToEdit.setSinonimos(synonyms);

        ontologyService.saveIndividual(originalIndividualName, wordToEdit.getLema(), wordToEdit.getClasePadre(), wordToEdit.getDefinicion(), wordToEdit.getEjemplo(), wordToEdit.getLemaRAE(), wordToEdit.getSinonimos());

        return new ResponseEntity<>("individual Saved", HttpStatus.OK);
    }


    @GetMapping("/getAllIndividuals")
    @Operation(summary = "Get All Individuals", description = "Retorna una lista con todas las individuales")
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
    @Operation(summary = "Get All Classes", description = "Retorna una lista con todas las clases")
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


    @GetMapping("/ontology")
    @Operation(summary = "Get Ontology Data", description = "Retorna datos de la ontologia")
    public ResponseEntity<String> getOntology() {

        return new ResponseEntity<>(ontologyConnectionService.ontologyURI, HttpStatus.OK);
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
