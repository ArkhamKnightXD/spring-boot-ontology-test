package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class SurveyWordRestController {

    private final SurveyWordService surveyWordService;

    private final SimpleWordService simpleWordService;

    public SurveyWordRestController(SurveyWordService surveyWordService, SimpleWordService simpleWordService) {
        this.surveyWordService = surveyWordService;
        this.simpleWordService = simpleWordService;
    }


    @GetMapping("/surveys")
    @Operation(summary = "Get All Survey Words", description = "Retorna una lista con todas las encuestas avanzadas realizadas")
    public ResponseEntity<List<SurveyWord>> getAllSurveys() {

        return new ResponseEntity<>(surveyWordService.getAllSurveys(), HttpStatus.OK);
    }


    @GetMapping("/surveys/{lemma}")
    @Operation(summary = "Get All Survey Words By Lemma", description = "Retorna una lista con todas las encuestas realizadas del lema indicado")
    public ResponseEntity<List<SurveyWord>> getAllSurveysByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(surveyWordService.getAllSurveysByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/surveys/rae/{lemmaRAE}")
    @Operation(summary = "Get All Survey Words By Lemma Rae", description = "Retorna una lista con todas las encuestas avanzadas realizadas del lema de la rae indicado")
    public ResponseEntity<List<SurveyWord>> getAllSurveysByLemmaRAE(@PathVariable String lemmaRAE) {

        return new ResponseEntity<>(surveyWordService.getAllSurveysByLemmaRAE(lemmaRAE), HttpStatus.OK);
    }


    @GetMapping("/simple-words")
    @Operation(summary = "Get All Simple Survey Word Data", description = "Retorna una lista con los datos de la encuestas iniciales")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWord() {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWord(), HttpStatus.OK);
    }


    @GetMapping("/simple-words/{lemma}")
    @Operation(summary = "Get All Simple Survey Word Data By Word", description = "Retorna una lista con los datos de la encuestas iniciales del lema indicado")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWordByLemma(lemma), HttpStatus.OK);
    }
}
