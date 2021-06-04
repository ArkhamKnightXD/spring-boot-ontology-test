package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


    @GetMapping("/getAllSurveyWordData")
    @Operation(summary = "Get All Survey Word Data", description = "Retorna los lemas de la encuestas")
    public ResponseEntity<List<SurveyWordData>> getAllSurveyWordData() {

        return new  ResponseEntity<>(surveyWordDataService.getAllSurveys(), HttpStatus.OK);
    }


    @GetMapping("/getAllSurveyWordDataByLemma")
    @Operation(summary = "Get All Survey Word Data", description = "Retorna los lemas de la encuestas")
    public ResponseEntity<List<SurveyWordData>> getAllSurveyWordDataByLemma(@RequestParam String lemma) {

        return new  ResponseEntity<>(surveyWordDataService.getAllSurveysByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/getAllSimpleWordData")
    @Operation(summary = "Get All Simple Word Data", description = "Retorna los lemas de la encuestas simples")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordData() {

        return new  ResponseEntity<>(simpleWordService.getAllSimpleWord(), HttpStatus.OK);
    }


    @GetMapping("/getAllSimpleWordDataByLemma")
    @Operation(summary = "Get All Simple Word Data By Word", description = "Retorna los lemas de la encuestas simples")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordDataByWord(@RequestParam String lemma) {

        return new  ResponseEntity<>(simpleWordService.getAllSimpleWordByWord(lemma), HttpStatus.OK);
    }
}
