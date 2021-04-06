package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.repositories.SurveyWordDataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SurveyWordDataService {

    final SurveyWordDataRepository surveyWordDataRepository;

    public SurveyWordDataService(SurveyWordDataRepository surveyWordDataRepository) {
        this.surveyWordDataRepository = surveyWordDataRepository;
    }


    public void saveSurvey(SurveyWordData surveyWordDataToSave){

        surveyWordDataRepository.save(surveyWordDataToSave);
    }


    public List<SurveyWordData> getAllSurveys(){

        return surveyWordDataRepository.findAll();
    }


    public List<SurveyWordData> getAllSurveysByLemma(String lemma){

        return surveyWordDataRepository.findAllByLemma(lemma);
    }


    public SurveyWordData determineSurveysDataByLemmaAndReturnSurveyWord(String lemma){

        SurveyWordData winnerSurveyWordData = new SurveyWordData();

        int votesQuantity = 0;

        List<SurveyWordData> surveyWordDataList = surveyWordDataRepository.findAllByLemma(lemma);

        int totalAnswers = surveyWordDataList.size();

        for (SurveyWordData wordToEvaluate: surveyWordDataList) {

            int actualVotesByLemmaRAE = surveyWordDataRepository.findAllByLemmaRAE(wordToEvaluate.getLemmaRAE()).size();

            if (actualVotesByLemmaRAE > votesQuantity){
                votesQuantity = actualVotesByLemmaRAE;
                winnerSurveyWordData = wordToEvaluate;
            }
        }

        winnerSurveyWordData.setTotalAnswers(totalAnswers);
        winnerSurveyWordData.setVotesQuantity(votesQuantity);

        return winnerSurveyWordData;
    }


    public SurveyWordData getSurveyById(long id){

        return surveyWordDataRepository.findSurveyWordDataById(id);
    }


    public void deleteSurveyById(long id){

        surveyWordDataRepository.deleteById(id);
    }


    public void deleteAllSurveys(){

        surveyWordDataRepository.deleteAll();
    }
}
