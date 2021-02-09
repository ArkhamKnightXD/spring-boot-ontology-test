package arkham.knight.ontology.services;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Iterator;

@Service
public class OntologyConnectionService {

    //Utilizare inputstream para docker
    private final InputStream inputStream = OntologyConnectionService.class.getResourceAsStream("/ontology/diccionario.owl");

    private final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

    BufferedReader reader = new BufferedReader(inputStreamReader);

    //utilizare este normal file cuando trabaje de forma local
    private final File ontologyFile = new File("src/main/resources/ontology/diccionario.owl");

    //simple jar file
    //private final File ontologyFile = new File(System.getProperty("user.dir")+"\\ontology-0.0.1-SNAPSHOT\\BOOT-INF\\classes\\ontology\\diccionario.owl");


    public final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    public OntModel readOntologyFileAndReturnTheModel() {

      //  utilizo esto cuando trabajo local
        FileReader reader = null;

        try {
            reader = new FileReader(ontologyFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(reader,null);

        return model;
    }

    public Ontology getOntology(){

        Ontology ontology = null;

        Iterator<Ontology> ontologyIterator = readOntologyFileAndReturnTheModel().listOntologies();

        while (ontologyIterator.hasNext()) {

            ontology = ontologyIterator.next();

        }

        return ontology;
    }

    public final String ontologyURI = getOntology().getURI().concat("#");

    public final String definitionURI = ontologyURI.concat("definicion");

    public final String exampleURI = ontologyURI.concat("ejemplo");

    public final String lemmaRAEURI = ontologyURI.concat("lema_rae");

    public final String synonymsURI = ontologyURI.concat("sinonimos");

    public void saveOntologyFile(OWLOntology ontology){

        //no podre salvar en docker gracias al asunto de file ya que debo de utilizar inputstream
        IRI ontologySaveIRI = IRI.create(ontologyFile);

        //probar luego salvando mediante outputstream
        try {
            // save in RDF/XML
            ontologyManager.saveOntology(ontology, ontologySaveIRI);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

        // Remove the ontology from the manager, esta parte es necesaria porque sino da error a la hora de guardar mas de una clase o individual
        ontologyManager.removeOntology(ontology);
    }

    public OWLOntology loadTheOntologyOwlAPI(){

        //No estoy seguro si esto da error con inputstream
        try {
            return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
