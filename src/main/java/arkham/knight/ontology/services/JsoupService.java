package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DefinitionResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsoupService {


    public List<String> getAllDefinitions(String definitionResponse) {

        Document document = Jsoup.parse(definitionResponse);

        List<String> attributes = new ArrayList<>();

        document.getElementsByTag("p").forEach(element -> {

            attributes.add(element.text());
        });

        return attributes;
    }


    public List<DefinitionResponse> getSeparateDefinitionData(List<String> definitionList) {

        int comparator = 0;

        List<DefinitionResponse> cleanDefinitionList = new ArrayList<>();

        for (String definition : definitionList) {

            DefinitionResponse definitionResponse = new DefinitionResponse();

            String [] tokens = definition.split("[.]");

            try {

                definitionResponse.setDefinitionNumber(tokens[0]);

                evaluateTokenData(definitionResponse, tokens);

                //el try/catch es para esta linea de codigo
                int actualDefinitionNumber = Integer.parseInt(definitionResponse.getDefinitionNumber());

                if (actualDefinitionNumber > comparator && actualDefinitionNumber < 7){

                    comparator = actualDefinitionNumber;

                    cleanDefinitionList.add(definitionResponse);
                }

            } catch (Exception ignored){

            }
        }

        return cleanDefinitionList;
    }


    private void evaluateTokenData(DefinitionResponse definitionResponse, String[] tokens) {

        if (tokens[2].length() < 7) {

            if (tokens[3].length() < 10) {

                definitionResponse.setWordClassType(tokens[1] + "." + tokens[2] + "." + tokens[3] + ".");
                definitionResponse.setDefinition(tokens[4] + ".");
            }

            else {

                definitionResponse.setWordClassType(tokens[1] + "." + tokens[2] + ".");
                definitionResponse.setDefinition(tokens[3] + ".");
            }
        }

        else {

            definitionResponse.setWordClassType(tokens[1] + ".");
            definitionResponse.setDefinition(tokens[2] + ".");
        }
    }
}
