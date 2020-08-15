package arkham.knight.ontology.models;

public class Word {

    private String lema;

    private String definition;
    private String example;
    private String grammarMark;

    public Word() {
    }

    public Word(String lema, String definition, String example, String grammarMark) {
        this.lema = lema;
        this.definition = definition;
        this.example = example;
        this.grammarMark = grammarMark;
    }


    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getGrammarMark() {
        return grammarMark;
    }

    public void setGrammarMark(String grammarMark) {
        this.grammarMark = grammarMark;
    }
}
