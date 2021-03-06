package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.repositories.SurveyWordRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyWordService {

    private final SurveyWordRepository surveyWordRepository;

    private final WordService wordService;

    private final OntologyService ontologyService;

    public SurveyWordService(SurveyWordRepository surveyWordRepository, WordService wordService, OntologyService ontologyService) {
        this.surveyWordRepository = surveyWordRepository;
        this.wordService = wordService;
        this.ontologyService = ontologyService;
    }


    public void saveSurveyWord(SurveyWord surveyWordToSave){

        surveyWordRepository.save(surveyWordToSave);
    }


    public List<SurveyWord> getAllSurveys(){

        return surveyWordRepository.findAll();
    }


    public List<SurveyWord> getAllSurveysByLemma(String lemma){

        List<SurveyWord> wordsByLemma = new ArrayList<>();

        List<SurveyWord> words = surveyWordRepository.findAll();

        for (SurveyWord surveyWord : words) {

            if (surveyWord.getLemma().equalsIgnoreCase(lemma))
                wordsByLemma.add(surveyWord);
        }

        return wordsByLemma;
    }


    public List<SurveyWord> getAllSurveysByLemmaRAE(String lemmaRAE){

        return surveyWordRepository.findAllByLemmaRAE(lemmaRAE);
    }


    public SurveyWord getSurveyWordById(long id) {

        return surveyWordRepository.findSurveyWordById(id);
    }


    public Word convertSurveyWordToWord(SurveyWord surveyWord){

        String totalAnswers = String.valueOf(surveyWord.getTotalAnswers());
        String votesQuantity = String.valueOf(surveyWord.getVotesQuantity());

        return new Word(surveyWord.getLemma(), surveyWord.getDefinition(), surveyWord.getExample(), surveyWord.getFatherClass(), surveyWord.getSynonyms(), surveyWord.getLemmaRAE(), totalAnswers, votesQuantity);
    }


    public boolean surveyWordDefinitionAlreadyExist(SurveyWord surveyWordToEvaluate){

        for (SurveyWord surveyWord : getAllSurveysByLemma(surveyWordToEvaluate.getLemma())) {
            //si hay tan solo 1 palabra con el mismo lemma que tenga la misma definicion es suficiente para retornar true
            if (surveyWordToEvaluate.getDefinition().equalsIgnoreCase(surveyWord.getDefinition()))
                return true;
        }

        return false;
    }


    public SurveyWord determineSurveyWordWinner(String lemma){

        SurveyWord winnerSurveyWord = new SurveyWord();

        List<SurveyWord> surveyWordByLemma = getAllSurveysByLemma(lemma);

        int votesQuantity = 0;

        for (SurveyWord wordToEvaluate: surveyWordByLemma) {

            int actualVotes = wordToEvaluate.getVotesQuantity();

            if (actualVotes > votesQuantity) {

                votesQuantity = actualVotes;
                winnerSurveyWord = wordToEvaluate;
            }
        }

        winnerSurveyWord.setTotalAnswers(surveyWordByLemma.size());
        winnerSurveyWord.setVotesQuantity(votesQuantity);

        return winnerSurveyWord;
    }

    public boolean alreadyVoteSurveyWordWithTheSameLemmaAndDifferentDefinition(SurveyWord surveyWordToEvaluate, String actualIpAddress){

        List<SurveyWord> wordsByLemma = getAllSurveysByLemma(surveyWordToEvaluate.getLemma());

        for (SurveyWord surveyWord : wordsByLemma) {

            boolean hasSameDefinition = surveyWordToEvaluate.getDefinition().equalsIgnoreCase(surveyWord.getDefinition());

            if (!hasSameDefinition && surveyWord.getIpAddresses().contains(actualIpAddress))
                return true;
        }

        return false;
    }


    public void evaluateIfTheWordEntersTheOntology(SurveyWord surveyWordToVote) {

        SurveyWord surveyWordWinner = determineSurveyWordWinner(surveyWordToVote.getLemma());

        Word wordWinner = convertSurveyWordToWord(surveyWordWinner);

        int votesQuantity = Integer.parseInt(wordWinner.getCantidadVotacionesI());

        float percentageAgreement = wordService.calculateWordPercentageAgreement(wordWinner);

        if (percentageAgreement > 40 && votesQuantity > 2)
            ontologyService.saveIndividual(wordWinner.getLema(), wordWinner);
    }
}
