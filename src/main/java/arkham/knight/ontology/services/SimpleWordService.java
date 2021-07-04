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


    public SimpleWord getSimpleWordById(long id) {

        return simpleWordRepository.findById(id);
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


    public boolean alreadyVoteSimpleWordWithTheSameLemma(String lemma){

        List<SimpleWord> wordsByLemma = getAllSimpleWordByLemma(lemma);

        for (SimpleWord simpleWordToEvaluate : wordsByLemma) {

            if (simpleWordToEvaluate.getVotesQuantity() > 0)
                return true;
        }

        return false;
    }


    public SimpleWord determineSimpleWordWinner(String word){

        SimpleWord winnerWord = new SimpleWord();

        List<SimpleWord> simpleList = getAllSimpleWordByLemma(word);

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


    public void voteSimpleWord(String actualIpAddress, SimpleWord simpleWordToEdit) {

        simpleWordToEdit.setIpAddresses(actualIpAddress);

        simpleWordToEdit.setVotesQuantity(simpleWordToEdit.getVotesQuantity() + 1);

        simpleWordRepository.save(simpleWordToEdit);

        SimpleWord simpleWordWinner = determineSimpleWordWinner(simpleWordToEdit.getWord());

        SurveyWord surveyWordWinner = convertSimpleWordToSurveyWord(simpleWordWinner);

        boolean wordExist = surveyWordService.surveyWordAlreadyExist(surveyWordWinner.getLemma());

        float percentageAgreement = surveyWordService.calculateSurveyWordPercentageAgreement(surveyWordWinner);

        if (!wordExist && percentageAgreement > 40 && surveyWordWinner.getVotesQuantity() > 2){

            surveyWordWinner.setVotesQuantity(0);
            surveyWordWinner.setTotalAnswers(0);

            surveyWordService.saveSurveyWord(surveyWordWinner);
        }
    }
}
