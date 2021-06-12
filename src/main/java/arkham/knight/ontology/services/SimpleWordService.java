package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.repositories.SimpleWordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimpleWordService {

    private final SimpleWordRepository simpleWordRepository;

    public SimpleWordService(SimpleWordRepository simpleWordRepository) {
        this.simpleWordRepository = simpleWordRepository;
    }


    public void saveSimpleWord(SimpleWord simpleWord){

        simpleWordRepository.save(simpleWord);
    }


    public List<SimpleWord> getAllSimpleWord(){

        return simpleWordRepository.findAll();
    }


    public List<SimpleWord> getAllSimpleWordByWord(String word){

        return simpleWordRepository.findAllByWord(word);
    }


    public void calculateVotesQuantityByWord(SimpleWord word){

        int totalVotes;

        List<SimpleWord> simpleWords = simpleWordRepository.findAllByWord(word.getWord());

        for (SimpleWord wordToEvaluate : simpleWords) {

             totalVotes = simpleWordRepository.findAllByWordDefinition(wordToEvaluate.getWordDefinition()).size();

             wordToEvaluate.setVotesQuantity(totalVotes);

             simpleWordRepository.save(wordToEvaluate);
        }
    }


    public SimpleWord determineSimpleWordWinner(String word){

        SimpleWord winnerWord = new SimpleWord();

        List<SimpleWord> simpleList = simpleWordRepository.findAllByWord(word);

        int votesQuantity = 0;
        int totalAnswers = simpleList.size();

        for (SimpleWord wordToEvaluate: simpleList) {

            int actualVotesByWord = simpleWordRepository.findAllByWordDefinition(wordToEvaluate.getWordDefinition()).size();

            if (actualVotesByWord > votesQuantity){

                votesQuantity = actualVotesByWord;
                winnerWord = wordToEvaluate;
            }
        }

        winnerWord.setTotalAnswers(totalAnswers);
        winnerWord.setVotesQuantity(votesQuantity);

        return winnerWord;
    }


    public Word convertSimpleWordToWord(SimpleWord winnerWord) {

        Word wordToSaveInOntology = new Word();

        wordToSaveInOntology.setLema(winnerWord.getWord());
        wordToSaveInOntology.setDefinicion(winnerWord.getWordDefinition());
        wordToSaveInOntology.setTotalRespuestasN(String.valueOf(winnerWord.getTotalAnswers()));
        wordToSaveInOntology.setCantidadVotacionesI(String.valueOf(winnerWord.getVotesQuantity()));
        wordToSaveInOntology.setClasePadre("n/a");
        wordToSaveInOntology.setEjemplo("n/a");
        wordToSaveInOntology.setLemaRAE("n/a");
        wordToSaveInOntology.setSinonimos("n/a");

        return wordToSaveInOntology;
    }
}
