package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.services.OntologyService;
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

    final OntologyService ontologyService;

    final WordService wordService;

    final SurveyWordDataService surveyWordDataService;

    public SurveyWordDataController(OntologyService ontologyService, WordService wordService, SurveyWordDataService surveyWordDataService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
        this.surveyWordDataService = surveyWordDataService;
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

        var surveyWordDataToSave = new SurveyWordData(individualName,definition,"",fatherClassName,synonyms,individualNameRAE,definitionRAE);

        surveyWordDataService.saveSurvey(surveyWordDataToSave);

        var completeSurveyWordData = surveyWordDataService.determineSurveysDataByLemmaAndReturnSurveyWord(individualName);

        var wordToSaveInTheOntology = wordService.convertWordSurveyDataToWord(completeSurveyWordData);

        var totalAnswers = Integer.parseInt(wordToSaveInTheOntology.getTotalRespuestasN());

        if (totalAnswers > 2 && wordService.calculateWordPercentageAgreement(wordToSaveInTheOntology) > 40)
            ontologyService.saveIndividual(wordToSaveInTheOntology.getLema(), wordToSaveInTheOntology);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "/simple-survey-creation", method = RequestMethod.GET)
    public String creationSimpleSurveyPage() {

        return "/freemarker/survey/createWordSurveyProposition";
    }


    @RequestMapping(value = "/simple-survey-create", method = RequestMethod.POST)
    public String createSimpleSurvey(@RequestParam String individualName, @RequestParam String definition) {

        var surveyWordDataToSave = new SurveyWordData(individualName, definition);

        surveyWordDataService.saveSurvey(surveyWordDataToSave);

        return "redirect:/surveys/";
    }
}
