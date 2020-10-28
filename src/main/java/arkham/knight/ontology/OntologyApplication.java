package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.ArrayList;
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

          /*  final String textExample = "Yo soy un jornalero que esperaba beber un morirsoñando em el entretiempo para despues ponerme a motoconchar echadías";

            for (Individual individual: ontologyService.findAllIndividualByName(ontologyService.getAllIndividualLocalName(),"text")) {

                System.out.println(individual.getLocalName());
            }


            String string = "pancakes";

            // Esta la implementacion en java del metodo like de sql
            //En este caso al string se le debe de agregar .* que es el equivalente a % esto quiere decir que no importa las palabras demas que haya
            String string2 = ".*panca.*";

            //y con el metodo matches aqui hago las comparaciones
            if (string.matches(string2)){

                System.out.println(string);
            }*/
        };
    }
}
