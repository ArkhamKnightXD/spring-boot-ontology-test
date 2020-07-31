package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.TDBConnectionService;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class OntologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntologyApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(OntologyService ontologyService) {
        return args -> {

            //ontologyService.readOntologyFileAndReturn().write(System.out,"RDF/XML-ABBREV");

            /*TDBConnectionService tdbConnectionService = new TDBConnectionService("tdb");

            File file = new File("Lemas.owl");

            FileReader reader = new FileReader(file);

            Model model = ModelFactory.createDefaultModel();

            model.read(reader,null);

            tdbConnectionService.loadModel(model,"Lemas.owl");

            tdbConnectionService.closeModel();*/
        };
    }
}
