package arkham.knight.ontology.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class SimpleWord implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private String wordDefinition;


    public SimpleWord() { }

    public SimpleWord(String word) { this.word = word; }


    public Long getId() { return id; }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }

    public String getWordDefinition() { return wordDefinition; }

    public void setWordDefinition(String wordDefinition) { this.wordDefinition = wordDefinition; }
}
