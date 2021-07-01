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


    public void saveSurveyWord(SurveyWordData surveyWordDataToSave){

        surveyWordDataRepository.save(surveyWordDataToSave);
    }


    public boolean surveyWordAlreadyExist(String lemma){

        SurveyWordData surveyWordData = surveyWordDataRepository.findByLemma(lemma);

        if (surveyWordData != null)
            return true;

        return false;
    }


    //TODO esta seria lo forma ideal de evaluar las palabras que pasaran a la ontologia, pero falla
    public boolean surveyWordAlreadyExistComplex(SurveyWordData surveyWordData){

        int counter = 0;

        SurveyWordData surveyWordToEvaluate = surveyWordDataRepository.findSurveyWordDataById(surveyWordData.getId());

        List<SurveyWordData> SurveyWordDataList = surveyWordDataRepository.findAllByLemma(surveyWordData.getLemma());

        for (SurveyWordData surveyWord : SurveyWordDataList) {

            if (surveyWord.getOriginalDefinition().equalsIgnoreCase(surveyWordToEvaluate.getOriginalDefinition()))
                counter++;
        }

        if (counter > 0)
            return true;

        return false;
    }


    public List<SurveyWordData> getAllSurveys(){

        return surveyWordDataRepository.findAll();
    }


    public SurveyWordData determineSurveyWordWinner(String lemma){

        SurveyWordData winnerSurveyWordData = new SurveyWordData();

        int votesQuantity = 0;

        List<SurveyWordData> surveyWordDataList = surveyWordDataRepository.findAll();

        int totalAnswers = surveyWordDataRepository.findAllByLemma(lemma).size();

        for (SurveyWordData wordToEvaluate: surveyWordDataList) {

            int actualVotesByLemmaRAE = wordToEvaluate.getVotesQuantity();

            if (actualVotesByLemmaRAE > votesQuantity){

                votesQuantity = actualVotesByLemmaRAE;
                winnerSurveyWordData = wordToEvaluate;
            }
        }

        winnerSurveyWordData.setTotalAnswers(totalAnswers);
        winnerSurveyWordData.setVotesQuantity(votesQuantity);

        return winnerSurveyWordData;
    }


    public List<SurveyWordData> getAllSurveysByLemma(String lemma){

        return surveyWordDataRepository.findAllByLemma(lemma);
    }


    public List<SurveyWordData> getAllSurveysByLemmaRAE(String lemmaRAE){

        return surveyWordDataRepository.findAllByLemmaRAE(lemmaRAE);
    }


    public void deleteSurveyById(long id){

        surveyWordDataRepository.deleteById(id);
    }


    public void deleteAllSurveys(){

        surveyWordDataRepository.deleteAll();
    }

    public SurveyWordData getSurveyWordById(long id) {

        return surveyWordDataRepository.findSurveyWordDataById(id);
    }
}
