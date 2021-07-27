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
    @Operation(summary = "Get All Survey Words", description = "Retorna una lista con los datos de todas las encuestas avanzadas realizadas")
    public ResponseEntity<List<SurveyWord>> getAllSurveys() {

        return new ResponseEntity<>(surveyWordService.getAllSurveys(), HttpStatus.OK);
    }


    @GetMapping("/surveys/{lemma}")
    @Operation(summary = "Get All Survey Words By Lemma", description = "Retorna una lista con los datos de todas las encuestas realizadas del lema indicado")
    public ResponseEntity<List<SurveyWord>> getAllSurveysByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(surveyWordService.getAllSurveysByLemma(lemma), HttpStatus.OK);
    }


    @GetMapping("/simple-words")
    @Operation(summary = "Get All Simple Survey Word Data", description = "Retorna una lista con los datos de las encuestas iniciales")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWord() {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWord(), HttpStatus.OK);
    }


    @PostMapping("/simple-words/{lemma}")
    @Operation(summary = "Submit Simple Survey Word To Vote", description = "Envia una palabra a votación")
    public ResponseEntity<String> saveSimpleWord(@PathVariable String lemma, @RequestParam(defaultValue = "N/A") String definition) {

        SimpleWord simpleWordToSave = new SimpleWord(lemma, definition, false);

        simpleWordService.saveSimpleWord(simpleWordToSave);

        return new ResponseEntity<>("Palabra puesta a votación", HttpStatus.OK);
    }


    @GetMapping("/simple-words/{lemma}")
    @Operation(summary = "Get All Simple Survey Word Data By Word", description = "Retorna una lista con los datos de las encuestas iniciales del lema indicado")
    public ResponseEntity<List<SimpleWord>> getAllSimpleWordByLemma(@PathVariable String lemma) {

        return new ResponseEntity<>(simpleWordService.getAllSimpleWordByLemma(lemma), HttpStatus.OK);
    }
}
