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
import java.net.URLConnection;
import java.util.Iterator;

public class OntologyConnectionService {

    public final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    private final String ontologyURL = "https://ghcdn.rawgit.org/ArkhamKnightXD/spring-boot-ontology-test/master/src/main/resources/ontology/diccionario.owl";

    private final File ontologyFile = new File("src/main/resources/ontology/diccionario.owl");

    public final String ontologyURI = getOntology().getURI().concat("#");

    public Property definitionProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("definicion"));

    public Property exampleProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("ejemplo"));

    public Property lemmaRAEProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("lema_rae"));

    public Property synonymsProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("sinonimos"));

    public Property totalAnswersProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("total_respuestas_N"));

    public Property votesQuantityProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("cantidad_votaciones_I"));

    public Property numberOfAbsencesProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("numero_de_ausencias_D"));

    public Property numberOfPresencesProperty = readOntologyFileAndReturnTheModel().getProperty(ontologyURI.concat("numero_de_presencias_A"));

    private static OntologyConnectionService instance;


    public static OntologyConnectionService getInstance() {

        if (instance == null)
            return instance = new OntologyConnectionService();

        return instance;
    }


    public InputStream getOntologyURLInputStream() {

        try {
            return new URL(ontologyURL).openStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public OutputStream getOntologyURLOutputStream() {

        URLConnection urlConnection = null;

        try {

            urlConnection = new URL(ontologyURL).openConnection();
            urlConnection.setDoOutput(true);

            return urlConnection.getOutputStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public Ontology getOntology(){

        Ontology ontology = null;

        Iterator<Ontology> ontologyIterator = readOntologyFileAndReturnTheModel().listOntologies();

        while (ontologyIterator.hasNext()) {

            ontology = ontologyIterator.next();

        }

        return ontology;
    }


    public OntModel readOntologyFileAndReturnTheModel() {

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        FileReader reader = null;

        try {
            reader = new FileReader(ontologyFile);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        model.read(reader,null);

        //URL Connection
//        model.read(getOntologyURLInputStream(), null);

        return model;
    }


    public void saveOntologyFile(OWLOntology ontology){

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        try {
            // save in RDF/XML
            ontologyManager.saveOntology(ontology, ontologySaveIRI);

            //UrlConnection
//            ontologyManager.saveOntology(ontology, getOntologyURLOutputStream());
        } catch (OWLOntologyStorageException exception) {
            exception.printStackTrace();
        }

        // Remove the ontology from the manager, esta parte es necesaria porque sino da error a la hora de guardar mas de una clase o individual
        ontologyManager.removeOntology(ontology);
    }


    public OWLOntology loadTheOntologyOwlAPI(){

        try {

            return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            //URl connection
//            return ontologyManager.loadOntologyFromOntologyDocument(getOntologyURLInputStream());
        } catch (OWLOntologyCreationException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
