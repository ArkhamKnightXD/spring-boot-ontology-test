package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.BaseResponse;
import arkham.knight.ontology.models.DRAEDefinition;
import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.JsoupConnectionService;
import arkham.knight.ontology.services.RaeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/rae")
@Controller
public class RaeController {

    private final DRAEConnectionService draeConnectionService;

    private final RaeService raeService;

    private final JsoupConnectionService jsoupConnectionService;

    private final RestTemplate restTemplate;

    public RaeController(DRAEConnectionService draeConnectionService, RaeService raeService, JsoupConnectionService jsoupConnectionService, RestTemplate restTemplate) {
        this.draeConnectionService = draeConnectionService;
        this.raeService = raeService;
        this.jsoupConnectionService = jsoupConnectionService;
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


    @RequestMapping(value = "/search-rae", method = RequestMethod.GET)
    public String searchPageRAE(Model model, @RequestParam(defaultValue = "casa") String word) {

        List<BaseResponse> wordList = raeService.getTheLemmaListFromTheRaeAPI(restTemplate, word);

        model.addAttribute("words", wordList.get(0).getRes());

        return "/freemarker/rae-new-search/searchRae";
    }


    @RequestMapping(value = "/show-rae", method = RequestMethod.GET)
    public String showRAEDefinitionWordData(Model model, @RequestParam String wordId, @RequestParam String lemma) {

        String definitionsResponse = raeService.getTheDefinitionListByWordId(restTemplate, wordId);

        List<String> definitions = jsoupConnectionService.getAllDefinitions(definitionsResponse);

        definitions.remove(0);

        model.addAttribute("word", lemma);
        model.addAttribute("definitions", definitions);

        return "/freemarker/rae-new-search/searchRaeDefinitions";
    }
}
