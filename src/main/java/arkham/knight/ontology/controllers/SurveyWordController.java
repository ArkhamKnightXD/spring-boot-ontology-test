package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordService;
import arkham.knight.ontology.services.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/surveys")
@Controller
public class SurveyWordController {

    private final OntologyService ontologyService;

    private final WordService wordService;

    private final SurveyWordService surveyWordService;

    private final SimpleWordService simpleWordService;

    public SurveyWordController(OntologyService ontologyService, WordService wordService, SurveyWordService surveyWordService, SimpleWordService simpleWordService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
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

        String actualIpAddress = request.getLocalAddr();

        SurveyWord surveyWordToVote = surveyWordService.getSurveyWordById(id);

        List<String> ipAddresses = surveyWordToVote.getIpAddresses();

//        boolean alreadyVoteWord = surveyWordService.alreadyVoteSurveyWordWithTheSameLemma(surveyWordToVote.getLemma());

        if (ipAddresses.contains(actualIpAddress) /*|| alreadyVoteWord*/) {

            System.out.println("You can only vote once!");
        }

        else {

            surveyWordService.voteSurveyWord(actualIpAddress, surveyWordToVote);
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

        String actualIpAddress = request.getLocalAddr();

        SimpleWord simpleWordToEdit = simpleWordService.getSimpleWordById(id);

        List<String> ipAddresses = simpleWordToEdit.getIpAddresses();

        //si ya hay una palabra con el mismo lemma votada no se podra votar por esta palabra que tiene el mismo lema
       // boolean alreadyVoteWord = simpleWordService.alreadyVoteSimpleWordWithTheSameLemma(simpleWordToEdit.getWord());

        //mejorar el if para la votacion, es posible que este sea el problema desactivando la otra comparacion, pues falla
        if (ipAddresses.contains(actualIpAddress) /*|| alreadyVoteWord*/) {

            System.out.println("You can only vote once!");
        }

        else {

           simpleWordService.voteSimpleWord(actualIpAddress, simpleWordToEdit);
        }

        return "redirect:/surveys/simple/";
    }
}
