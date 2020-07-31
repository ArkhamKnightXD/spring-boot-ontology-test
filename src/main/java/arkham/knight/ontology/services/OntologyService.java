package arkham.knight.ontology.services;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class OntologyService {

    final String ontologyFileName = "Lemas.owl";


    public OntModel readOntologyFileAndReturnTheModel(){

        try {

            File file = new File(ontologyFileName);

            FileReader reader = new FileReader(file);

            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void saveOntologyFile(OntModel ontologyModel, Individual individualToSave) throws IOException {

      //  File file = new File(ontologyFileName);

       // FileWriter fileWriter = new  FileWriter(file);

        //fileWriter.write(String.valueOf(individualToSave));

        OutputStream out = new FileOutputStream((File) individualToSave);

        ontologyModel.write(out);

        ontologyModel.close();

        //fileWriter.close();
    }
}
