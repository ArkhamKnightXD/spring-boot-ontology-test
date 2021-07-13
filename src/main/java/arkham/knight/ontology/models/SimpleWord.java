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

    @ElementCollection
    @JsonIgnore
    private List<String> ipAddresses;


    public SimpleWord() { }

    public SimpleWord(String word, String wordDefinition, boolean passTheVote) {
        this.word = word;
        this.wordDefinition = wordDefinition;
        this.passTheVote = passTheVote;
    }

    public Long getId() { return id; }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }

    public String getWordDefinition() { return wordDefinition; }

    public void setWordDefinition(String wordDefinition) { this.wordDefinition = wordDefinition; }

    public int getTotalAnswers() { return totalAnswers; }

    public void setTotalAnswers(int totalAnswers) { this.totalAnswers = totalAnswers; }

    public int getVotesQuantity() { return votesQuantity; }

    public void setVotesQuantity(int votesQuantity) { this.votesQuantity = votesQuantity; }

    public boolean isPassTheVote() { return passTheVote; }

    public void setPassTheVote(boolean passTheVote) { this.passTheVote = passTheVote; }

    public List<String> getIpAddresses() { return ipAddresses; }

    public void setIpAddresses(String ipAddress) { this.ipAddresses.add(ipAddress); }
}
