package arkham.knight.ontology.services;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

public class OntologyConnectionService {

    public final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    private final String ontologyURL = "https://ghcdn.rawgit.org/ArkhamKnightXD/spring-boot-ontology-test/master/src/main/resources/ontology/diccionario.owl";

    private final File ontologyFile = new File("src/main/resources/ontology/diccionario.owl");

    public final String ontologyURI = getOntology().getURI().concat("#");

    public final String definitionURI = ontologyURI.concat("definicion");

    public final String exampleURI = ontologyURI.concat("ejemplo");

    public final String lemmaRAEURI = ontologyURI.concat("lema_rae");

    public final String synonymsURI = ontologyURI.concat("sinonimos");

    public Property definitionProperty = readOntologyFileAndReturnTheModel().getProperty(definitionURI);

    public Property exampleProperty = readOntologyFileAndReturnTheModel().getProperty(exampleURI);

    public Property lemmaRAEProperty = readOntologyFileAndReturnTheModel().getProperty(lemmaRAEURI);

    public Property synonymsProperty = readOntologyFileAndReturnTheModel().getProperty(synonymsURI);

    private static OntologyConnectionService instance;


    public static OntologyConnectionService getInstance() {

        if (instance == null)
            return instance = new OntologyConnectionService();

        return instance;
    }


    public OntModel readOntologyFileAndReturnTheModel() {

        FileReader reader = null;

        try {
            reader = new FileReader(ontologyFile);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(reader,null);

        //URL Connection
//        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
//        try {
//            model.read(new URL(ontologyURL).openStream(),null);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

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


    public void saveOntologyFile(OWLOntology ontology){
        //Este metodo falla a la hora de implementarse con url, pero todos los demas funcionan bien
//        IRI ontologySaveIRI = IRI.create(ontologyURL);

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        try {
            // save in RDF/XML
            ontologyManager.saveOntology(ontology, ontologySaveIRI);
        } catch (OWLOntologyStorageException exception) {
            exception.printStackTrace();
        }

        // Remove the ontology from the manager, esta parte es necesaria porque sino da error a la hora de guardar mas de una clase o individual
        ontologyManager.removeOntology(ontology);
    }


    public OWLOntology loadTheOntologyOwlAPI(){

        try {
            return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException exception) {
            exception.printStackTrace();
        }

        //URl connection
//        try {
//            return ontologyManager.loadOntologyFromOntologyDocument(new URL(ontologyURL).openStream());
//        } catch (OWLOntologyCreationException | IOException exception) {
//            exception.printStackTrace();
//        }
        return null;
    }
}
