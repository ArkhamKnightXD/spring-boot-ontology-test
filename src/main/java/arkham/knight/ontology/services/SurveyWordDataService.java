package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWordData;
import arkham.knight.ontology.repositories.SurveyWordDataRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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


    public HashMap<String, Float> calculateSurveysDataByLemma(String lemma){

        HashMap<String, Float> resultsMap = new HashMap<>();

        float percentageAgreement = 0.f;

        int totalAnswers = 0;

        int votesQuantity = 0;

        int actualVotesByLemmaRAE = 0;

        String actualMostVoteLemmaRae = "";

        List<SurveyWordData> surveyWordDataList = surveyWordDataRepository.findAllByLemma(lemma);

        totalAnswers = surveyWordDataList.size();

        for (SurveyWordData wordToEvaluate: surveyWordDataList) {

            actualVotesByLemmaRAE = surveyWordDataRepository.findAllByLemmaRAE(wordToEvaluate.getLemmaRAE()).size();

            if (actualVotesByLemmaRAE > votesQuantity){
                votesQuantity = actualVotesByLemmaRAE;
                actualMostVoteLemmaRae = wordToEvaluate.getLemmaRAE();
            }
        }

        percentageAgreement = (float) votesQuantity/totalAnswers * 100;

        resultsMap.put(actualMostVoteLemmaRae, percentageAgreement);

        return resultsMap;
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
