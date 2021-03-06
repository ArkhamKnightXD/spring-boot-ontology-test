package arkham.knight.ontology;

import arkham.knight.ontology.models.DRAEDefinition;
import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.DRAEVariation;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OntologyApplicationTests {

    private final OntologyConnectionService ontologyConnectionService = OntologyConnectionService.getInstance();

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private WordService wordService;

    @Autowired
    private DRAEConnectionService draeConnectionService;

    @Autowired
    private RestTemplate restTemplate;

    private final String testLemma = "prueba";

    private final String wordToSearchDRAE = "diccionario";


    @Test
    void testHermitReasoner() {

        List<String> individualNames = ontologyService.getAllIndividualNameByClassNameWithReasoner("Verbos");

        assertFalse(individualNames.contains("fuñir"));
    }


    @Test
    void testCalculateWordPercentageAgreement() {

        Word testWord = wordService.getWordByLemma("apota");

        float response = wordService.calculateWordPercentageAgreement(testWord);

        assertEquals(40 ,response);
    }


    @Test
    void testCalculateWordPercentageAgreementOfPresence() {

        Word testWord = wordService.getWordByLemma("apota");

        float response = wordService.calculateWordPercentageAgreementOfPresenceOrAbsents(testWord, true);

        assertEquals(67, Math.round(response));
    }


    @Test
    void testCalculateWordPercentageAgreementOfAbsences() {

        Word testWord = wordService.getWordByLemma("apota");

        float response = wordService.calculateWordPercentageAgreementOfPresenceOrAbsents(testWord, false);

        assertEquals(67, Math.round(response));
    }


    @Test
    void testCalculateWordMeanPercentageAgreement() {

        Word testWord = wordService.getWordByLemma("apota");

        float response = wordService.calculateWordMeanPercentageAgreement(testWord);

        assertEquals(67, Math.round(response));
    }


    @Test
    void testGetAllClassesLocalName() {

        List<String> classNameList = ontologyService.getAllClassesLocalName();

        assertFalse(classNameList.contains("bobolongo"));
    }


    @Test
    void testGetAllClasses() {

        List<OntClass> classList = ontologyConnectionService.getAllClasses();

        assertFalse(classList.isEmpty());
    }


    @Test
    void testGetAllIndividuals() {

        List<Individual> individualList = ontologyConnectionService.getAllIndividuals();

        assertFalse(individualList.isEmpty());
    }


    @Test
    void testGetAllDataTypeProperties() {

        List<DatatypeProperty> datatypePropertyList = ontologyConnectionService.getAllDataTypeProperties();

        assertFalse(datatypePropertyList.isEmpty());
    }


    @Test
    void testGetOntologyData() {

        String response = ontologyConnectionService.ontologyURI;

        String expectedResponse = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";

        assertEquals(expectedResponse, response);
    }


    @Test
    void testClassesCreation() {

        String response = ontologyService.saveFatherClassAndSubClass("test1", "test2");

        assertEquals("Classes Saved", response);
    }


    @Test
    void testTweetSearch() {

        String individualTestName = "";

        final String textExample = "soy un bobolongo que come yuca";

        List<String> sentenceByWords = ontologyService.tokenizeTheSentence(textExample);

        List<Individual> individualList = ontologyService.getAllIndividualByName(sentenceByWords, "tweet-search");

        for (Individual individual: individualList) {

            individualTestName =  individual.getLocalName();
        }

        assertEquals("bobolongo", individualTestName);
    }


    @Test
    void testIndividualCreation() {

        Word wordToSave = new Word(testLemma, "definition", "example", "fatherClassName", "synonyms", "individualNameRAE", "0", "0");

        String response = ontologyService.saveIndividual(testLemma, wordToSave);

        assertEquals("Individual Saved", response);
    }


    @Test
    void testIndividualEdition() {

        String testLemma2 = "prueba1";

        Word wordToSave = new Word(testLemma2, "definition331", "example331", "fatherClassName331", "synonyms331", "individualNameRAE331", "1", "1");

        String response = ontologyService.saveIndividual(testLemma2, wordToSave);

        assertEquals("Individual Saved", response);
    }


    @Test
    void testGetWordByLemma() {

        Word response = wordService.getWordByLemma(testLemma);

        assertEquals(testLemma, response.getLema());
    }


    @Test
    void testGetAllWords() {

        List<Word> response = wordService.getAllWords();

        assertFalse(response.isEmpty());
    }


    @Test
    void testGetAllWordByFatherClassName() {

        List<Word> response = wordService.getAllWordsByFatherClassName("Adjetivos");

        assertFalse(response.isEmpty());
    }


    @Test
    void testDeleteIndividual() {

        boolean response = ontologyService.deleteIndividual(testLemma);

        assertTrue(response);
    }


//    @Test
//    void testDRAEObjectSearch() {
//
//        String wordFound = "";
//
//        List<DRAEObject> wordsResponse = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordToSearchDRAE);
//
//        for (DRAEObject word: wordsResponse) {
//
//            wordFound =  word.getWord();
//        }
//
//        assertEquals(wordToSearchDRAE ,wordFound);
//    }
//
//
//    @Test
//    void testDRAEDefinitionsSearch() {
//
//        List<DRAEObject> wordList = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordToSearchDRAE);
//
//        List<DRAEDefinition> definitions = draeConnectionService.getAllDefinitionsFromDRAEWordList(wordList);
//
//        assertFalse(definitions.isEmpty());
//    }
//
//
//    @Test
//    void testDRAEVariationsSearch() {
//
//        List<DRAEObject> wordList = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordToSearchDRAE);
//
//        List<DRAEVariation> variations = draeConnectionService.getAllVariationsFromDRAEWordList(wordList);
//
//        assertFalse(variations.isEmpty());
//    }
}
