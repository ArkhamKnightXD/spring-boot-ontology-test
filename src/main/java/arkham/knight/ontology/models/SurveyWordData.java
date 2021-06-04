package arkham.knight.ontology.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class SurveyWordData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lemma;
    private String originalDefinition;
    private String example;
    private String fatherClass;
    private String synonyms;
    private String lemmaRAE;
    private String definitionRAE;
    private int totalAnswers;
    private int votesQuantity;


    public SurveyWordData() {
    }

    public SurveyWordData(String lemma) {
        this.lemma = lemma;
    }

    public SurveyWordData(String lemma, String originalDefinition, String example, String fatherClass, String synonyms, String lemmaRAE, String definitionRAE) {
        this.lemma = lemma;
        this.originalDefinition = originalDefinition;
        this.example = example;
        this.fatherClass = fatherClass;
        this.synonyms = synonyms;
        this.lemmaRAE = lemmaRAE;
        this.definitionRAE = definitionRAE;
    }


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getLemma() { return lemma; }

    public void setLemma(String lemma) { this.lemma = lemma; }

    public String getOriginalDefinition() { return originalDefinition; }

    public void setOriginalDefinition(String originalDefinition) { this.originalDefinition = originalDefinition; }

    public String getExample() { return example; }

    public void setExample(String example) { this.example = example; }

    public String getFatherClass() { return fatherClass; }

    public void setFatherClass(String fatherClass) { this.fatherClass = fatherClass; }

    public String getSynonyms() { return synonyms; }

    public void setSynonyms(String synonyms) { this.synonyms = synonyms; }

    public String getLemmaRAE() { return lemmaRAE; }

    public void setLemmaRAE(String lemmaRAE) { this.lemmaRAE = lemmaRAE; }

    public String getDefinitionRAE() { return definitionRAE; }

    public void setDefinitionRAE(String definitionRAE) { this.definitionRAE = definitionRAE; }

    public int getTotalAnswers() { return totalAnswers; }

    public void setTotalAnswers(int totalAnswers) { this.totalAnswers = totalAnswers; }

    public int getVotesQuantity() { return votesQuantity; }

    public void setVotesQuantity(int votesQuantity) { this.votesQuantity = votesQuantity; }
}
