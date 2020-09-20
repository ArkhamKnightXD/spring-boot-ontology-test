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

    final IRI ontologyIRI = IRI.create("http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#");

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


    public OWLOntology loadTheOntologyOwlAPI(){

        try {
            return ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return null;
    }


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


    public void saveClasses(String className1, String className2) {

        OWLOntology ontology = loadTheOntologyOwlAPI();

        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        OWLClass classA = dataFactory.getOWLClass(IRI.create(ontologyIRI + className1));
        OWLClass classB = dataFactory.getOWLClass(IRI.create(ontologyIRI + className2));

        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(classA, classB);

        //OWLAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classA,classB);

        ontologyManager.addAxiom(ontology,axiom);

        saveOntologyFile(ontology);
    }


    public void saveIndividual(String individualName, String fatherClassName, String definition, String example, String mark) {

        OWLOntology ontology = loadTheOntologyOwlAPI();

        OWLIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIRI + individualName));

        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + fatherClassName));

        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);

        ontologyManager.addAxiom(ontology, axiom);

        saveIndividualProperties(ontology, individual, definition, example, mark);


        saveOntologyFile(ontology);
    }


    public void saveIndividualProperties(OWLOntology ontology, OWLIndividual individual, String definition, String example, String mark) {

        IRI dataTypePropertyIRI = IRI.create(ontologyIRI +"definicion");

        IRI dataTypePropertyExampleIRI = IRI.create(ontologyIRI +"ejemplo");

        IRI dataTypePropertyMarkIRI = IRI.create(ontologyIRI +"marca_gramatical");


        OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(dataTypePropertyIRI);

        OWLDataProperty exampleDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyExampleIRI);

        OWLDataProperty markDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyMarkIRI);

        OWLDataPropertyAssertionAxiom axiom = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, definition);

        OWLDataPropertyAssertionAxiom axiomExample = dataFactory.getOWLDataPropertyAssertionAxiom(exampleDataProperty, individual, example);

        OWLDataPropertyAssertionAxiom axiomMark = dataFactory.getOWLDataPropertyAssertionAxiom(markDataProperty, individual, mark);


        ontologyManager.addAxiom(ontology, axiom);

        ontologyManager.addAxiom(ontology, axiomExample);

        ontologyManager.addAxiom(ontology, axiomMark);
    }


    public void deleteIndividual(String individualName) {

        OWLOntology ontology = loadTheOntologyOwlAPI();

        IRI individualIRI = IRI.create(ontologyIRI + individualName);

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));


        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            if (individualIRI.toString().equals(individual.getIRI().toString()))
            individual.accept(remover);
        }

        ontologyManager.applyChanges(remover.getChanges());

        saveOntologyFile(ontology);
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


    public List<Word> saveAllPropertiesValueInAWordList(List<Individual> individualList, Property definition, Property example, Property grammarMark, Property markSocialCulturalLevel, Property markStyleVariation, Property locution){

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

            RDFNode marcaNivelSocioCulturalValue = individual.getPropertyValue(markSocialCulturalLevel);

            if (marcaNivelSocioCulturalValue!= null)
                wordToSave.setMarcaNivelSocioCultural(marcaNivelSocioCulturalValue.toString());

            RDFNode marcaVariacionEstilisticaValue = individual.getPropertyValue(markStyleVariation);

            if (marcaVariacionEstilisticaValue!= null)
                wordToSave.setMarcaVariacionEstilistica(marcaVariacionEstilisticaValue.toString());

            //RDFNode locutionValue = individual.getPropertyValue(locution);
            //if (locutionValue!= null)

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
