package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.BaseResponse;
import arkham.knight.ontology.services.JsoupService;
import arkham.knight.ontology.services.RaeConnectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RequestMapping("/rae")
@Controller
public class RaeController {

    private final RaeConnectionService raeConnectionService;

    private final JsoupService jsoupService;

    private final RestTemplate restTemplate;

    public RaeController(RaeConnectionService raeConnectionService, JsoupService jsoupService, RestTemplate restTemplate) {
        this.raeConnectionService = raeConnectionService;
        this.jsoupService = jsoupService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/search-rae")
    public String searchPageRAE(Model model, @RequestParam(defaultValue = "casa") String sentence) {

        List<BaseResponse> wordList = raeConnectionService.getTheLemmaListFromTheRaeAPI(restTemplate, sentence);

        model.addAttribute("words", wordList.get(0).getRes());

        return "/freemarker/rae-new-search/searchRae";
    }


    @GetMapping("/show-rae")
    public String showRAEDefinitionWordData(Model model, @RequestParam String wordId, @RequestParam String lemma) {

        String definitionsResponse = raeConnectionService.getTheDefinitionListByWordId(restTemplate, wordId);

        List<String> definitions = jsoupService.getAllInitialData(definitionsResponse);

        definitions.remove(0);

        model.addAttribute("word", lemma);
        model.addAttribute("definitions", definitions);

        return "/freemarker/rae-new-search/searchRaeDefinitions";
    }
}
