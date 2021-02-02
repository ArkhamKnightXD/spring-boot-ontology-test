package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OntologyService {

    @Autowired
    private OntologyConnectionService ontologyConnectionService;

    private final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

    private final IRI ontologyIRI = IRI.create("http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#");


    public List<String> getAllClassesLocalName(){

        List<String> classListNames = new ArrayList<>();

        Iterator<OntClass> classesIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listClasses();


        while (classesIterator.hasNext()) {

            OntClass nextClass = classesIterator.next();

            classListNames.add(nextClass.getLocalName());
        }

        return classListNames;
    }


    public List<String> getAllIndividualLocalName(){

        List<String> individualNamesList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();


        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            individualNamesList.add(individual.getLocalName());
        }

        return individualNamesList;
    }


    public List<Individual> getAllIndividualsByFatherClassName(String fatherClassName){

        List<Individual> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();

        while (individualsIterator.hasNext()) {

            Individual individual = individualsIterator.next();

            if (individual.getOntClass().getLocalName().equalsIgnoreCase(fatherClassName))
                individualList.add(individual);
        }

        return individualList;
    }


    public void saveFatherClassAndSubClass(String fatherClassName, String subClassName) {

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + fatherClassName));
        OWLClass subClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + subClassName));

       // OWLAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classA, classB);

        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(fatherClass, subClass);

        ontologyConnectionService.ontologyManager.addAxiom(ontology,axiom);

        ontologyConnectionService.saveOntologyFile(ontology);
    }


    public void saveIndividual(String originalIndividualName, String individualName, String fatherClassName, String definition, String example, String lemmaRAE, String synonyms) {

        deleteIndividual(originalIndividualName);

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        OWLIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIRI + individualName));

        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + fatherClassName));

        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);


        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiom);

        saveIndividualProperties(ontology, individual, definition, example, lemmaRAE, synonyms);

        ontologyConnectionService.saveOntologyFile(ontology);
    }


    public void saveIndividualProperties(OWLOntology ontology, OWLIndividual individual, String definition, String example, String lemmaRAE, String synonyms) {

        IRI dataTypePropertyIRI = IRI.create(ontologyIRI +"definicion");

        IRI dataTypePropertyExampleIRI = IRI.create(ontologyIRI +"ejemplo");

        IRI dataTypePropertyLemmaRAEIRI = IRI.create(ontologyIRI +"lema_rae");

        IRI dataTypePropertySynonymsIRI = IRI.create(ontologyIRI +"sinonimos");


        OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(dataTypePropertyIRI);

        OWLDataProperty exampleDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyExampleIRI);

        OWLDataProperty lemmaRAEDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyLemmaRAEIRI);

        OWLDataProperty synonymsDataProperty = dataFactory.getOWLDataProperty(dataTypePropertySynonymsIRI);


        OWLDataPropertyAssertionAxiom axiomDefinition = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, definition);

        OWLDataPropertyAssertionAxiom axiomExample = dataFactory.getOWLDataPropertyAssertionAxiom(exampleDataProperty, individual, example);

        OWLDataPropertyAssertionAxiom axiomLemmaRAE = dataFactory.getOWLDataPropertyAssertionAxiom(lemmaRAEDataProperty, individual, lemmaRAE);

        OWLDataPropertyAssertionAxiom axiomSynonyms = dataFactory.getOWLDataPropertyAssertionAxiom(synonymsDataProperty, individual, synonyms);


        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomDefinition);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomExample);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomLemmaRAE);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomSynonyms);
    }


    public void deleteIndividual(String individualName) {

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        IRI individualIRI = IRI.create(ontologyIRI + individualName);

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));


        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            if (individualIRI.toString().equals(individual.getIRI().toString()))
                individual.accept(remover);
        }

        ontologyConnectionService.ontologyManager.applyChanges(remover.getChanges());

        ontologyConnectionService.saveOntologyFile(ontology);
    }


    public List<String> tokenizeTheSentence(String sentence){

        String [] tokens = sentence.split("[\\s'.,:;]");

        return new ArrayList<>(Arrays.asList(tokens));
    }


    public List<Individual> getAllIndividualByName(List<String> sentenceByWords, String searchType) {

        List<Individual> individualList = new ArrayList<>();

        Iterator<Individual> individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheModel().listIndividuals();

        Individual individual;


        while (individualsIterator.hasNext()) {

            int avoidRepeatIndividualCount = 0;

            individual = individualsIterator.next();

            compareAllWordsInTheWordListAndSaveInTheIndividualList(searchType, sentenceByWords, individualList,avoidRepeatIndividualCount, individual);
        }

        return individualList;
    }


    public void compareAllWordsInTheWordListAndSaveInTheIndividualList(String searchType, List<String> sentenceByWords, List<Individual> individualList, int avoidRepeatIndividualCount, Individual individual){

        //Con StringUtils.stripAccents a cada palabra que tenga una letra con acento se le quitara el acento
        String cleanIndividual = StringUtils.stripAccents(individual.getLocalName());

        for (String word: sentenceByWords) {

            String cleanWord = StringUtils.stripAccents(word);

            if (cleanIndividual.equalsIgnoreCase(cleanWord) && avoidRepeatIndividualCount == 0 || searchType.equals("word-search") & cleanIndividual.matches(".*"+cleanWord.toLowerCase()+".*") && avoidRepeatIndividualCount == 0){
                individualList.add(individual);

                avoidRepeatIndividualCount++;
            }
        }
    }


    public List<Word> saveAllIndividualPropertiesValueInAWordList(List<Individual> individualList){

        List<Word> wordList = new ArrayList<>();

        Property definition = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.definitionURI);

        Property example = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.exampleURI);

        Property lemmaRAE = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.lemmaRAEURI);

        Property synonyms = ontologyConnectionService.readOntologyFileAndReturnTheModel().getProperty(ontologyConnectionService.synonymsURI);


        for (Individual individual: individualList) {

            Word wordToSave = new Word();

            if (individual.getLocalName()!= null)
                wordToSave.setLema(individual.getLocalName());

            if (individual.getOntClass() != null)
                wordToSave.setClasePadre(individual.getOntClass().getLocalName());

            RDFNode definitionPropertyValue = individual.getPropertyValue(definition);

            if (definitionPropertyValue!= null)
                wordToSave.setDefinicion(definitionPropertyValue.toString());

            RDFNode examplePropertyValue = individual.getPropertyValue(example);

            if (examplePropertyValue!= null)
                wordToSave.setEjemplo(examplePropertyValue.toString());

            RDFNode lemmaRAEPropertyValue = individual.getPropertyValue(lemmaRAE);

            if (lemmaRAEPropertyValue!= null)
                wordToSave.setLemaRAE(lemmaRAEPropertyValue.toString());

            RDFNode synonymsPropertyValue = individual.getPropertyValue(synonyms);

            if (synonymsPropertyValue != null)
                wordToSave.setSinonimos(synonymsPropertyValue.toString());

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
