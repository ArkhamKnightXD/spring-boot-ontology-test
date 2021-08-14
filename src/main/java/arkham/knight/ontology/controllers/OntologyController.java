package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.*;
import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RequestMapping("/dashboard")
@Controller
public class OntologyController {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    private final OntologyService ontologyService;

    private final WordService wordService;

    private final UserService userService;

    private final SimpleWordService simpleWordService;

    private final SurveyWordService surveyWordService;

    public OntologyController(OntologyService ontologyService, WordService wordService, UserService userService, SimpleWordService simpleWordService, SurveyWordService surveyWordService) {
        this.ontologyService = ontologyService;
        this.wordService = wordService;
        this.userService = userService;
        this.simpleWordService = simpleWordService;
        this.surveyWordService = surveyWordService;
    }


    @GetMapping("/")
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "apota") String sentence, @RequestParam(defaultValue = "tweet") String searchType) {

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, searchType);

        List<Word> wordList = ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);

        model.addAttribute("words", wordService.evaluateWordsAndReturnCleanWordList(wordList));

        return "/freemarker/ontology/summary";
    }


    @GetMapping("/individuals")
    public String showAllIndividuals(Model model, @RequestParam(defaultValue = "") String sentence, Principal principal) {

        List<Individual> individualList;

        if (sentence.length() != 0){

            List<String> sentenceByWords = ontologyService.tokenizeTheSentence(sentence);
            individualList = ontologyService.getAllIndividualByName(sentenceByWords, "word-search");
        }

        else{

            individualList = ontologyConnectionService.getAllIndividuals();
        }

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("individuals", individualList);

        return "/freemarker/ontology/individuals";
    }


    @GetMapping("/individuals/vote")
    public String showAllVotedIndividuals(Model model, Principal principal) {

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("words", wordService.getAllVotedWords());

        return "/freemarker/ontology/votesIndividuals";
    }


    @GetMapping("individuals/statistics-top")
    public String statisticsPage(Model model, Principal principal){

        List<SurveyWord> topFiveMostVotedSurveys = surveyWordService.getTopFiveMostVotedSurveys();

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("words", topFiveMostVotedSurveys);
        model.addAttribute("votes", surveyWordService.getTopFiveVotesQuantity());

        return "/freemarker/statistics/stats";
    }


    @GetMapping("individuals/statistics-total")
    public String statisticsTotalPage(Model model, Principal principal){

        int acceptedWordsTotal = ontologyConnectionService.getAllIndividuals().size();
        int surveyWordsQuantity = surveyWordService.getAllSurveys().size();
        int simpleWordsQuantity = simpleWordService.getAllSimpleWords().size();

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("initialVotesQuantity", simpleWordsQuantity);
        model.addAttribute("finalVotesQuantity", surveyWordsQuantity);
        model.addAttribute("acceptedWordsQuantity", acceptedWordsTotal);

        return "/freemarker/statistics/total";
    }


    @GetMapping("individuals/statistics-total-users")
    public String statisticsTotalUsersPage(Model model, Principal principal){

        int normalUsersQuantity = userService.getUsersQuantityByRole("ROLE_USER");
        int adminUsersQuantity = userService.getUsersQuantityByRole("ROLE_ADMIN");
        int totalUsersQuantity = userService.getAllUsers().size();

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("normalUsers",  normalUsersQuantity);
        model.addAttribute("adminUsers", adminUsersQuantity);
        model.addAttribute("totalUsers", totalUsersQuantity);

        return "/freemarker/statistics/totalUsers";
    }


    @GetMapping("individuals/statistics-percentage")
    public String statisticsExtraPage(Model model, Principal principal){

        List<Word> topFivePercentageAgreementWords = wordService.getTopFivePercentageAgreementWords();

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("words", topFivePercentageAgreementWords);
        model.addAttribute("percentages", wordService.getTopFivePercentageAgreement());

        return "/freemarker/statistics/extra";
    }


    @GetMapping("/creation")
    public String creationPage(Model model) {

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());

        return "/freemarker/ontology/createIndividual";
    }


    @PostMapping("/create")
    public String create(@RequestParam String individualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam String fatherClassName, @RequestParam String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam(defaultValue = "N/A") String synonyms) {

        Word wordToSave = new Word(individualName, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        ontologyService.saveIndividual(individualName, wordToSave);

        return "redirect:/dashboard/individuals";
    }


    @GetMapping("/edition")
    public String getIndividualByName(Model model, @RequestParam String individualName)  {

        Word actualWord = wordService.getWordByLemma(individualName);

        model.addAttribute("classes", ontologyService.getAllClassesLocalName());
        model.addAttribute("fatherClass", actualWord.getClasePadre());
        model.addAttribute("lema", actualWord.getLema());
        model.addAttribute("definicion", actualWord.getDefinicion());
        model.addAttribute("ejemplo", actualWord.getEjemplo());
        model.addAttribute("lemmaRAE", actualWord.getLemaRAE());
        model.addAttribute("sinonimos", actualWord.getSinonimos());

        return "/freemarker/ontology/editIndividual";
    }


    @PostMapping("/edit")
    public String edit(@RequestParam String originalIndividualName, @RequestParam(defaultValue = "N/A") String individualNameRAE, @RequestParam String individualName, @RequestParam String definition, @RequestParam(defaultValue = "N/A") String example, @RequestParam String fatherClassName, @RequestParam(defaultValue = "") String synonyms) {

        Word wordDataToSave = new Word(individualName, definition, example, fatherClassName, synonyms, individualNameRAE, "0", "0");

        Word wordToEdit = wordService.getWordByLemma(originalIndividualName);

        Word filteredWord = wordService.editionWordFilter(wordToEdit, wordDataToSave);

        ontologyService.saveIndividual(originalIndividualName, filteredWord);

        return "redirect:/dashboard/individuals";
    }


    @GetMapping("/show")
    public String showIndividual(Model model, @RequestParam String lemma) {

        Word wordToShow = wordService.getWordByLemma(lemma);

        model.addAttribute("word", wordToShow);

        return "/freemarker/ontology/show";
    }


    @GetMapping("/delete")
    public String deleteIndividual(@RequestParam String individualName) {

        ontologyService.deleteIndividual(individualName);

        return "redirect:/dashboard/individuals";
    }


    @GetMapping("/class-creation")
    public String creationClassPage() {

        return "/freemarker/ontology/createClass";
    }


    @PostMapping("/class-create")
    public String createClass(@RequestParam String fatherClassName, @RequestParam(defaultValue = "") String subClass) {

        if (subClass.length() == 0) {

            Word defaultTestWord = new Word("prueba","definition","example", fatherClassName,"individualNameRae", "synonims", "0", "0");

            ontologyService.saveIndividual(defaultTestWord.getLema(), defaultTestWord);
        }

        else
            ontologyService.saveFatherClassAndSubClass(fatherClassName, subClass);

        return "redirect:/dashboard/individuals";
    }
}
