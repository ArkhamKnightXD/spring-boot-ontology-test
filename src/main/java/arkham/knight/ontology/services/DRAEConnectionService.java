package arkham.knight.ontology.services;

import arkham.knight.ontology.models.DRAEObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DRAEConnectionService {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    public DRAEObject[] getTheWordDataFromDRAE(RestTemplate restTemplate, String wordToSearch){

        String searchLink = "http://localhost:4000/api/"+wordToSearch;

        ResponseEntity<String> responseEntityDRAE = restTemplate.getForEntity(searchLink, String.class);

        String wordDataString = responseEntityDRAE.getBody();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        return gson.fromJson(wordDataString, DRAEObject[].class);
    }
}
