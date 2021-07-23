package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.BaseResponse;
import arkham.knight.ontology.models.DefinitionResponse;
import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RequestMapping("/surveys")
@Controller
public class SurveyWordController {

    private final SurveyWordService surveyWordService;

    private final SimpleWordService simpleWordService;

    private final RaeConnectionService raeConnectionService;

    private final JsoupService jsoupService;

    private final RestTemplate restTemplate;

    public SurveyWordController(SurveyWordService surveyWordService, SimpleWordService simpleWordService, RaeConnectionService raeConnectionService, JsoupService jsoupService, RestTemplate restTemplate) {
        this.surveyWordService = surveyWordService;
        this.simpleWordService = simpleWordService;
        this.raeConnectionService = raeConnectionService;
        this.jsoupService = jsoupService;
        this.restTemplate = restTemplate;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model model, Principal principal) {

        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("surveys", surveyWordService.getAllSurveys());

        return "/freemarker/survey/surveyIndex";
    }


    @RequestMapping(value = "/survey-edition", method = RequestMethod.GET)
    public String editionSurveyCompletePage(Model model, @RequestParam long id, @RequestParam(defaultValue = "prueba") String sentence) {

        SurveyWord surveyWordToEdit = surveyWordService.getSurveyWordById(id);

        List<BaseResponse> wordList = raeConnectionService.getTheLemmaListFromTheRaeAPI(restTemplate, sentence.toLowerCase());

        String definitionResponse = "";

        try {

            definitionResponse = raeConnectionService.getTheDefinitionListByWordId(restTemplate, wordList.get(0).getRes().get(0).getId());
        }catch (Exception exception){

            System.out.println("Palabra no encontrada");
        }

        List<String> initialList = jsoupService.getAllInitialData(definitionResponse);
        List<String> classes = jsoupService.getAllClasses(definitionResponse);
        List<String> definitions = jsoupService.getAllDefinitions(definitionResponse);

        List<DefinitionResponse> cleanDefinitions = jsoupService.getCompleteDefinitionData(initialList, classes, definitions);

        //todo evaluar si seguire enviando varios lemas o lo ideal seria mandar uno, ya que en la pagina solo tomo en cuenta el primer lemma a la hora de buscar las definiciones
        model.addAttribute("raeWords", wordList.get(0).getRes());
        model.addAttribute("definitions", cleanDefinitions);
        model.addAttribute("word", surveyWordToEdit);

        return "/freemarker/survey/completeSurveyCreation";
    }


    @RequestMapping(value = "/survey-edit", method = RequestMethod.POST)
    public String editSurvey(@RequestParam long id, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam(defaultValue = "N/A") String fatherClassName, @RequestParam(defaultValue = "N/A") String synonyms) {

        SurveyWord surveyWordToEdit = surveyWordService.getSurveyWordById(id);

        surveyWordToEdit.setLemmaRAE(individualNameRAE);
        surveyWordToEdit.setDefinitionRAE(definitionRAE);
        surveyWordToEdit.setFatherClass(fatherClassName);
        surveyWordToEdit.setSynonyms(synonyms);

        surveyWordService.saveSurveyWord(surveyWordToEdit);

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "/survey-vote", method = RequestMethod.GET)
    public String voteSurvey(@RequestParam long id, HttpServletRequest request) {

        String actualIpAddress = request.getRemoteAddr();

        SurveyWord surveyWordToVote = surveyWordService.getSurveyWordById(id);

        boolean alreadyVoteWord = surveyWordService.alreadyVoteSurveyWordWithTheSameLemmaAndDifferentDefinition(surveyWordToVote, actualIpAddress);

//        if (surveyWordToVote.getIpAddresses().contains(actualIpAddress) || alreadyVoteWord) {
//
//            surveyWordToVote.setUserAlreadyVote(true);
//
//            surveyWordService.saveSurveyWord(surveyWordToVote);
//        }

//        else {

            surveyWordToVote.setUserAlreadyVote(false);
            surveyWordToVote.setIpAddresses(actualIpAddress);
            surveyWordToVote.setVotesQuantity(surveyWordToVote.getVotesQuantity() + 1);

            surveyWordService.saveSurveyWord(surveyWordToVote);

            surveyWordService.evaluateIfTheWordEntersTheOntology(surveyWordToVote);
//        }

        return "redirect:/surveys/";
    }


    @RequestMapping(value = "simple/", method = RequestMethod.GET)
    public String indexSimplePage(Model model, Principal principal) {

        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("words", simpleWordService.getAllSimpleWord());

        return "/freemarker/survey/simpleSurveyIndex";
    }


    @RequestMapping(value = "/simple-survey-creation", method = RequestMethod.GET)
    public String creationSimpleSurveyPage() {

        return "/freemarker/survey/createWordSurveyProposition";
    }


    @RequestMapping(value = "/simple-survey-create", method = RequestMethod.POST)
    public String createSimpleSurvey(@RequestParam String word, @RequestParam(defaultValue = "N/A") String definition) {

        SimpleWord simpleWordToCreate = new SimpleWord(word, definition, false);

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
    public String editSimpleSurvey(@RequestParam long id, @RequestParam String definition) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        simpleWordToEdit.setWordDefinition(definition);

        simpleWordService.saveSimpleWord(simpleWordToEdit);

        return "redirect:/surveys/simple/";
    }


    @RequestMapping(value = "/simple-survey-vote", method = RequestMethod.GET)
    public String voteSimpleSurvey(@RequestParam long id, HttpServletRequest request) {

        String actualIpAddress = request.getRemoteAddr();

        SimpleWord simpleWordToVote = simpleWordService.getSimpleWordById(id);

        //Si el usuario ya voto por una palabra con el mismo lema, este mismo usuario no podra votar por las otras palabras que tengan el mismo lema
        boolean alreadyVoteWord = simpleWordService.alreadyVoteSimpleWordWithTheSameLemmaAndDifferentDefinition(simpleWordToVote, actualIpAddress);

//        if (simpleWordToVote.getIpAddresses().contains(actualIpAddress) || alreadyVoteWord) {
//
//            System.out.println("You can only vote once!");
//
//            simpleWordToVote.setUserAlreadyVote(true);
//
//            simpleWordService.saveSimpleWord(simpleWordToVote);
//        }

//        else {

            simpleWordToVote.setUserAlreadyVote(false);
            simpleWordToVote.setIpAddresses(actualIpAddress);
            simpleWordToVote.setVotesQuantity(simpleWordToVote.getVotesQuantity() + 1);

            simpleWordService.saveSimpleWord(simpleWordToVote);

            simpleWordService.evaluateIfTheWordEntersTheSurvey(simpleWordToVote);
//        }

        return "redirect:/surveys/simple/";
    }
}
