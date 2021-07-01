package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWordData;
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


    public SimpleWord getSimpleWordById(long id) {

        return simpleWordRepository.findById(id);
    }


    public List<SimpleWord> getAllSimpleWord(){

        return simpleWordRepository.findAll();
    }


    public List<SimpleWord> getAllSimpleWordByLemma(String word){

        return simpleWordRepository.findAllByWord(word);
    }


    public SimpleWord determineSimpleWordWinner(String word){

        SimpleWord winnerWord = new SimpleWord();

        List<SimpleWord> simpleList = simpleWordRepository.findAllByWord(word);

        int votesQuantity = 0;
        int totalAnswers = simpleList.size();

        for (SimpleWord wordToEvaluate: simpleList) {

            int actualVotesByWord = wordToEvaluate.getVotesQuantity();

            if (actualVotesByWord > votesQuantity){

                votesQuantity = actualVotesByWord;
                winnerWord = wordToEvaluate;
            }
        }

        winnerWord.setTotalAnswers(totalAnswers);
        winnerWord.setVotesQuantity(votesQuantity);

        return winnerWord;
    }


    public SurveyWordData convertSimpleWordToSurveyWord(SimpleWord winnerWord) {

        SurveyWordData surveyWord = new SurveyWordData();

        surveyWord.setLemma(winnerWord.getWord());
        surveyWord.setOriginalDefinition(winnerWord.getWordDefinition());
        surveyWord.setTotalAnswers(winnerWord.getTotalAnswers());
        surveyWord.setVotesQuantity(winnerWord.getVotesQuantity());
        surveyWord.setFatherClass("Temporal");
        surveyWord.setExample("n/a");
        surveyWord.setLemmaRAE("n/a");
        surveyWord.setDefinitionRAE("n/a");
        surveyWord.setSynonyms("n/a");

        return surveyWord;
    }
}
