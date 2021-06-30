package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordDataService;
import arkham.knight.ontology.services.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/surveys")
@Controller
public class SurveyWordDataController {

    private final OntologyService ontologyService;

    private final WordService wordService;

    private final SurveyWordDataService surveyWordDataService;

    private final SimpleWordService simpleWordService;

    public SurveyWordDataController(OntologyService ontologyService, WordService wordService, SurveyWordDataService surveyWordDataService, SimpleWordService simpleWordService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
        this.surveyWordDataService = surveyWordDataService;
        this.simpleWordService = simpleWordService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model model) {

        model.addAttribute("surveys", surveyWordDataService.getAllSurveys());

        return "/freemarker/survey/surveyIndex";
    }


    @RequestMapping(value = "/survey-creation", method = RequestMethod.GET)
    public String creationSurveyPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("words", wordService.getAllWords());

        return "/freemarker/survey/createWordSurveyData";
    }


    @RequestMapping(value = "/survey-complete-creation", method = RequestMethod.GET)
    public String completeCreationSurveyPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/survey/createCompleteWordSurveyData";
    }


    @RequestMapping(value = "/survey-create", method = RequestMethod.POST)
    public String createSurvey(@RequestParam String individualName, @RequestParam String definition, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms) {

        SurveyWordData surveyWordDataToSave = new SurveyWordData(individualName,definition,"",fatherClassName,synonyms,individualNameRAE,definitionRAE);

        surveyWordDataService.saveSurvey(surveyWordDataToSave);

        SurveyWordData completeSurveyWordData = surveyWordDataService.determineSurveysDataByLemmaAndReturnSurveyWord(individualName);

        Word wordToSaveInTheOntology = wordService.convertWordSurveyDataToWord(completeSurveyWordData);

        int totalAnswers = Integer.parseInt(wordToSaveInTheOntology.getTotalRespuestasN());

        if (totalAnswers > 2 && wordService.calculateWordPercentageAgreement(wordToSaveInTheOntology) > 40)
            ontologyService.saveIndividual(wordToSaveInTheOntology.getLema(), wordToSaveInTheOntology);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "simple/", method = RequestMethod.GET)
    public String indexSimplePage(Model model) {

        model.addAttribute("words", simpleWordService.getAllSimpleWord());

        return "/freemarker/survey/simpleSurveyIndex";
    }


    @RequestMapping(value = "/simple-survey-creation", method = RequestMethod.GET)
    public String creationSimpleSurveyPage() {

        return "/freemarker/survey/createWordSurveyProposition";
    }


    @RequestMapping(value = "/simple-survey-create", method = RequestMethod.POST)
    public String createSimpleSurvey(@RequestParam String word, @RequestParam(defaultValue = "N/A") String definition) {

        SimpleWord simpleWordToCreate = new SimpleWord(word, definition);

        simpleWordService.saveSimpleWord(simpleWordToCreate);

        return "redirect:/surveys/simple-survey-creation";
    }


    @RequestMapping(value = "/simple-survey-edition", method = RequestMethod.GET)
    public String editionSimpleSurveyPage(Model model, @RequestParam long id) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        model.addAttribute("word", simpleWordToEdit);

        return "/freemarker/survey/editWordSurveyProposition";
    }


    @RequestMapping(value = "/simple-survey-edit", method = RequestMethod.POST)
    public String editSimpleSurvey(@RequestParam Long id, @RequestParam(defaultValue = "N/A") String definition) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        simpleWordToEdit.setWordDefinition(definition);

        simpleWordService.saveSimpleWord(simpleWordToEdit);

        return "redirect:/surveys/simple/";
    }


    @RequestMapping(value = "/simple-survey-vote", method = RequestMethod.GET)
    public String editSimpleSurvey(@RequestParam Long id) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        simpleWordToEdit.setVotesQuantity(simpleWordToEdit.getVotesQuantity() + 1);

        simpleWordService.saveSimpleWord(simpleWordToEdit);

        SimpleWord simpleWordWinner = simpleWordService.determineSimpleWordWinner(simpleWordToEdit.getWord());

        SurveyWordData surveyWordWinner = simpleWordService.convertSimpleWordToSurveyWord(simpleWordWinner);

        boolean wordExist = surveyWordDataService.surveyWordAlreadyExist(surveyWordWinner.getLemma());

        if (!wordExist && wordService.calculateSurveyWordPercentageAgreement(surveyWordWinner) > 40)
            surveyWordDataService.saveSurvey(surveyWordWinner);

        return "redirect:/surveys/simple/";
    }
}
