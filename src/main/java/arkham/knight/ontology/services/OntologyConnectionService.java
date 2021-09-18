package arkham.knight.ontology.services;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

public class OntologyConnectionService {

    public final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    private final String ontologyURL = "https://ghcdn.rawgit.org/ArkhamKnightXD/spring-boot-ontology-test/master/src/main/resources/ontology/diccionario.owl";

    private final File ontologyFile = new File("src/main/resources/ontology/diccionario.owl");

    public final String ontologyURI = getOntology().getURI().concat("#");

    public Property definitionProperty = getPropertyByName("definicion");

    public Property exampleProperty = getPropertyByName("ejemplo");

    public Property lemmaRAEProperty = getPropertyByName("lema_rae");

    public Property synonymsProperty = getPropertyByName("sinonimos");

    public Property totalAnswersProperty = getPropertyByName(("total_respuestas_N"));

    public Property votesQuantityProperty = getPropertyByName(("cantidad_votaciones_I"));

    private static OntologyConnectionService instance;


    public static OntologyConnectionService getInstance() {

        if (instance == null)
            return instance = new OntologyConnectionService();

        return instance;
    }


    private InputStream getOntologyURLInputStream() {

        try {

            return new URL(ontologyURL).openStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }


    private OutputStream getOntologyURLOutputStream() {

        URLConnection urlConnection;

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

        Ontology actualOntology = null;

        List<Ontology> ontologies = readOntologyFileAndReturnTheJenaModel().listOntologies().toList();

        for (Ontology ontology : ontologies) {

            actualOntology = ontology;
        }

        return actualOntology;
    }


    private OntModel readOntologyFileAndReturnTheJenaModel() {

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        FileReader reader = null;

        if (ontologyFile.exists()){

            try {

                reader = new FileReader(ontologyFile);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

            model.read(reader,null);
        }
        //URL Connection
        else
            model.read(getOntologyURLInputStream(), null);

        return model;
    }


    public Property getPropertyByName(String propertyName){

        return readOntologyFileAndReturnTheJenaModel().getProperty(ontologyURI.concat(propertyName));
    }


    public List<Individual> getAllIndividuals(){

        return readOntologyFileAndReturnTheJenaModel().listIndividuals().toList();
    }


    public List<OntClass> getAllClasses(){

        return readOntologyFileAndReturnTheJenaModel().listClasses().toList();
    }


    public List<DatatypeProperty> getAllDataTypeProperties(){

        return readOntologyFileAndReturnTheJenaModel().listDatatypeProperties().toList();
    }


    public void saveOntologyFile(OWLOntology ontology){

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        try {
            // save in RDF/XML
            if (ontologyFile.exists())
                ontologyManager.saveOntology(ontology, ontologySaveIRI);

            //URL Connection
            else
                ontologyManager.saveOntology(ontology, Objects.requireNonNull(getOntologyURLOutputStream()));
        } catch (OWLOntologyStorageException exception) {
            exception.printStackTrace();
        }
        // Remove the ontology from the manager, esta parte es necesaria porque sino da error a la hora de guardar mas de una clase o individual
        //remuevo la ontologia para que no se quede en uso, si esta queda en uso no puedo volver a utilizar la ontologia o me da error
        ontologyManager.removeOntology(ontology);
    }


    public OWLOntology loadTheOntologyOwlAPI(){

        try {

            if (ontologyFile.exists())
                return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
            //URL Connection
            return ontologyManager.loadOntologyFromOntologyDocument(Objects.requireNonNull(getOntologyURLInputStream()));
        } catch (OWLOntologyCreationException exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public OWLReasoner getHermitReasoner(){

        OWLOntology ontology = loadTheOntologyOwlAPI();
        //Setting up the reasoner
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration reasonerConfiguration = new SimpleConfiguration(progressMonitor);
        //Create the reasoner
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, reasonerConfiguration);
        //Metodo encargado de trabajar la inferencias
        reasoner.precomputeInferences(InferenceType.values());

        ontologyManager.removeOntology(ontology);

        return reasoner;
    }
}
