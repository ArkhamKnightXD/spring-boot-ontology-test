package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.repositories.SurveyWordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SurveyWordService {

    final SurveyWordRepository surveyWordRepository;

    public SurveyWordService(SurveyWordRepository surveyWordRepository) {
        this.surveyWordRepository = surveyWordRepository;
    }


    public void saveSurveyWord(SurveyWord surveyWordToSave){

        surveyWordRepository.save(surveyWordToSave);
    }


    public SurveyWord getSurveyWordById(long id) {

        return surveyWordRepository.findSurveyWordById(id);
    }


    public boolean surveyWordAlreadyExist(String lemma){

        SurveyWord surveyWord = surveyWordRepository.findByLemma(lemma);

        if (surveyWord != null)
            return true;

        return false;
    }


    //TODO esta seria lo forma ideal de evaluar las palabras que pasaran a la ontologia, pero falla
    public boolean surveyWordAlreadyExistComplex(SurveyWord surveyWordData){

        int counter = 0;

        SurveyWord surveyWordToEvaluate = surveyWordRepository.findSurveyWordById(surveyWordData.getId());

        List<SurveyWord> surveyWordList = surveyWordRepository.findAllByLemma(surveyWordData.getLemma());

        for (SurveyWord surveyWord : surveyWordList) {

            if (surveyWord.getDefinition().equalsIgnoreCase(surveyWordToEvaluate.getDefinition()))
                counter++;
        }

        if (counter > 0)
            return true;

        return false;
    }


    public List<SurveyWord> getAllSurveys(){

        return surveyWordRepository.findAll();
    }


    public SurveyWord determineSurveyWordWinner(String lemma){

        SurveyWord winnerSurveyWord = new SurveyWord();

        int votesQuantity = 0;

        List<SurveyWord> surveyWordList = surveyWordRepository.findAll();

        int totalAnswers = surveyWordRepository.findAllByLemma(lemma).size();

        for (SurveyWord wordToEvaluate: surveyWordList) {

            int actualVotesByLemmaRAE = wordToEvaluate.getVotesQuantity();

            if (actualVotesByLemmaRAE > votesQuantity){

                votesQuantity = actualVotesByLemmaRAE;
                winnerSurveyWord = wordToEvaluate;
            }
        }

        winnerSurveyWord.setTotalAnswers(totalAnswers);
        winnerSurveyWord.setVotesQuantity(votesQuantity);

        return winnerSurveyWord;
    }


    public List<SurveyWord> getAllSurveysByLemma(String lemma){

        return surveyWordRepository.findAllByLemma(lemma);
    }


    public List<SurveyWord> getAllSurveysByLemmaRAE(String lemmaRAE){

        return surveyWordRepository.findAllByLemmaRAE(lemmaRAE);
    }


    public void deleteSurveyById(long id){

        surveyWordRepository.deleteById(id);
    }


    public void deleteAllSurveys(){

        surveyWordRepository.deleteAll();
    }
}
