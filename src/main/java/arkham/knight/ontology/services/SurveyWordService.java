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


    public SurveyWord getSurveyWordById(long id) {

        return surveyWordRepository.findSurveyWordById(id);
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


    public Word convertSurveyWordToWord(SurveyWord surveyWord){

        String totalAnswers = String.valueOf(surveyWord.getTotalAnswers());
        String votesQuantity = String.valueOf(surveyWord.getVotesQuantity());

        return new Word(surveyWord.getLemma(), surveyWord.getDefinition(), surveyWord.getExample(), surveyWord.getFatherClass(), surveyWord.getSynonyms(), surveyWord.getLemmaRAE(), totalAnswers, votesQuantity);
    }


    public float calculateSurveyWordPercentageAgreement(SurveyWord wordToEvaluate){

        int votesQuantity = wordToEvaluate.getVotesQuantity();
        int totalAnswers = wordToEvaluate.getTotalAnswers();

        return (float) votesQuantity/totalAnswers * 100;
    }


    public boolean surveyWordAlreadyExist(String lemma){

        SurveyWord surveyWord = surveyWordRepository.findByLemma(lemma);

        if (surveyWord != null)
            return true;

        return false;
    }


    //TODO esta seria lo forma ideal de evaluar las palabras que pasaran a la ontologia, pero falla
    public boolean surveyWordAlreadyExistComplex(SurveyWord surveyWord){

        int counter = 0;

        //Aqui falla debido a que el surveyWord Que llega no tiene id, el id se asigna cuando se salva la palabra
        SurveyWord surveyWordToEvaluate = surveyWordRepository.findSurveyWordById(surveyWord.getId());

        List<SurveyWord> surveyWordList = getAllSurveysByLemma(surveyWord.getLemma());

        for (SurveyWord surveyWordToIterate : surveyWordList) {

            if (surveyWordToIterate.getDefinition().equalsIgnoreCase(surveyWordToIterate.getDefinition()))
                counter++;
        }

        if (counter > 0)
            return true;

        return false;
    }


    public SurveyWord determineSurveyWordWinner(String lemma){

        SurveyWord winnerSurveyWord = new SurveyWord();

        int votesQuantity = 0;

        List<SurveyWord> surveyWordList = surveyWordRepository.findAll();

        int totalAnswers = getAllSurveysByLemma(lemma).size();

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

    public boolean alreadyVoteSurveyWordWithTheSameLemma(String lemma) {

        List<SurveyWord> wordsByLemma = getAllSurveysByLemma(lemma);

        for (SurveyWord surveyWord : wordsByLemma) {

            if (surveyWord.getVotesQuantity() > 0)
                return true;
        }

        return false;
    }


    public void voteSurveyWord(String actualIpAddress, SurveyWord surveyWordToVote) {

        surveyWordToVote.setIpAddresses(actualIpAddress);

        surveyWordToVote.setVotesQuantity(surveyWordToVote.getVotesQuantity() + 1);

        surveyWordRepository.save(surveyWordToVote);

        SurveyWord surveyWordWinner = determineSurveyWordWinner(surveyWordToVote.getLemma());

        Word wordWinner = convertSurveyWordToWord(surveyWordWinner);

//        boolean wordExist = surveyWordDataService.surveyWordAlreadyExist(wordWinner.getLemma());

        int votesQuantity = Integer.parseInt(wordWinner.getCantidadVotacionesI());

        float percentageAgreement = wordService.calculateWordPercentageAgreement(wordWinner);

        if (percentageAgreement > 40 && votesQuantity > 2)
            ontologyService.saveIndividual(wordWinner.getLema(), wordWinner);
    }
}
