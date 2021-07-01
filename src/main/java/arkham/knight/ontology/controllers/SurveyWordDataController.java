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
        model.addAttribute("words", surveyWordDataService.getAllSurveys());

        return "/freemarker/survey/createWordSurveyData";
    }


    @RequestMapping(value = "/survey-create", method = RequestMethod.POST)
    public String createSurvey(@RequestParam String individualName, @RequestParam String definition, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms) {

        SurveyWordData surveyWordDataToSave = new SurveyWordData(individualName,definition,"",fatherClassName,synonyms,individualNameRAE,definitionRAE);

        surveyWordDataService.saveSurveyWord(surveyWordDataToSave);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "/survey-edition", method = RequestMethod.GET)
    public String editionSurveyPage(Model model, @RequestParam long id) {

        SurveyWordData surveyWordToEdit = surveyWordDataService.getSurveyWordById(id);

        model.addAttribute("word", surveyWordToEdit);
        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/survey/editWordSurveyData";
    }


    @RequestMapping(value = "/survey-edit", method = RequestMethod.POST)
    public String editSurvey(@RequestParam Long id, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "n/a") String synonyms) {

        SurveyWordData surveyWordToEdit = surveyWordDataService.getSurveyWordById(id);

        surveyWordToEdit.setLemmaRAE(individualNameRAE);
        surveyWordToEdit.setDefinitionRAE(definitionRAE);
        surveyWordToEdit.setFatherClass(fatherClassName);
        surveyWordToEdit.setSynonyms(synonyms);

        surveyWordDataService.saveSurveyWord(surveyWordToEdit);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "/survey-vote", method = RequestMethod.GET)
    public String voteSurvey(@RequestParam Long id) {

        SurveyWordData surveyWordToVote = surveyWordDataService.getSurveyWordById(id);

        surveyWordToVote.setVotesQuantity(surveyWordToVote.getVotesQuantity() + 1);

        surveyWordDataService.saveSurveyWord(surveyWordToVote);

        SurveyWordData surveyWordWinner = surveyWordDataService.determineSurveyWordWinner(surveyWordToVote.getLemma());

        Word wordWinner = wordService.convertWordSurveyDataToWord(surveyWordWinner);

//        boolean wordExist = surveyWordDataService.surveyWordAlreadyExist(wordWinner.getLemma());

        int votesQuantity = Integer.parseInt(wordWinner.getCantidadVotacionesI());

        float percentageAgreement = wordService.calculateWordPercentageAgreement(wordWinner);

        if (percentageAgreement > 40 && votesQuantity > 2)
            ontologyService.saveIndividual(wordWinner.getLema(), wordWinner);

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
    public String voteSimpleSurvey(@RequestParam Long id) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        simpleWordToEdit.setVotesQuantity(simpleWordToEdit.getVotesQuantity() + 1);

        simpleWordService.saveSimpleWord(simpleWordToEdit);

        SimpleWord simpleWordWinner = simpleWordService.determineSimpleWordWinner(simpleWordToEdit.getWord());

        SurveyWordData surveyWordWinner = simpleWordService.convertSimpleWordToSurveyWord(simpleWordWinner);

        boolean wordExist = surveyWordDataService.surveyWordAlreadyExist(surveyWordWinner.getLemma());

        float percentageAgreement = wordService.calculateSurveyWordPercentageAgreement(surveyWordWinner);

        if (!wordExist && percentageAgreement > 40 && surveyWordWinner.getVotesQuantity() > 2){

            surveyWordWinner.setVotesQuantity(0);
            surveyWordWinner.setTotalAnswers(0);

            surveyWordDataService.saveSurveyWord(surveyWordWinner);
        }

        return "redirect:/surveys/simple/";
    }
}
