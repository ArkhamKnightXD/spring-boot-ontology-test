package arkham.knight.ontology.services;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;

@Service
public class OntologyService {


    public OntModel readOntologyFileAndReturn(){

        final String fileName = "Lemas.owl";

        try {

            File file = new File(fileName);

            FileReader reader = new FileReader(file);

            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
