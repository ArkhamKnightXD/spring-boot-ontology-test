package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.repositories.SimpleWordRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleWordService {

    private final SimpleWordRepository simpleWordRepository;

    private final SurveyWordService surveyWordService;

    public SimpleWordService(SimpleWordRepository simpleWordRepository, SurveyWordService surveyWordService) {
        this.simpleWordRepository = simpleWordRepository;
        this.surveyWordService = surveyWordService;
    }


    public void saveSimpleWord(SimpleWord simpleWord){

        simpleWordRepository.save(simpleWord);
    }


    public List<SimpleWord> getAllSimpleWords(){

        return simpleWordRepository.findAll();
    }


    public List<SimpleWord> getAllSimpleWordByLemma(String lemma){

        List<SimpleWord> wordsByLemma = new ArrayList<>();

        List<SimpleWord> words = simpleWordRepository.findAll();

        for (SimpleWord simpleWord : words) {

            if (simpleWord.getWord().equalsIgnoreCase(lemma))
                wordsByLemma.add(simpleWord);
        }

        return wordsByLemma;
    }


    public SimpleWord getSimpleWordById(long id) {

        return simpleWordRepository.findById(id);
    }


    public boolean alreadyVoteWordWithTheSameLemmaExist(SimpleWord wordToEvaluate, String actualUserName){

        List<SimpleWord> wordsByLemma = getAllSimpleWordByLemma(wordToEvaluate.getWord());

        for (SimpleWord simpleWord : wordsByLemma) {

            boolean hasSameDefinition = wordToEvaluate.getWordDefinition().equalsIgnoreCase(simpleWord.getWordDefinition());

            if (!hasSameDefinition && simpleWord.getAlreadyVoteUsernames().contains(actualUserName))
                return true;
        }

        return false;
    }


    private SurveyWord convertSimpleWordToSurveyWord(SimpleWord winnerWord) {

        SurveyWord surveyWord = new SurveyWord();

        surveyWord.setLemma(winnerWord.getWord());
        surveyWord.setDefinition(winnerWord.getWordDefinition());
        surveyWord.setTotalAnswers(winnerWord.getTotalAnswers());
        surveyWord.setVotesQuantity(winnerWord.getVotesQuantity());
        surveyWord.setFatherClass("Temporal");
        surveyWord.setExample("N/A");
        surveyWord.setLemmaRAE("N/A");
        surveyWord.setDefinitionRAE("N/A");
        surveyWord.setSynonyms("N/A");

        return surveyWord;
    }


    public void evaluateIfTheWordEntersTheSurvey(SimpleWord wordToVote) {

        SurveyWord surveyWordWinner = convertSimpleWordToSurveyWord(wordToVote);

        boolean wordDefinitionAlreadyExist = surveyWordService.surveyWordDefinitionAlreadyExist(surveyWordWinner);

        if (!wordDefinitionAlreadyExist && surveyWordWinner.getVotesQuantity() > 2){

            surveyWordWinner.setVotesQuantity(0);
            wordToVote.setPassTheVote(true);

            simpleWordRepository.save(wordToVote);
            surveyWordService.saveSurveyWord(surveyWordWinner);
        }
    }
}
