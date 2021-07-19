package arkham.knight.ontology;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.services.SimpleWordService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.*;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Español Dominicano", version = "1.0", description = "API Centrada en el desarrollo de un diccionario de palabras dominicanas"))
public class OntologyApplication {

    public static void main(String[] args) { SpringApplication.run(OntologyApplication.class, args); }


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
    public CommandLineRunner startup(SimpleWordService simpleWordService) {
        return args -> {

            simpleWordService.saveSimpleWord(new SimpleWord("Arroz", "Comida", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Arroz", "Test", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Cocotazo", "Golpe", false));
            simpleWordService.saveSimpleWord(new SimpleWord("Relajo", "N/A", false));

//            openIndexPage(true);
//            openIndexPage(false);
        };
    }
}
