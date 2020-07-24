package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OntologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntologyApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(OntologyService ontologyService) {
        return args -> {

           // ontologyService.readOntologyFileAndReturn().write(System.out,"RDF/XML-ABBREV");
        };
    }

}
