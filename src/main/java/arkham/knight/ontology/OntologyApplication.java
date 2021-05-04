package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.SurveyWordDataService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.*;
import java.util.Set;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Español Dominicano", version = "1.0", description = "API Centrada en el desarrollo de un diccionario de palabras dominicanas"))
public class OntologyApplication {

    public static void main(String[] args) { SpringApplication.run(OntologyApplication.class, args); }


    private void openIndexPage(boolean identifier) {

        Runtime runtime = Runtime.getRuntime();
        try {
            if (identifier)
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:88/words/");
            else
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:88/swagger.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public CommandLineRunner startup(SurveyWordDataService surveyWordDataService) {
        return args -> {

            final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

//            surveyWordDataService.deleteAllSurveys();


//            OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
//            OWLClass owlClass = dataFactory.getOWLClass(IRI.create(ontologyConnectionService.prefixManager.getDefaultPrefix(), "Adjetivos"));
//
//            Set<OWLNamedIndividual> individualSet = ontologyConnectionService.getHermitReasoner().getInstances(owlClass, false).getFlattened();
//
//            for (OWLNamedIndividual individual :individualSet) {
//
//                //El defaultPrefixManager se utiliza aqui para de esta forma poder obtener el nombre corto del individual
//                System.out.println("Individual Name: " + ontologyConnectionService.prefixManager.getShortForm(individual));
//            }
//            openIndexPage(true);
//
//            openIndexPage(false);
        };
    }
}
