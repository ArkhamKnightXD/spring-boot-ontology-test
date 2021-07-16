package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DefinitionResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JsoupConnectionService {


    public List<String> getAllDefinitions(String definitionResponse) {

        Document document = Jsoup.parse(definitionResponse);

        List<String> attributes = new ArrayList<>();

        document.getElementsByTag("p").forEach(element -> {

            attributes.add(element.text());
        });

        return attributes;
    }


    public List<Element> getAllTagsByName(String definitionResponse, String tagName) {

        Document document = Jsoup.parse(definitionResponse);

        return new ArrayList<>(document.getElementsByTag(tagName));
    }


    public List<DefinitionResponse> getSeparateDefinitionData(List<String> definitionList) {

        List<DefinitionResponse> cleanDefinitionList = new ArrayList<>();

        for (String definition : definitionList) {

            DefinitionResponse definitionResponse = new DefinitionResponse();

            String [] tokens = definition.split("[.]");

            definitionResponse.setDefinitionNumber(tokens[0]);
            definitionResponse.setWordClassType(tokens[1]);
            definitionResponse.setDefinition(tokens[2]);

            cleanDefinitionList.add(definitionResponse);
        }

        return cleanDefinitionList;
    }
}
