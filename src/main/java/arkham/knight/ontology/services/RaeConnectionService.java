package arkham.knight.ontology.services;

import arkham.knight.ontology.models.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RaeConnectionService {

    private final String URLConnection = "http://localhost:8080/";


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    public List<BaseResponse> getTheLemmaListFromTheRaeAPI(RestTemplate restTemplate, String wordToSearch){

        String searchLink =  URLConnection + "search?word="+wordToSearch;

        return getBaseResponses(restTemplate, searchLink);
    }


    public List<BaseResponse> getTheExactLemmaFromTheRaeAPI(RestTemplate restTemplate, String wordToSearch){

        String searchLink = URLConnection + "exact?word="+wordToSearch;

        return getBaseResponses(restTemplate, searchLink);
    }


    private List<BaseResponse> getBaseResponses(RestTemplate restTemplate, String searchLink) {

        ResponseEntity<String> RaeResponse = restTemplate.getForEntity(searchLink, String.class);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        return new ArrayList<>(Collections.singletonList(gson.fromJson(RaeResponse.getBody(), BaseResponse.class)));
    }


    public String getTheDefinitionListByWordId(RestTemplate restTemplate, String wordToSearch){

        String searchLink = URLConnection + "definition?id="+wordToSearch;

        ResponseEntity<String> responseEntityDRAE = restTemplate.getForEntity(searchLink, String.class);

        return responseEntityDRAE.getBody();
    }
}
