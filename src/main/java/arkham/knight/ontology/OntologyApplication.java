package arkham.knight.ontology;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.services.SimpleWordService;
import arkham.knight.ontology.services.SurveyWordService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import java.io.*;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Español Dominicano", version = "1.0", description = "API Centrada en el desarrollo de un diccionario de palabras dominicanas"))
public class OntologyApplication {

    public static void main(String[] args) { SpringApplication.run(OntologyApplication.class, args); }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }


    private void openIndexPage(boolean identifier) {

        Runtime runtime = Runtime.getRuntime();
        try {
            
            if (identifier)
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:88/");
            else
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:88/swagger.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public CommandLineRunner startup(SimpleWordService simpleWordService, SurveyWordService surveyWordService) {
        return args -> {

            simpleWordService.saveSimpleWord(new SimpleWord("Jevi", "Referido a persona, simpática, divertida.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Jevi", "Algo genial", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Fundazo", "Golpe fuerte.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Cocotazo", "Golpe en la cabeza con el puño cerrado", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Bacano", "persona o cosa muy de moda. ¡Que pantalone' ma' vacano!", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Tumbe", "robo, arrebato de algo, estafa. me dieron un tumbe (me robaron)", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Trompón", "Trompada. No me sigas molestando, te vua' da' un trompón!", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Desacatao", "Alguien que no hace lo que otros le dicen; un rebelde; alguien listo y dispuesto a hacer cualquier cosa.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Lleca", "calle. Es un juego de la palabra calle.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Wawawa", "Aquellas personas que les gustan las costumbres del barrio y emitan sus actividades.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Popi", "Una persona de clase media o alta, caracterizada por ser  atractiva con gustos refinados.", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Bobo", "Es un  problema o algo que te molesta.", false));

//            surveyWordService.saveSurveyWord(new SurveyWord("Jevi", "Referido a persona, simpática, divertida.","Temporal", "Genial", "TEST"));
//            surveyWordService.saveSurveyWord(new SurveyWord("Fundazo", "Golpe fuerte.","Temporal", "Genial", "TEST"));
//            surveyWordService.saveSurveyWord(new SurveyWord("Cocotazo", "Golpe en la cabeza con el puño cerrado","Temporal", "Genial", "TEST"));
//            surveyWordService.saveSurveyWord(new SurveyWord("Bacano", "persona o cosa muy de moda. ¡Que pantalone' ma' vacano!","Temporal", "Genial", "TEST"));
//            surveyWordService.saveSurveyWord(new SurveyWord("Tumbe", "robo, arrebato de algo, estafa. me dieron un tumbe (me robaron)","Temporal", "Genial", "TEST"));
//            surveyWordService.saveSurveyWord(new SurveyWord("Trompón", "Trompada. No me sigas molestando, te vua' da' un trompón!","Temporal", "Genial", "TEST"));

//            openIndexPage(true);
//            openIndexPage(false);
        };
    }
}
