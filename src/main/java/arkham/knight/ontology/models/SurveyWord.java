package arkham.knight.ontology.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class SurveyWord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lemma;
    private String definition;
    private String example;
    private String fatherClass;
    private String synonyms;
    private String lemmaRAE;
    //para evitar error a la hora de agregar definiciones tan largas
    @Column(length = 500)
    private String definitionRAE;
    private int totalAnswers;
    private int votesQuantity;
    private boolean userAlreadyVote;

    @ElementCollection
    @JsonIgnore
    private List<String> alreadyVoteUsernames;


    public SurveyWord() {
    }

    public SurveyWord(String lemma, String definition, String example, String fatherClass, String synonyms, String lemmaRAE, String definitionRAE) {
        this.lemma = lemma;
        this.definition = definition;
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

    public String getDefinition() { return definition; }

    public void setDefinition(String definition) { this.definition = definition; }

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

    public boolean isUserAlreadyVote() { return userAlreadyVote; }

    public void setUserAlreadyVote(boolean userAlreadyVote) { this.userAlreadyVote = userAlreadyVote; }

    public List<String> getAlreadyVoteUsernames() { return alreadyVoteUsernames; }

    public void setAlreadyVoteUsernames(String alreadyVoteUser) { this.alreadyVoteUsernames.add(alreadyVoteUser); }
}
