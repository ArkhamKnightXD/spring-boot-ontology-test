package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SurveyWordData;
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

            if (calculateWordPercentageAgreement(wordToEvaluate) >= 40)
                cleanWordList.add(wordToEvaluate);
        }

        return cleanWordList;
    }


    public Word convertWordSurveyDataToWord (SurveyWordData surveyWordData){

        String totalAnswers = String.valueOf(surveyWordData.getTotalAnswers());
        String votesQuantity = String.valueOf(surveyWordData.getVotesQuantity());

        return new Word(surveyWordData.getLemma(), surveyWordData.getOriginalDefinition(),"",surveyWordData.getFatherClass(), surveyWordData.getSynonyms(),surveyWordData.getLemmaRAE(), totalAnswers, votesQuantity);
    }


    public float calculateWordPercentageAgreement(Word wordToEvaluate){

        if (wordToEvaluate.getCantidadVotacionesI() != null){

            int votesQuantity = Integer.parseInt(wordToEvaluate.getCantidadVotacionesI());
            int totalAnswers = Integer.parseInt(wordToEvaluate.getTotalRespuestasN());
            //formula = I/N *100 el cast a float es necesario sino me da 0 siempre
            return (float) votesQuantity/totalAnswers * 100;
        }

        return 0;
    }


    public float calculateSurveyWordPercentageAgreement(SurveyWordData wordToEvaluate){

//        if (wordToEvaluate.getVotesQuantity() > 0){

            int votesQuantity = wordToEvaluate.getVotesQuantity();
            int totalAnswers = wordToEvaluate.getTotalAnswers();

            return (float) votesQuantity/totalAnswers * 100;
//        }

//        return 0;
    }


    public float calculateWordPercentageAgreementOfPresenceOrAbsents(Word wordToEvaluate, boolean percentageType){

        if (wordToEvaluate.getCantidadVotacionesI() != null){

            float numberOfAbsencesD = 2;
            float numberOfPresencesA = 2;

            int totalAnswers = Integer.parseInt(wordToEvaluate.getTotalRespuestasN());
            //Porcentaje de acuerdo de ‘presencia’ formula = A/N-D * 100
            if (percentageType)
                return (numberOfPresencesA /(totalAnswers - numberOfAbsencesD)) * 100;
            //Porcentaje de acuerdo de ausencias formula = D/N-A * 100
            return (numberOfAbsencesD /(totalAnswers - numberOfPresencesA)) * 100;
        }

        return 0;
    }


    public float calculateWordMeanPercentageAgreement(Word wordToEvaluate){

        float percentageOfAbsences = calculateWordPercentageAgreementOfPresenceOrAbsents(wordToEvaluate, false)/100;
        float percentageOfPresences = calculateWordPercentageAgreementOfPresenceOrAbsents(wordToEvaluate, true)/100;
        //formula = porcentaje de ausencias + porcentaje de asistencias *100
        return  ((percentageOfPresences+percentageOfAbsences)/2) * 100;
    }
}
