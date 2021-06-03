package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
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

        var owlClass = dataFactory.getOWLClass(IRI.create(prefixManager.getDefaultPrefix(), className));

        var individualSet = ontologyConnectionService.getHermitReasoner().getInstances(owlClass, false).getFlattened();

        return convertFromSetToStringList(individualSet);
    }


    public List<String> convertFromSetToStringList(Set<OWLNamedIndividual> dataSet){

        List<String> individualNameList = new ArrayList<>();

        for (var individual : dataSet) {

            //El defaultPrefixManager se utiliza aqui para de esta forma poder obtener el nombre corto del individual
            individualNameList.add(prefixManager.getShortForm(individual));
        }

        return individualNameList;
    }


    public List<String> getAllClassesLocalName(){

        List<String> classListNames = new ArrayList<>();

        var classesIterator = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().listClasses();

        while (classesIterator.hasNext()) {

            var nextClass = classesIterator.next();

            classListNames.add(nextClass.getLocalName());
        }

        return classListNames;
    }


    public List<String> getAllIndividualLocalName(){

        List<String> individualNamesList = new ArrayList<>();

        var individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().listIndividuals();

        while (individualsIterator.hasNext()) {

            var individual = individualsIterator.next();

            individualNamesList.add(individual.getLocalName());
        }

        return individualNamesList;
    }


    public List<Individual> getAllIndividualsByFatherClassName(String fatherClassName){

        List<Individual> individualList = new ArrayList<>();

        var individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().listIndividuals();

        while (individualsIterator.hasNext()) {

            var individual = individualsIterator.next();

            if (individual.getOntClass().getLocalName().equalsIgnoreCase(fatherClassName))
                individualList.add(individual);
        }

        return individualList;
    }


    public String saveFatherClassAndSubClass(String fatherClassName, String subClassName) {

        var ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        //Aqui puedo agregar clases nuevas que la api las llama axiomas
        var fatherClass = dataFactory.getOWLClass(createIRIByPropertyName(fatherClassName));
        var subClass = dataFactory.getOWLClass(createIRIByPropertyName(subClassName));

       // OWLAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classA, classB);

        var axiom = dataFactory.getOWLSubClassOfAxiom(fatherClass, subClass);

        ontologyConnectionService.ontologyManager.addAxiom(ontology,axiom);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Classes Saved";
    }


    public String saveIndividual(String originalIndividualName, Word wordToSave) {

        deleteIndividual(originalIndividualName);

        var ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        var individual = dataFactory.getOWLNamedIndividual(createIRIByPropertyName(wordToSave.getLema()));

        var fatherClass = dataFactory.getOWLClass(createIRIByPropertyName(wordToSave.getClasePadre()));

        var axiom = dataFactory.getOWLClassAssertionAxiom(fatherClass, individual);

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiom);

        saveIndividualProperties(ontology, individual, wordToSave);

        ontologyConnectionService.saveOntologyFile(ontology);

        return "Individual Saved";
    }


    public void saveIndividualProperties(OWLOntology ontology, OWLIndividual individual, Word wordToSave) {

        var dataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("definicion"));
        var exampleDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("ejemplo"));
        var lemmaRAEDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("lema_rae"));
        var synonymsDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("sinonimos"));
        var totalAnswersDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("total_respuestas_N"));
        var votesQuantityDataProperty = dataFactory.getOWLDataProperty(createIRIByPropertyName("cantidad_votaciones_I"));

        var axiomDefinition = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, wordToSave.getDefinicion());
        var axiomExample = dataFactory.getOWLDataPropertyAssertionAxiom(exampleDataProperty, individual, wordToSave.getEjemplo());
        var axiomLemmaRAE = dataFactory.getOWLDataPropertyAssertionAxiom(lemmaRAEDataProperty, individual, wordToSave.getLemaRAE());
        var axiomSynonyms = dataFactory.getOWLDataPropertyAssertionAxiom(synonymsDataProperty, individual, wordToSave.getSinonimos());
        var axiomTotalAnswers = dataFactory.getOWLDataPropertyAssertionAxiom(totalAnswersDataProperty, individual, wordToSave.getTotalRespuestasN());
        var axiomVotesQuantity = dataFactory.getOWLDataPropertyAssertionAxiom(votesQuantityDataProperty, individual, wordToSave.getCantidadVotacionesI());

        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomDefinition);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomExample);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomLemmaRAE);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomSynonyms);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomTotalAnswers);
        ontologyConnectionService.ontologyManager.addAxiom(ontology, axiomVotesQuantity);
    }


    public boolean deleteIndividual(String individualName) {

        boolean deleteConfirmation = false;

        var ontology = ontologyConnectionService.loadTheOntologyOwlAPI();

        var individualIRI = createIRIByPropertyName(individualName);

        var remover = new OWLEntityRemover(Collections.singleton(ontology));

        for (var individual : ontology.getIndividualsInSignature()) {

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

        var individualsIterator = ontologyConnectionService.readOntologyFileAndReturnTheJenaModel().listIndividuals();

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
        var cleanIndividual = StringUtils.stripAccents(individual.getLocalName());

        for (String word: sentenceByWords) {

            var cleanWord = StringUtils.stripAccents(word);

            if (cleanIndividual.equalsIgnoreCase(cleanWord) && avoidRepeatIndividualCount == 0 || searchType.equals("word-search") & cleanIndividual.matches(".*"+cleanWord.toLowerCase()+".*") && avoidRepeatIndividualCount == 0){

                individualList.add(individual);

                avoidRepeatIndividualCount++;
            }
        }
    }


    public List<Word> saveAllIndividualPropertiesValueInAWordList(List<Individual> individualList){

        List<Word> wordList = new ArrayList<>();

        for (Individual individual: individualList) {

            var wordToSave = new Word();

            if (individual.getLocalName()!= null)
                wordToSave.setLema(individual.getLocalName());

            if (individual.getOntClass() != null)
                wordToSave.setClasePadre(individual.getOntClass().getLocalName());

            var definitionPropertyValue = individual.getPropertyValue(ontologyConnectionService.definitionProperty);

            if (definitionPropertyValue!= null)
                wordToSave.setDefinicion(definitionPropertyValue.toString());

            var examplePropertyValue = individual.getPropertyValue(ontologyConnectionService.exampleProperty);

            if (examplePropertyValue!= null)
                wordToSave.setEjemplo(examplePropertyValue.toString());

            var lemmaRAEPropertyValue = individual.getPropertyValue(ontologyConnectionService.lemmaRAEProperty);

            if (lemmaRAEPropertyValue!= null)
                wordToSave.setLemaRAE(lemmaRAEPropertyValue.toString());

            var synonymsPropertyValue = individual.getPropertyValue(ontologyConnectionService.synonymsProperty);

            if (synonymsPropertyValue != null)
                wordToSave.setSinonimos(synonymsPropertyValue.toString());

            var totalAnswersPropertyValue = individual.getPropertyValue(ontologyConnectionService.totalAnswersProperty);

            if (totalAnswersPropertyValue != null)
                wordToSave.setTotalRespuestasN(totalAnswersPropertyValue.toString());

            var votesQuantityPropertyValue = individual.getPropertyValue(ontologyConnectionService.votesQuantityProperty);

            if (votesQuantityPropertyValue != null)
                wordToSave.setCantidadVotacionesI(votesQuantityPropertyValue.toString());

            wordList.add(wordToSave);
        }

        return wordList;
    }
}
