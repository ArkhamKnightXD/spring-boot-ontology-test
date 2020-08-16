package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class OntologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntologyApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(OntologyService ontologyService) {
        return args -> {

           // ontologyService.readOntologyFileAndReturnTheModel().write(System.out,"RDF/XML-ABBREV");

            /*final String textExample = ",:echadías;.";

            List<Individual> individualList = ontologyService.findAllIndividualByName(ontologyService.getAllWordsFromTheSentence(textExample));

            for (Individual individual: individualList) {

                System.out.println(individual.getLocalName());
            }*/
        };
    }
}
