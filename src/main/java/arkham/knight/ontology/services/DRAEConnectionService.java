package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DRAEDefinition;
import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.DRAEVariation;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DRAEConnectionService {

    //Este restTemplate tendra timeout para evitar que el api DRAE se quede buscando una palabra de forma indefinida
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplateBuilder()
//                .setConnectTimeout(Duration.ofMillis(1500))
//                .setReadTimeout(Duration.ofMillis(1500))
//                .build();
//    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    public List<DRAEObject> getTheWordDataFromDRAE(RestTemplate restTemplate, String wordToSearch){

        //Link to use with docker
//        String searchLink = "http://docker-squat-drae:4000/api/"+wordToSearch;

        var searchLink = "http://localhost:4000/api/"+wordToSearch;

        ResponseEntity<String> responseEntityDRAE = restTemplate.getForEntity(searchLink, String.class);

        var builder = new GsonBuilder();
        builder.setPrettyPrinting();
        var gson = builder.create();

        return new ArrayList<>(Arrays.asList(gson.fromJson(responseEntityDRAE.getBody(), DRAEObject[].class)));
    }


    public List<DRAEDefinition> getAllDefinitionsFromDRAEWordList(List<DRAEObject> words){

        List<DRAEDefinition> definitionList = new ArrayList<>();

        for (var word: words) {

            definitionList.addAll(word.getDefinitions());
        }

        return definitionList;
    }


    public List<DRAEVariation> getAllVariationsFromDRAEWordList(List<DRAEObject> words){

        List<DRAEVariation> variationList = new ArrayList<>();

        for (var word: words) {

            variationList.addAll(word.getVariations());
        }

        return variationList;
    }
}
