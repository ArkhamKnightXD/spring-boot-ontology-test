package arkham.knight.ontology.services;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Collections;

@Service
public class OntologyService {

    final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

    final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    final IRI ontologyIRI = IRI.create("http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2");

    final File ontologyFile = new File("diccionario.owl");


    public OntModel readOntologyFileAndReturnTheModel() throws FileNotFoundException {

            FileReader reader = new FileReader(ontologyFile);

            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            return model;
    }


    public void saveClasses(String className1, String className2) throws OWLOntologyCreationException, OWLOntologyStorageException {

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        OWLClass classA = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + className1));
        OWLClass classB = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + className2));

        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(classA, classB);

        // add the axiom to the ontology the axioms are the classes.
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);

        // We now use the manager to apply the change
        ontologyManager.applyChange(addAxiom);

        // save in RDF/XML
        ontologyManager.saveOntology(ontology, ontologySaveIRI);

        // Remove the ontology from the manager, esta parte es necesaria porque sino da error a la hora de guardar mas de una clase o individual
        ontologyManager.removeOntology(ontology);
    }


    public void saveIndividual(String individualName, String fatherClassName) throws OWLOntologyCreationException, OWLOntologyStorageException {

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

        OWLIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + individualName));

        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + fatherClassName));
        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);

        ontologyManager.addAxiom(ontology, axiom);

        ontologyManager.saveOntology(ontology, ontologySaveIRI);

        ontologyManager.removeOntology(ontology);
    }


    public void deleteIndividual(String individualName) throws OWLOntologyCreationException, OWLOntologyStorageException {

        IRI ontologySaveIRI = IRI.create(ontologyFile);

        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

        IRI individualIRI = IRI.create(ontologyIRI + "#" + individualName);

        OWLIndividual individualToDelete = dataFactory.getOWLNamedIndividual(individualIRI);

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));


        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            if (individualIRI.toString().equals(individual.getIRI().toString()))
            individual.accept(remover);
        }

        // Now apply the changes
        ontologyManager.applyChanges(remover.getChanges());

        ontologyManager.saveOntology(ontology, ontologySaveIRI);

        ontologyManager.removeOntology(ontology);
    }
}
