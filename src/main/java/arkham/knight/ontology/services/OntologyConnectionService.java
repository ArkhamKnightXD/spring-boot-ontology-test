package arkham.knight.ontology.services;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

@Service
public class OntologyConnectionService {

    public final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    private final File ontologyFile = new File("diccionario.owl");

    public OntModel readOntologyFileAndReturnTheModel() {

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

        IRI ontologySaveIRI = IRI.create(ontologyFile);

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

        try {
            return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
