package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DRAEDefinition;
import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.DRAEVariation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DRAEConnectionService {


    public List<DRAEObject> getTheWordDataFromDRAE(RestTemplate restTemplate, String wordToSearch){

        //Link to use with docker
//        String searchLink = "http://docker-squat-drae:4000/api/"+wordToSearch;

        String searchLink = "http://localhost:4000/api/"+wordToSearch;

        ResponseEntity<String> responseEntityDRAE = restTemplate.getForEntity(searchLink, String.class);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        return new ArrayList<>(Arrays.asList(gson.fromJson(responseEntityDRAE.getBody(), DRAEObject[].class)));
    }


    public List<DRAEDefinition> getAllDefinitionsFromDRAEWordList(List<DRAEObject> words){

        List<DRAEDefinition> definitionList = new ArrayList<>();

        for (DRAEObject word: words) {

            definitionList.addAll(word.getDefinitions());
        }

        return definitionList;
    }


    public List<DRAEVariation> getAllVariationsFromDRAEWordList(List<DRAEObject> words){

        List<DRAEVariation> variationList = new ArrayList<>();

        for (DRAEObject word: words) {

            variationList.addAll(word.getVariations());
        }

        return variationList;
    }
}
