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


    public List<SimpleWord> getAllSimpleWord(){

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


    public boolean alreadyVoteSimpleWordWithTheSameLemmaAndDifferentDefinition(SimpleWord simpleWordToEvaluate, String actualIpAddress){

        List<SimpleWord> wordsByLemma = getAllSimpleWordByLemma(simpleWordToEvaluate.getWord());

        for (SimpleWord simpleWord : wordsByLemma) {

            boolean hasSameDefinition = simpleWordToEvaluate.getWordDefinition().equalsIgnoreCase(simpleWord.getWordDefinition());

            if (!hasSameDefinition && simpleWord.getIpAddresses().contains(actualIpAddress))
                return true;
        }

        return false;
    }


    public SurveyWord convertSimpleWordToSurveyWord(SimpleWord winnerWord) {

        SurveyWord surveyWord = new SurveyWord();

        surveyWord.setLemma(winnerWord.getWord());
        surveyWord.setDefinition(winnerWord.getWordDefinition());
        surveyWord.setTotalAnswers(winnerWord.getTotalAnswers());
        surveyWord.setVotesQuantity(winnerWord.getVotesQuantity());
        surveyWord.setFatherClass("Temporal");
        surveyWord.setExample("n/a");
        surveyWord.setLemmaRAE("n/a");
        surveyWord.setDefinitionRAE("n/a");
        surveyWord.setSynonyms("n/a");

        return surveyWord;
    }


    public void evaluateIfTheWordEntersTheSurvey(SimpleWord simpleWordToVote) {

        SurveyWord surveyWordWinner = convertSimpleWordToSurveyWord(simpleWordToVote);

        boolean wordDefinitionAlreadyExist = surveyWordService.surveyWordDefinitionAlreadyExist(surveyWordWinner);

        if (!wordDefinitionAlreadyExist && surveyWordWinner.getVotesQuantity() > 2){

            surveyWordWinner.setVotesQuantity(0);
            simpleWordToVote.setPassTheVote(true);

            simpleWordRepository.save(simpleWordToVote);
            surveyWordService.saveSurveyWord(surveyWordWinner);
        }
    }
}
