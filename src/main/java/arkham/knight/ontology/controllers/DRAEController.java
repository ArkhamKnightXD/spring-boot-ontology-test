package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.DRAEDefinition;
import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.services.DRAEConnectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/rae")
@Controller
public class DRAEController {

    private final DRAEConnectionService draeConnectionService;

    private final RestTemplate restTemplate;

    public DRAEController(DRAEConnectionService draeConnectionService, RestTemplate restTemplate) {
        this.draeConnectionService = draeConnectionService;
        this.restTemplate = restTemplate;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchPageDRAE(Model model, @RequestParam(defaultValue = "diccionario") String sentence) {

        List<DRAEObject> wordList = draeConnectionService.getTheWordDataFromDRAE(restTemplate, sentence);

        model.addAttribute("words", wordList);

        return "/freemarker/rae-search/searchDRAE";
    }


    @RequestMapping(value = "/showDRAE", method = RequestMethod.GET)
    public String showDRAEWordData(Model model, @RequestParam String lemma) {

        List<DRAEObject> words = draeConnectionService.getTheWordDataFromDRAE(restTemplate, lemma);

        List<DRAEDefinition> definitions = draeConnectionService.getAllDefinitionsFromDRAEWordList(words);

        model.addAttribute("word", lemma);
        model.addAttribute("definitions", definitions);

        return "/freemarker/rae-search/searchDRAEComplete";
    }
}
