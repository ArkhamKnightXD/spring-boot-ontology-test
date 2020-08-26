package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class OntologyService {

    final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

    final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    final IRI ontologyIRI = IRI.create("http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2");

    final File ontologyFile = new File("diccionario.owl");


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

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));


        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            if (individualIRI.toString().equals(individual.getIRI().toString()))
            individual.accept(remover);
        }

        ontologyManager.applyChanges(remover.getChanges());

        ontologyManager.saveOntology(ontology, ontologySaveIRI);

        ontologyManager.removeOntology(ontology);
    }


    public List<String> getAllWordsFromTheSentence(String sentence){

        String [] tokens = sentence.split("[\\s'.,:;]");

        return new ArrayList<>(Arrays.asList(tokens));
    }


    public List<Individual> findAllIndividualByName(List<String> sentenceByWords) {

        List<Individual> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = readOntologyFileAndReturnTheModel().listIndividuals();

        Individual individual;


        while (individualsIterator.hasNext()) {

            int avoidRepeatIndividualCount = 0;

            individual = individualsIterator.next();

            for (String word: sentenceByWords) {

                if (individual.getLocalName().equalsIgnoreCase(word) && avoidRepeatIndividualCount == 0){
                    individualList.add(individual);

                    avoidRepeatIndividualCount++;
                }
            }
        }

        return individualList;
    }


    public List<Word> saveAllPropertiesValueInAWordList(List<Individual> individualList, Property definition, Property example, Property grammarMark, Property marcaNivelSocioCultural, Property marcaVariacionEstilistica, Property locution, Property locutionType){

        List<Word> wordList = new ArrayList<>();


        for (Individual individual: individualList) {

            Word wordToSave = new Word();

            wordToSave.setLema(individual.getLocalName());

            RDFNode definitionPropertyValue = individual.getPropertyValue(definition);

            wordToSave.setDefinicion(definitionPropertyValue.toString());

            RDFNode examplePropertyValue = individual.getPropertyValue(example);

            if (examplePropertyValue!= null)
                wordToSave.setEjemplo(examplePropertyValue.toString());

            RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMark);

            if (grammarMarkPropertyValue!= null)
                wordToSave.setMarcaGramatical(grammarMarkPropertyValue.toString());

            RDFNode marcaNivelSocioCulturalValue = individual.getPropertyValue(marcaNivelSocioCultural);

            if (marcaNivelSocioCulturalValue!= null)
                wordToSave.setMarcaNivelSocioCultural(marcaNivelSocioCulturalValue.toString());

            RDFNode marcaVariacionEstilisticaValue = individual.getPropertyValue(marcaVariacionEstilistica);

            if (marcaVariacionEstilisticaValue!= null)
                wordToSave.setMarcaVariacionEstilistica(marcaVariacionEstilisticaValue.toString());

            RDFNode locutionValue = individual.getPropertyValue(locution);

            if (locutionValue!= null)
                wordToSave.setLocucion(locutionValue.toString());

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
