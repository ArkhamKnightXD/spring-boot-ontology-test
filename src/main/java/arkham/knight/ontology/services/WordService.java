package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {

    private final OntologyService ontologyService;

    public WordService(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }


    public List<Word> getAllWords(){

        List<String> individualNameList = ontologyService.getAllIndividualLocalName();

        List<Individual> individualList = ontologyService.getAllIndividualByName(individualNameList,"tweet");

        return ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);
    }


    public List<Word> getAllVotedWords(){

        List<Word> votedWords = new ArrayList<>();

        for (Word word : getAllWords()) {

            try {

                if (!word.getLemaRAE().isEmpty()){

                    word.setPorcentajeDeAcuerdo(calculateWordPercentageAgreement(word));

                    votedWords.add(word);
                }

            }catch (Exception ignored){


            }
        }

        return votedWords;
    }


    public List<Word> getTopFivePercentageAgreementWords(){

        int counter = 0;

        float comparatorPercentage = 40;

        List<Word> topFive = new ArrayList<>();

        for (Word word : getAllWords()) {

            float actualPercentage = calculateWordPercentageAgreement(word);

            if (actualPercentage >= comparatorPercentage && counter < 5){

                comparatorPercentage = actualPercentage;

                counter++;

                topFive.add(word);
            }
        }

        return topFive;
    }


    public List<Float> getTopFivePercentageAgreement(){

        List<Float> topFive = new ArrayList<>();

        for (Word word : getTopFivePercentageAgreementWords()) {

            topFive.add(calculateWordPercentageAgreement(word));
        }

        topFive.add(0.f);

        return topFive;
    }


    public List<Word> getAllWordsByFatherClassName(String fatherClassName){

        List<Individual> individualList = ontologyService.getAllIndividualsByFatherClassName(fatherClassName);

        return ontologyService.saveAllIndividualPropertiesValueInAWordList(individualList);
    }


    public Word getWordByLemma(String lemma){

        for (Word wordToFind: getAllWords()) {

            if (wordToFind.getLema().equals(lemma))
                return wordToFind;
        }

        return null;
    }


    public Word editionWordFilter(Word wordToEdit, Word wordDataToSave){

        if (wordDataToSave.getLema().length() != 0)
            wordToEdit.setLema(wordDataToSave.getLema());

        if (wordDataToSave.getDefinicion().length() !=0)
            wordToEdit.setDefinicion(wordDataToSave.getDefinicion());

        if (wordDataToSave.getEjemplo().length() != 0)
            wordToEdit.setEjemplo(wordDataToSave.getEjemplo());

        if (wordDataToSave.getClasePadre().length() != 0)
            wordToEdit.setClasePadre(wordDataToSave.getClasePadre());

        if (wordDataToSave.getLemaRAE().length() != 0)
            wordToEdit.setLemaRAE(wordDataToSave.getLemaRAE());

        if (wordDataToSave.getSinonimos().length() != 0)
            wordToEdit.setSinonimos(wordDataToSave.getSinonimos());

        return wordToEdit;
    }


    public List<Word> evaluateWordsAndReturnCleanWordList(List<Word> originalWordList) {

        List<Word> cleanWordList = new ArrayList<>();

        for (Word wordToEvaluate : originalWordList) {

            float percentage = calculateWordPercentageAgreement(wordToEvaluate);

            if (percentage >= 40){

                wordToEvaluate.setPorcentajeDeAcuerdo(percentage);

                cleanWordList.add(wordToEvaluate);
            }
        }

        return cleanWordList;
    }


    public float calculateWordPercentageAgreement(Word wordToEvaluate){

        if (wordToEvaluate.getCantidadVotacionesI() != null){

            int votesQuantity = Integer.parseInt(wordToEvaluate.getCantidadVotacionesI());
            int totalAnswers = Integer.parseInt(wordToEvaluate.getTotalRespuestasN());
            //formula = I/N *100 el cast a float es necesario sino me da 0 siempre

            if (totalAnswers > votesQuantity)
                return (float) votesQuantity/totalAnswers * 100;

            //si las respuesta totales son menores que los votos el resultado me dara mas de 100% en todos los casos
            return (float) 100;
        }

        return 0;
    }
}
