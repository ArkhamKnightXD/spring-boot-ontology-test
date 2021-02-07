package arkham.knight.ontology;

import arkham.knight.ontology.models.DRAEObject;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.services.DRAEConnectionService;
import arkham.knight.ontology.services.OntologyConnectionService;
import arkham.knight.ontology.services.OntologyService;
import arkham.knight.ontology.services.WordService;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OntologyApplicationTests {

    @Autowired
    DRAEConnectionService draeConnectionService;

    @Autowired
    OntologyService ontologyService;

    @Autowired
    WordService wordService;

    @Autowired
    OntologyConnectionService ontologyConnectionService;

    @Autowired
    RestTemplate restTemplate;

    final String testLemma = "prueba";


//    @Test
//    void testDRAESearch() {
//
//        String wordExpected = "diccionario";
//
//        String wordFound = "";
//
//        List<DRAEObject> wordsResponse = draeConnectionService.getTheWordDataFromDRAE(restTemplate, wordExpected);
//
//        for (DRAEObject word: wordsResponse) {
//
//            wordFound =  word.getWord();
//        }
//
//        assertEquals(wordExpected ,wordFound);
//    }


    @Test
    void testGetOntologyData() {

        String response = ontologyConnectionService.getOntology().getURI().concat("#");

        String expectedResponse = ontologyConnectionService.ontologyURI;

        assertEquals(expectedResponse, response);
    }


    @Test
    void testClassesCreation() {

        String response = ontologyService.saveFatherClassAndSubClass("test1", "test2");

        assertEquals("Classes Saved1", response);
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

        Word wordToSave = new Word(testLemma, "definition", "example", "fatherClassName", "synonyms", "individualNameRAE");

        String response = ontologyService.saveIndividual(testLemma, wordToSave);

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
}
