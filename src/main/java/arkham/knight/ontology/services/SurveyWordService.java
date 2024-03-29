package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWord;
import arkham.knight.ontology.models.Word;
import arkham.knight.ontology.repositories.SurveyWordRepository;
import org.springframework.data.domain.Sort;
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


    public List<SurveyWord> getTopFiveMostVotedSurveys(){

        int counter = 0;

        List<SurveyWord> topFive = new ArrayList<>();

        for (SurveyWord surveyWord : surveyWordRepository.findAll(Sort.by("votesQuantity").descending())) {

            if (counter < 5){

                topFive.add(surveyWord);
                counter++;
            }

        }

        return topFive;
    }


    public List<String> getTopFiveVotesQuantity(){

        List<String> topFive = new ArrayList<>();

        for (SurveyWord surveyWord : getTopFiveMostVotedSurveys()) {

            topFive.add(Integer.toString(surveyWord.getVotesQuantity()));
        }

        //Agrego un 0 al final debido a que en chart js esta es la unica forma de que el origen se mantenga en 0
        topFive.add("0");

        return topFive;
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


    public SurveyWord getSurveyWordById(long id) {

        return surveyWordRepository.findSurveyWordById(id);
    }


    public String getEquivalentClassOfTheRaeClass(String raeClass){

        List<String> raeList = ontologyService.tokenizeTheSentence(raeClass);

        List<String> ontClassesName = ontologyService.getAllClassesLocalName();

        for (String ontClassName : ontClassesName) {

            for (String raeWord : raeList) {

                if (ontClassName.toLowerCase().matches(".*"+raeWord.toLowerCase()+".*"))
                    return ontClassName;
            }
        }

        return "Temporal";
    }


    public Word convertSurveyWordToWord(SurveyWord word){

        String totalAnswers = String.valueOf(word.getTotalAnswers());
        String votesQuantity = String.valueOf(word.getVotesQuantity());

        return new Word(word.getLemma(), word.getDefinition(), word.getExample(), word.getFatherClass(),
                word.getSynonyms(), word.getLemmaRAE(), totalAnswers, votesQuantity);
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


    public boolean alreadyVoteWordWithTheSameLemmaExist(SurveyWord wordToEvaluate, String actualUsername){

        List<SurveyWord> wordsByLemma = getAllSurveysByLemma(wordToEvaluate.getLemma());

        for (SurveyWord surveyWord : wordsByLemma) {

            boolean hasSameDefinition = wordToEvaluate.getDefinition().equalsIgnoreCase(surveyWord.getDefinition());

            if (!hasSameDefinition && surveyWord.getAlreadyVoteUsernames().contains(actualUsername))
                return true;
        }

        return false;
    }


    public void evaluateIfTheWordEntersTheOntology(SurveyWord surveyWordToVote) {

        SurveyWord surveyWordWinner = determineSurveyWordWinner(surveyWordToVote.getLemma());

        Word wordWinner = convertSurveyWordToWord(surveyWordWinner);

        int votesQuantity = Integer.parseInt(wordWinner.getCantidadVotacionesI());

        float percentageAgreement = wordService.calculateWordPercentageAgreement(wordWinner);

        if (percentageAgreement > 40 && votesQuantity > 1)
            ontologyService.saveIndividual(wordWinner.getLema(), wordWinner);
    }
}
