package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.*;
import arkham.knight.ontology.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.security.Principal;
import java.util.List;

@RequestMapping("/surveys")
@Controller
public class SurveyWordController {

    private final SurveyWordService surveyWordService;

    private final SimpleWordService simpleWordService;

    private final UserService userService;

    private final RaeConnectionService raeConnectionService;

    private final JsoupService jsoupService;

    private final RestTemplate restTemplate;

    public SurveyWordController(SurveyWordService surveyWordService, SimpleWordService simpleWordService, UserService userService, RaeConnectionService raeConnectionService, JsoupService jsoupService, RestTemplate restTemplate) {
        this.surveyWordService = surveyWordService;
        this.simpleWordService = simpleWordService;
        this.userService = userService;
        this.raeConnectionService = raeConnectionService;
        this.jsoupService = jsoupService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/")
    public String indexPage(Model model, Principal principal) {

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("surveys", surveyWordService.getAllSurveys());

        return "/freemarker/survey/surveyIndex";
    }


    @GetMapping("/survey-edition")
    public String editionSurveyCompletePage(Model model, @RequestParam long id, @RequestParam(defaultValue = "prueba") String sentence) {

        SurveyWord surveyWordToEdit = surveyWordService.getSurveyWordById(id);

        List<BaseResponse> wordList = raeConnectionService.getTheLemmaListFromTheRaeAPI(restTemplate, sentence.toLowerCase());

        String definitionResponse = "";

        String raeWord;

        try {

            definitionResponse = raeConnectionService.getTheDefinitionListByWordId(restTemplate, wordList.get(0).getRes().get(0).getId());

            raeWord = wordList.get(0).getRes().get(0).getHeader();
        }catch (Exception exception){

            raeWord = "Palabra no encontrada";
        }

        List<DefinitionResponse> cleanDefinitions = jsoupService.getCompleteDefinitionData(definitionResponse);


        model.addAttribute("raeWord", raeWord);
        model.addAttribute("definitions", cleanDefinitions);
        model.addAttribute("word", surveyWordToEdit);

        return "/freemarker/survey/completeSurveyCreation";
    }


    @PostMapping("/survey-edit")
    public String editSurvey(@RequestParam long id, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName) {

        SurveyWord surveyWordToEdit = surveyWordService.getSurveyWordById(id);

        surveyWordToEdit.setLemmaRAE(individualNameRAE);
        surveyWordToEdit.setDefinitionRAE(definitionRAE);

        String ontClass = surveyWordService.getEquivalentClassOfTheRaeClass(fatherClassName);

        surveyWordToEdit.setFatherClass(ontClass);

        surveyWordService.saveSurveyWord(surveyWordToEdit);

        return "redirect:/surveys/";
    }


    @GetMapping("/survey-vote")
    public String voteSurvey(@RequestParam long id, Principal principal) {

        String actualUserName = principal.getName();

        SurveyWord surveyWordToVote = surveyWordService.getSurveyWordById(id);

        boolean isWordAlreadyVote = surveyWordService.alreadyVoteWordWithTheSameLemmaExist(surveyWordToVote, actualUserName);

        if (surveyWordToVote.getAlreadyVoteUsernames().contains(actualUserName) || isWordAlreadyVote) {

            System.out.println("no puede votar mas de 2 veces");
        }

        else {

            surveyWordToVote.setUserAlreadyVote(false);
            surveyWordToVote.setAlreadyVoteUsernames(actualUserName);
            surveyWordToVote.setVotesQuantity(surveyWordToVote.getVotesQuantity() + 1);

            surveyWordService.saveSurveyWord(surveyWordToVote);

            surveyWordService.evaluateIfTheWordEntersTheOntology(surveyWordToVote);
        }

        return "redirect:/surveys/";
    }


    @GetMapping("simple/")
    public String indexSimplePage(Model model, Principal principal) {

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("words", simpleWordService.getAllSimpleWords());

        return "/freemarker/survey/simpleSurveyIndex";
    }


    @GetMapping("/simple-survey-creation")
    public String creationSimpleSurveyPage() {

        return "/freemarker/survey/createWordSurveyProposition";
    }


    @PostMapping("/simple-survey-create")
    public String createSimpleSurvey(@RequestParam String word, @RequestParam(defaultValue = "N/A") String definition) {

        SimpleWord simpleWordToCreate = new SimpleWord(word, definition, false);

        simpleWordService.saveSimpleWord(simpleWordToCreate);

        return "redirect:/surveys/simple-survey-creation";
    }


    @GetMapping("/simple-survey-edition")
    public String editionSimpleSurveyPage(Model model, @RequestParam long id) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        model.addAttribute("word", simpleWordToEdit);

        return "/freemarker/survey/editWordSurveyProposition";
    }


    @PostMapping("/simple-survey-edit")
    public String editSimpleSurvey(@RequestParam long id, @RequestParam String definition) {

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        simpleWordToEdit.setWordDefinition(definition);

        simpleWordService.saveSimpleWord(simpleWordToEdit);

        return "redirect:/surveys/simple/";
    }


    @GetMapping("/simple-survey-vote")
    public String voteSimpleSurvey(@RequestParam long id, Principal principal) {

        SimpleWord simpleWordToVote = simpleWordService.getSimpleWordById(id);

        String actualUserName = principal.getName();
        //Si el usuario ya voto por una palabra con el mismo lema, este mismo usuario no podra votar por las otras palabras que tengan el mismo lema
        boolean isWordAlreadyVote = simpleWordService.alreadyVoteWordWithTheSameLemmaExist(simpleWordToVote, actualUserName);

        if (simpleWordToVote.getAlreadyVoteUsernames().contains(actualUserName) || isWordAlreadyVote) {

            System.out.println("no puede votar mas de 2 veces");
        }

        else {

            simpleWordToVote.setUserAlreadyVote(false);
            simpleWordToVote.setAlreadyVoteUsernames(actualUserName);
            simpleWordToVote.setVotesQuantity(simpleWordToVote.getVotesQuantity() + 1);

            simpleWordService.saveSimpleWord(simpleWordToVote);

            simpleWordService.evaluateIfTheWordEntersTheSurvey(simpleWordToVote);
        }

        return "redirect:/surveys/simple/";
    }
}
