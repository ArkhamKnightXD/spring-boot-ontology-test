package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OntologyService {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    private final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

    private final IRI ontologyIRI = IRI.create(ontologyConnectionService.ontologyURI);


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


    public String saveFatherClassAndSubClass(String fatherClassName, String subClassName) {

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + fatherClassName));
        OWLClass subClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + subClassName));

       // OWLAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classA, classB);

        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(fatherClass, subClass);

        ontologyConnectionService.ontologyManager.addAxiom(ontology,axiom);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Classes Saved";
    }


    public String saveIndividual(String originalIndividualName, Word wordToSave) {

        deleteIndividual(originalIndividualName);

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        OWLIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIRI + wordToSave.getLema()));

        OWLClass fatherClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + wordToSave.getClasePadre()));

        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);


        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiom);

        saveIndividualProperties(ontology, individual, wordToSave);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Individual Saved";
    }


    public void saveIndividualProperties(OWLOntology ontology, OWLIndividual individual, Word wordToSave) {

        IRI dataTypePropertyIRI = IRI.create(ontologyIRI +"definicion");

        IRI dataTypePropertyExampleIRI = IRI.create(ontologyIRI +"ejemplo");

        IRI dataTypePropertyLemmaRAEIRI = IRI.create(ontologyIRI +"lema_rae");

        IRI dataTypePropertySynonymsIRI = IRI.create(ontologyIRI +"sinonimos");


        OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(dataTypePropertyIRI);

        OWLDataProperty exampleDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyExampleIRI);

        OWLDataProperty lemmaRAEDataProperty = dataFactory.getOWLDataProperty(dataTypePropertyLemmaRAEIRI);

        OWLDataProperty synonymsDataProperty = dataFactory.getOWLDataProperty(dataTypePropertySynonymsIRI);


        OWLDataPropertyAssertionAxiom axiomDefinition = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, wordToSave.getDefinicion());

        OWLDataPropertyAssertionAxiom axiomExample = dataFactory.getOWLDataPropertyAssertionAxiom(exampleDataProperty, individual, wordToSave.getEjemplo());

        OWLDataPropertyAssertionAxiom axiomLemmaRAE = dataFactory.getOWLDataPropertyAssertionAxiom(lemmaRAEDataProperty, individual, wordToSave.getLemaRAE());

        OWLDataPropertyAssertionAxiom axiomSynonyms = dataFactory.getOWLDataPropertyAssertionAxiom(synonymsDataProperty, individual, wordToSave.getSinonimos());


        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomDefinition);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomExample);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomLemmaRAE);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomSynonyms);
    }


    public boolean deleteIndividual(String individualName) {

        boolean deleteConfirmation = false;

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        IRI individualIRI = IRI.create(ontologyIRI + individualName);

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));


        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            if (individualIRI.toString().equals(individual.getIRI().toString())){

                individual.accept(remover);

                deleteConfirmation = true;
            }
        }


        ontologyConnectionService.ontologyManager.applyChanges(remover.getChanges());

        ontologyConnectionService.saveOntologyFile(ontology);

        return deleteConfirmation;
    }


    public List<String> tokenizeTheSentence(String sentence){

        String [] tokens = sentence.split("[\\s'.,:;+-]");

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


        for (Individual individual: individualList) {

            Word wordToSave = new Word();

            if (individual.getLocalName()!= null)
                wordToSave.setLema(individual.getLocalName());

            if (individual.getOntClass() != null)
                wordToSave.setClasePadre(individual.getOntClass().getLocalName());

            RDFNode definitionPropertyValue = individual.getPropertyValue(ontologyConnectionService.definitionProperty);

            if (definitionPropertyValue!= null)
                wordToSave.setDefinicion(definitionPropertyValue.toString());

            RDFNode examplePropertyValue = individual.getPropertyValue(ontologyConnectionService.exampleProperty);

            if (examplePropertyValue!= null)
                wordToSave.setEjemplo(examplePropertyValue.toString());

            RDFNode lemmaRAEPropertyValue = individual.getPropertyValue(ontologyConnectionService.lemmaRAEProperty);

            if (lemmaRAEPropertyValue!= null)
                wordToSave.setLemaRAE(lemmaRAEPropertyValue.toString());

            RDFNode synonymsPropertyValue = individual.getPropertyValue(ontologyConnectionService.synonymsProperty);

            if (synonymsPropertyValue != null)
                wordToSave.setSinonimos(synonymsPropertyValue.toString());

            RDFNode totalAnswersPropertyValue = individual.getPropertyValue(ontologyConnectionService.totalAnswersProperty);

            if (totalAnswersPropertyValue != null)
                wordToSave.setTotalRespuestasN(totalAnswersPropertyValue.toString());

            RDFNode votesQuantityPropertyValue = individual.getPropertyValue(ontologyConnectionService.votesQuantityProperty);

            if (votesQuantityPropertyValue != null)
                wordToSave.setCantidadVotacionesI(votesQuantityPropertyValue.toString());

            RDFNode numberOfAbsencesPropertyValue = individual.getPropertyValue(ontologyConnectionService.numberOfAbsencesProperty);

            if (numberOfAbsencesPropertyValue != null)
                wordToSave.setNumeroDeAusenciasD(numberOfAbsencesPropertyValue.toString());

            RDFNode numberOfPresencesPropertyValue = individual.getPropertyValue(ontologyConnectionService.numberOfPresencesProperty);

            if (numberOfPresencesPropertyValue != null)
                wordToSave.setNumeroDePresenciasA(numberOfPresencesPropertyValue.toString());

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
