package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/surveys")
@Controller
public class SurveyWordController {

    private final OntologyService ontologyService;

    private final SurveyWordService surveyWordService;

    private final SimpleWordService simpleWordService;

    public SurveyWordController(OntologyService ontologyService,SurveyWordService surveyWordService, SimpleWordService simpleWordService) {
        this.ontologyService = ontologyService;
        this.surveyWordService = surveyWordService;
        this.simpleWordService = simpleWordService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model model) {

        model.addAttribute("surveys", surveyWordService.getAllSurveys());

        return "/freemarker/survey/surveyIndex";
    }


    @RequestMapping(value = "/survey-edition", method = RequestMethod.GET)
    public String editionSurveyPage(Model model, @RequestParam long id) {

        SurveyWord surveyWordToEdit = surveyWordService.getSurveyWordById(id);

        model.addAttribute("word", surveyWordToEdit);
        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/survey/editWordSurveyData";
    }


    @RequestMapping(value = "/survey-edit", method = RequestMethod.POST)
    public String editSurvey(@RequestParam long id, @RequestParam String individualNameRAE, @RequestParam String definitionRAE, @RequestParam String fatherClassName, @RequestParam(defaultValue = "n/a") String synonyms) {

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

//        boolean alreadyVoteWord = surveyWordService.alreadyVoteSurveyWordWithTheSameLemma(surveyWordToVote.getLemma());

        if (surveyWordToVote.getIpAddresses().contains(actualIpAddress) /*|| alreadyVoteWord*/) {

            System.out.println("You can only vote once!");
        }

        else {

            surveyWordToVote.setIpAddresses(actualIpAddress);
            surveyWordToVote.setVotesQuantity(surveyWordToVote.getVotesQuantity() + 1);

            surveyWordService.saveSurveyWord(surveyWordToVote);

            surveyWordService.evaluateIfTheWordEntersTheOntology(surveyWordToVote);
        }

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
//        boolean alreadyVoteWord = simpleWordService.alreadyVoteSimpleWordWithTheSameLemma(simpleWordToEdit.getWord());

        if (simpleWordToVote.getIpAddresses().contains(actualIpAddress) /*|| alreadyVoteWord*/) {

            System.out.println("You can only vote once!");
        }
//
        else {

            simpleWordToVote.setIpAddresses(actualIpAddress);
            simpleWordToVote.setVotesQuantity(simpleWordToVote.getVotesQuantity() + 1);

            simpleWordService.saveSimpleWord(simpleWordToVote);

            simpleWordService.evaluateIfTheWordEntersTheSurvey(simpleWordToVote);
        }

        return "redirect:/surveys/simple/";
    }
}
