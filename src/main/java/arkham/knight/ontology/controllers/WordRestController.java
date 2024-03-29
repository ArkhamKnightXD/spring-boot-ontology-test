package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.BaseResponse;
import arkham.knight.ontology.models.DefinitionResponse;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.JsoupService;
import arkham.knight.ontology.services.RaeConnectionService;
import arkham.knight.ontology.services.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class WordRestController {

    private final WordService wordService;

    private final RestTemplate restTemplate;

    private final RaeConnectionService raeConnectionService;

    private final JsoupService jsoupService;

    public WordRestController(WordService wordService, RestTemplate restTemplate, RaeConnectionService raeConnectionService, JsoupService jsoupService) {
        this.wordService = wordService;
        this.restTemplate = restTemplate;
        this.raeConnectionService = raeConnectionService;
        this.jsoupService = jsoupService;
    }


    @GetMapping("/words")
    @Operation(summary = "Get All Words", description = "Retorna una lista con los datos de todas las palabras almacenadas en la ontologia")
    public ResponseEntity<List<Word>> getAllWords() {

        return new ResponseEntity<>(wordService.getAllWords(), HttpStatus.OK);
    }


    @GetMapping("/words/{lemma}")
    @Operation(summary = "Get A Word By Lemma", description = "Retornara los datos de la palabra correspondiente al lema indicado")
    public ResponseEntity<Word> getWordByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(wordService.getWordByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/words/father/{fatherClassName}")
    @Operation(summary = "Get All Words By Father Class Name", description = "Retorna una lista con los datos de todas las palabras que pertenezcan a la clase indicada")
    public ResponseEntity<List<Word>> getAllWordsByFatherClassName(@PathVariable String fatherClassName) {

        return new ResponseEntity<>(wordService.getAllWordsByFatherClassName(fatherClassName), HttpStatus.OK);
    }


    @GetMapping("/rae/search/{wordToSearch}")
    @Operation(summary = "Search For Any Word In The RAE Endpoint", description = " Buscara todos los lema una palabra con respecto al diccionario de la RAE")
    public ResponseEntity<List<BaseResponse>> getTheLemmaListFromTheRaeAPI(@PathVariable String wordToSearch) {

        return new ResponseEntity<>(raeConnectionService.getTheLemmaListFromTheRaeAPI(restTemplate, wordToSearch), HttpStatus.OK);
    }


    @GetMapping("/rae/search/exact-word/{wordToSearch}")
    @Operation(summary = "Search For The Exact Word In The RAE Endpoint", description = " Buscara todos los lema una palabra con respecto al diccionario de la RAE")
    public ResponseEntity<List<BaseResponse>> getTheExactLemmaFromTheRaeAPI(@PathVariable String wordToSearch) {

        return new ResponseEntity<>(raeConnectionService.getTheExactLemmaFromTheRaeAPI(restTemplate, wordToSearch), HttpStatus.OK);
    }


    @GetMapping("/rae/search/definitions/{wordToSearch}")
    @Operation(summary = "Search For All Definitions Of The Desire Word", description = "Retorna todas las definiciones relacionadas a la palabra deseada")
    public ResponseEntity<List<DefinitionResponse>> getTheDefinitionsFromTheRaeAPI(@PathVariable String wordToSearch) {

        String definitionResponse;

        List<BaseResponse> word = raeConnectionService.getTheExactLemmaFromTheRaeAPI(restTemplate, wordToSearch.toLowerCase());

        try {

            definitionResponse = raeConnectionService.getTheDefinitionListByWordId(restTemplate, word.get(0).getRes().get(0).getId());
        }catch (Exception exception){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jsoupService.getCompleteDefinitionData(definitionResponse), HttpStatus.OK);
    }
}
