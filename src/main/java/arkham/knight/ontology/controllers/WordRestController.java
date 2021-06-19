package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.jena.ontology.Individual;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class WordRestController {

    private final OntologyService ontologyService;

    private final WordService wordService;

    private final DRAEConnectionService draeConnectionService;

    private final RestTemplate restTemplate;

    public WordRestController(OntologyService ontologyService, WordService wordService, DRAEConnectionService draeConnectionService, RestTemplate restTemplate) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
        this.draeConnectionService = draeConnectionService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/search")
    @Operation(summary = "Get All Individuals Properties By Name", description = "Buscara las distintas palabras dominicanas de cualquier oracion que se digite")
    public ResponseEntity<List<Word>> getAllIndividualPropertiesByName(@RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        List<Word> wordList = ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);

        return new ResponseEntity<>(wordService.evaluateWordsAndReturnCleanWordList(wordList), HttpStatus.OK);
    }


    @GetMapping("/search-rae/{wordToSearch}")
    @Operation(summary = "Search For Any Word In The RAE Endpoint", description = "Retorna la definición de una palabra con respecto al diccionario de la RAE")
    public ResponseEntity<List<DRAEObject>> getTheWordDataFromDRAE(@PathVariable String wordToSearch) {

        List<DRAEObject> wordsResponse = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordToSearch);

        return new ResponseEntity<>(wordsResponse, HttpStatus.OK);
    }


    @GetMapping("/words")
    @Operation(summary = "Get All Words", description = "Retorna las distintas palabras almacenadas en la ontologia")
    public ResponseEntity<List<Word>> getAllWords() {

        return new ResponseEntity<>(wordService.getAllWords(), HttpStatus.OK);
    }


    @GetMapping("/words/{lemma}")
    @Operation(summary = "Get A Word By Lemma", description = "Retornara el individual del lema indicado")
    public ResponseEntity<Word> getWordByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(wordService.getWordByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/words/father/{fatherClassName}")
    @Operation(summary = "Get All Words By Father Class Name", description = "Retorna una lista con todas las individuales de la clase indicada")
    public ResponseEntity<List<Word>> getAllWordsByFatherClassName(@PathVariable String fatherClassName) {

        return new ResponseEntity<>(wordService.getAllWordsByFatherClassName(fatherClassName), HttpStatus.OK);
    }
}
