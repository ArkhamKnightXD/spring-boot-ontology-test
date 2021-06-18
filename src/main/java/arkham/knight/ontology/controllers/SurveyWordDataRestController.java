package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class SurveyWordDataRestController {

    private final SurveyWordDataService surveyWordDataService;

    private final SimpleWordService simpleWordService;

    public SurveyWordDataRestController(SurveyWordDataService surveyWordDataService, SimpleWordService simpleWordService) {
        this.surveyWordDataService = surveyWordDataService;
        this.simpleWordService = simpleWordService;
    }


    @GetMapping("/surveys")
    @Operation(summary = "Get All Survey Words", description = "Retorna una lista con todas las encuestas realizadas ")
    public ResponseEntity<List<SurveyWordData>> getAllSurveyWordData() {

        return new ResponseEntity<>(surveyWordDataService.getAllSurveys(), HttpStatus.OK);
    }


    @GetMapping("/surveys/{lemma}")
    @Operation(summary = "Get All Survey Words By Lemma", description = "Retorna una lista con todas las encuestas realizadas del lema indicado")
    public ResponseEntity<List<SurveyWordData>> getAllSurveyWordDataByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(surveyWordDataService.getAllSurveysByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/surveys/{lemmaRAE}")
    @Operation(summary = "Get All Survey Words By Lemma Rae", description = "Retorna una lista con todas las encuestas realizadas del lema de la rae indicado")
    public ResponseEntity<List<SurveyWordData>> getAllSurveyWordDataByLemmaRAE(@PathVariable String lemmaRAE) {

        return new ResponseEntity<>(surveyWordDataService.getAllSurveysByLemmaRAE(lemmaRAE), HttpStatus.OK);
    }


    @GetMapping("/simple-words")
    @Operation(summary = "Get All Simple Word Data", description = "Retorna los lemas de la encuestas simples")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordData() {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWord(), HttpStatus.OK);
    }


    @GetMapping("/simple-words/{lemma}")
    @Operation(summary = "Get All Simple Word Data By Word", description = "Retorna los lemas de la encuestas simples")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordDataByWord(@PathVariable String lemma) {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWordByLemma(lemma), HttpStatus.OK);
    }
}
