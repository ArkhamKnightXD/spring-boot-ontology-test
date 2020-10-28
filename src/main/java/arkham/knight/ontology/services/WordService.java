package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.jena.ontology.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WordService {

    @Autowired
    private OntologyService ontologyService;


    public List<Word> getAllWords(){

        List<Individual> individualList = ontologyService.getAllIndividualByName(ontologyService.getAllIndividualLocalName(),"tweet");

        return ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);
    }


    public Word getWordByLemma(String lemma){

        for (Word wordToFind: getAllWords()) {

            if (wordToFind.getLema().equals(lemma))
                return wordToFind;
        }

        return null;
    }
}