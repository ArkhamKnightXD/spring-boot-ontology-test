package arkham.knight.ontology.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class SimpleWord implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private String wordDefinition;
    private int totalAnswers;
    private int votesQuantity;
    private boolean passTheVote;
    private boolean userAlreadyVote;

    @ElementCollection
    @JsonIgnore
    private List<String> alreadyVoteUsernames;


    public SimpleWord() { }

    public SimpleWord(String word, String wordDefinition, boolean passTheVote) {
        this.word = word;
        this.wordDefinition = wordDefinition;
        this.passTheVote = passTheVote;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }

    public String getWordDefinition() { return wordDefinition; }

    public void setWordDefinition(String wordDefinition) { this.wordDefinition = wordDefinition; }

    public int getTotalAnswers() { return totalAnswers; }

    public int getVotesQuantity() { return votesQuantity; }

    public void setVotesQuantity(int votesQuantity) { this.votesQuantity = votesQuantity; }

    public boolean isPassTheVote() { return passTheVote; }

    public void setPassTheVote(boolean passTheVote) { this.passTheVote = passTheVote; }

    public boolean isUserAlreadyVote() { return userAlreadyVote; }

    public void setUserAlreadyVote(boolean userAlreadyVote) { this.userAlreadyVote = userAlreadyVote; }

    public List<String> getAlreadyVoteUsernames() { return alreadyVoteUsernames; }

    public void setAlreadyVoteUsernames(String alreadyVoteUser) { this.alreadyVoteUsernames.add(alreadyVoteUser); }
}
