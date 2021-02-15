package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.jena.ontology.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class WordRestController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private WordService wordService;

    @Autowired
    private DRAEConnectionService draeConnectionService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/search")
    @Operation(summary = "Get All Individuals Properties By Name", description = "Buscara las distintas palabras dominicanas de cualquier oracion que se digite")
    public ResponseEntity<List<Word>> findAllIndividualPropertiesByName(@RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet-search") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        return new ResponseEntity<>(ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList), HttpStatus.OK);
    }


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


    @GetMapping("/getWord")
    @Operation(summary = "Get A Word By Name", description = "Retornara el individual del lema indicado")
    public ResponseEntity<Word> findIndividualByName(@RequestParam() String individualName) {

        return new ResponseEntity<>(wordService.getWordByLemma(individualName), HttpStatus.OK);
    }


    @GetMapping("/getAllWordsByClassName")
    @Operation(summary = "Get All Words By Father Class Name", description = "Retorna una lista con todas las individuales de la clase indicada")
    public ResponseEntity<List<Word>> getAllIndividualsByClasses(@RequestParam() String fatherClassName) {

        return new ResponseEntity<>(wordService.getAllWordsByFatherClassName(fatherClassName), HttpStatus.OK);
    }
}
