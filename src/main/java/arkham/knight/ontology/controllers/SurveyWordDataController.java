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

        return "/freemarker/surveyIndex";
    }


    @RequestMapping(value = "/survey-creation", method = RequestMethod.GET)
    public String creationSurveyPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("words", wordService.getAllWords());

        return "/freemarker/createWordSurveyData";
    }


    @RequestMapping(value = "/survey-create", method = RequestMethod.POST)
    public String createSurvey(@RequestParam String individualName, @RequestParam String definition, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms ) {

        SurveyWordData surveyWordDataToSave = new SurveyWordData(individualName,definition,"",fatherClassName,synonyms,individualNameRAE,definitionRAE);

        surveyWordDataService.saveSurvey(surveyWordDataToSave);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "/survey-complete-creation", method = RequestMethod.GET)
    public String completeCreationSurveyPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/createCompleteWordSurveyData";
    }


    @RequestMapping(value = "/survey-complete-create", method = RequestMethod.POST)
    public String createCompleteSurvey(@RequestParam String individualName, @RequestParam String definition, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms ) {

        SurveyWordData surveyWordDataToSave = new SurveyWordData(individualName,definition,"",fatherClassName,synonyms,individualNameRAE,definitionRAE);

        surveyWordDataService.saveSurvey(surveyWordDataToSave);

        return "redirect:/surveys/";
    }
}
