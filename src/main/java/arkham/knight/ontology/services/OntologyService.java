package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OntologyService {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    private final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

    private final DefaultPrefixManager prefixManager = new DefaultPrefixManager(null, null, ontologyConnectionService.ontologyURI);


    public IRI createIRIByPropertyName (String propertyName){

        return IRI.create(ontologyConnectionService.ontologyURI.concat(propertyName));
    }


    public List<String> getAllIndividualNameByClassNameWithReasoner(String className){

        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(prefixManager.getDefaultPrefix(), className));

        Set<OWLNamedIndividual> individualSet = ontologyConnectionService.getHermitReasoner().getInstances(owlClass, false).getFlattened();

        return convertFromSetToStringList(individualSet);
    }


    public List<String> convertFromSetToStringList(Set<OWLNamedIndividual> dataSet){

        List<String> individualNameList = new ArrayList<>();

        for (OWLNamedIndividual individual : dataSet) {
            //El defaultPrefixManager se utiliza aqui para de esta forma poder obtener el nombre corto del individual
            individualNameList.add(prefixManager.getShortForm(individual));
        }

        return individualNameList;
    }


    public List<String> getAllClassesLocalName(){

        List<String> classListNames = new ArrayList<>();

        List<OntClass> ontClasses = ontologyConnectionService.getAllClasses();

        for (OntClass ontClass : ontClasses) {

            classListNames.add(ontClass.getLocalName());
        }

        return classListNames;
    }


    public List<String> getAllIndividualLocalName(){

        List<String> individualNamesList = new ArrayList<>();

        List<Individual> individuals = ontologyConnectionService.getAllIndividuals();

        for (Individual individual : individuals) {

            individualNamesList.add(individual.getLocalName());
        }

        return individualNamesList;
    }


    public List<Individual> getAllIndividualsByFatherClassName(String fatherClassName){

        List<Individual> individualsByFatherClassName = new ArrayList<>();

        List<Individual> allIndividuals = ontologyConnectionService.getAllIndividuals();

        for (Individual individual : allIndividuals) {

            if (individual.getOntClass().getLocalName().equalsIgnoreCase(fatherClassName))
                individualsByFatherClassName.add(individual);
        }

        return individualsByFatherClassName;
    }


    public String saveFatherClassAndSubClass(String fatherClassName, String subClassName) {

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();
        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        OWLClass fatherClass = dataFactory.getOWLClass(createIRIByPropertyName(fatherClassName));
        OWLClass subClass = dataFactory.getOWLClass(createIRIByPropertyName(subClassName));

       // OWLAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classA, classB);

        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(fatherClass, subClass);

        ontologyConnectionService.ontologyManager.addAxiom(ontology,axiom);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Classes Saved";
    }


    public String saveIndividual(String originalIndividualName, Word wordToSave) {

        deleteIndividual(originalIndividualName);

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        OWLIndividual individual = dataFactory.getOWLNamedIndividual(createIRIByPropertyName(wordToSave.getLema()));

        OWLClass fatherClass = dataFactory.getOWLClass(createIRIByPropertyName(wordToSave.getClasePadre()));

        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiom);

        saveIndividualProperties(ontology, individual, wordToSave);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Individual Saved";
    }


    public void saveIndividualProperties(OWLOntology ontology, OWLIndividual individual, Word wordToSave) {

        OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("definicion"));
        OWLDataProperty exampleDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("ejemplo"));
        OWLDataProperty lemmaRAEDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("lema_rae"));
        OWLDataProperty synonymsDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("sinonimos"));
        OWLDataProperty totalAnswersDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("total_respuestas_N"));
        OWLDataProperty votesQuantityDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("cantidad_votaciones_I"));

        OWLDataPropertyAssertionAxiom axiomDefinition = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, wordToSave.getDefinicion());
        OWLDataPropertyAssertionAxiom axiomExample = dataFactory.getOWLDataPropertyAssertionAxiom(exampleDataProperty, individual, wordToSave.getEjemplo());
        OWLDataPropertyAssertionAxiom axiomLemmaRAE = dataFactory.getOWLDataPropertyAssertionAxiom(lemmaRAEDataProperty, individual, wordToSave.getLemaRAE());
        OWLDataPropertyAssertionAxiom axiomSynonyms = dataFactory.getOWLDataPropertyAssertionAxiom(synonymsDataProperty, individual, wordToSave.getSinonimos());
        OWLDataPropertyAssertionAxiom axiomTotalAnswers = dataFactory.getOWLDataPropertyAssertionAxiom(totalAnswersDataProperty, individual, wordToSave.getTotalRespuestasN());
        OWLDataPropertyAssertionAxiom axiomVotesQuantity = dataFactory.getOWLDataPropertyAssertionAxiom(votesQuantityDataProperty, individual, wordToSave.getCantidadVotacionesI());

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomDefinition);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomExample);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomLemmaRAE);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomSynonyms);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomTotalAnswers);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomVotesQuantity);
    }


    public boolean deleteIndividual(String individualName) {

        boolean deleteConfirmation = false;

        OWLOntology ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        IRI individualIRI = createIRIByPropertyName(individualName);

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

        List<Individual> filteredIndividual = new ArrayList<>();

        List<Individual> allIndividuals = ontologyConnectionService.getAllIndividuals();

        for (Individual individual : allIndividuals) {

            int avoidRepeatIndividualCount = 0;

            compareAllWordsInTheWordListAndSaveInTheIndividualList(searchType, sentenceByWords, filteredIndividual, avoidRepeatIndividualCount, individual);
        }

        return filteredIndividual;
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

            RDFNode definitionPropertyValue = individual.getPropertyValue(ontologyConnectionService.definitionProperty);
            RDFNode examplePropertyValue = individual.getPropertyValue(ontologyConnectionService.exampleProperty);
            RDFNode lemmaRAEPropertyValue = individual.getPropertyValue(ontologyConnectionService.lemmaRAEProperty);
            RDFNode synonymsPropertyValue = individual.getPropertyValue(ontologyConnectionService.synonymsProperty);
            RDFNode totalAnswersPropertyValue = individual.getPropertyValue(ontologyConnectionService.totalAnswersProperty);
            RDFNode votesQuantityPropertyValue = individual.getPropertyValue(ontologyConnectionService.votesQuantityProperty);

            Word wordToSave = new Word();

            wordToSave.setLema(individual.getLocalName());
            wordToSave.setDefinicion(definitionPropertyValue.toString());
            wordToSave.setClasePadre(individual.getOntClass().getLocalName());

            if (examplePropertyValue != null)
                wordToSave.setEjemplo(examplePropertyValue.toString());

            if (lemmaRAEPropertyValue != null)
                wordToSave.setLemaRAE(lemmaRAEPropertyValue.toString());

            if (synonymsPropertyValue != null)
                wordToSave.setSinonimos(synonymsPropertyValue.toString());

            if (totalAnswersPropertyValue != null)
                wordToSave.setTotalRespuestasN(totalAnswersPropertyValue.toString());

            if (votesQuantityPropertyValue != null)
                wordToSave.setCantidadVotacionesI(votesQuantityPropertyValue.toString());

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
