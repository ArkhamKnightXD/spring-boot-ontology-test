package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Word;
import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Service;
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


    public float calculateWordPercentageAgreement(Word wordToEvaluate){

        if (wordToEvaluate.getCantidadVotacionesI() != null){

            int votesQuantity = Integer.parseInt(wordToEvaluate.getCantidadVotacionesI());
            int totalAnswers = Integer.parseInt(wordToEvaluate.getTotalRespuestasN());

            //formula = I/N *100 el cast a float es necesario sino me da 0 siempre
            return (float) votesQuantity/totalAnswers * 100;
        }

        return 0;
    }


    public float calculateWordPercentageAgreementOfPresenceOrAbsents(Word wordToEvaluate, boolean percentageType){

        if (wordToEvaluate.getCantidadVotacionesI() != null && wordToEvaluate.getNumeroDeAusenciasD() != null){

            int numberOfAbsencesD = Integer.parseInt(wordToEvaluate.getNumeroDeAusenciasD());;
            int numberOfPresencesA = Integer.parseInt(wordToEvaluate.getNumeroDePresenciasA());;

            int totalAnswers = Integer.parseInt(wordToEvaluate.getTotalRespuestasN());

            //Porcentaje de acuerdo de ‘presencia’ formula = A/N-D * 100
            if (percentageType)
                return (float) (numberOfPresencesA /(totalAnswers- numberOfAbsencesD)) * 100;

            //Porcentaje de acuerdo de ausencias formula = D/N-A * 100
            return (float) (numberOfAbsencesD /(totalAnswers- numberOfPresencesA)) * 100;
        }

        return 0;
    }


    public float calculateWordMeanPercentageAgreement(Word wordToEvaluate){

        float percentageOfAbsences = calculateWordPercentageAgreementOfPresenceOrAbsents(wordToEvaluate, false);
        float percentageOfPresences = calculateWordPercentageAgreementOfPresenceOrAbsents(wordToEvaluate, true);

        //formula = porcentaje de ausencias + porcentaje de asistencias *100
        return  ((percentageOfPresences+percentageOfAbsences)/2) * 100;
    }
}
