package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DefinitionResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class JsoupService {


    private Elements getAllElementsByTag(String definitionResponse, String tag) {

        Document document = Jsoup.parse(definitionResponse);

        return document.getElementsByTag(tag);
    }


    public List<String> getAllInitialData(String definitionResponse) {

        List<String> initialList = new ArrayList<>();

        getAllElementsByTag(definitionResponse, "p").forEach(element -> {

            initialList.add(element.text());
        });

        return initialList;
    }


    public List<String> getAllClasses(String definitionResponse) {

        AtomicInteger counter = new AtomicInteger();

        List<String> classes = new ArrayList<>();

        getAllElementsByTag(definitionResponse, "p").forEach(element -> {

            try {

                if (counter.get() < 10){

                    counter.getAndIncrement();
                    classes.add(element.child(1).text());
                }
            } catch (Exception ignored){

            }
        });

        return classes;
    }


    public List<String> getAllDefinitions(String definitionResponse) {

        AtomicInteger counter = new AtomicInteger();

        List<String> definitions = new ArrayList<>();

        getAllElementsByTag(definitionResponse, "p").forEach(element -> {

            if (counter.get() < 10) {

                counter.getAndIncrement();

                definitions.add(element.getElementsByTag("mark").text());
            }

        });

        return definitions;
    }


    public List<DefinitionResponse> getCompleteDefinitionData(String definitionResponse) {

        int comparator = 0;

        List<DefinitionResponse> cleanDefinitionList = new ArrayList<>();

        List<String> initialList = getAllInitialData(definitionResponse);
        List<String> classes = getAllClasses(definitionResponse);
        List<String> definitions = getAllDefinitions(definitionResponse);

        for (int i = 0; i < initialList.size(); i++) {

            DefinitionResponse definitionData = new DefinitionResponse();

            String [] tokens = initialList.get(i).split("[.]");

            try {

                definitionData.setDefinitionNumber(tokens[0]);
                definitionData.setWordClassType(classes.get(i));
                definitionData.setDefinition(definitions.get(i));

                int actualDefinitionNumber = Integer.parseInt(definitionData.getDefinitionNumber());

                if (actualDefinitionNumber > comparator && actualDefinitionNumber < 10){

                    comparator = actualDefinitionNumber;

                    cleanDefinitionList.add(definitionData);
                }

            } catch (Exception ignored){

            }
        }

        return cleanDefinitionList;
    }
}
