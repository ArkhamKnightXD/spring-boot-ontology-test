package arkham.knight.ontology.models;

public class Word {

    private String lema;

    private String definition;
    private String example;
    private String grammarMark;
    private String marcaNivelSocioCultural;
    private String marcaVariacionEstilistica;
    private String locution;
    private String locutionType;


    public Word() {
    }


    public String getLema() { return lema; }

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

    public String getMarcaNivelSocioCultural() {
        return marcaNivelSocioCultural;
    }

    public void setMarcaNivelSocioCultural(String marcaNivelSocioCultural) { this.marcaNivelSocioCultural = marcaNivelSocioCultural; }

    public String getMarcaVariacionEstilistica() {
        return marcaVariacionEstilistica;
    }

    public void setMarcaVariacionEstilistica(String marcaVariacionEstilistica) { this.marcaVariacionEstilistica = marcaVariacionEstilistica; }

    public String getLocution() {
        return locution;
    }

    public void setLocution(String locution) {
        this.locution = locution;
    }

    public String getLocutionType() {
        return locutionType;
    }

    public void setLocutionType(String locutionType) {
        this.locutionType = locutionType;
    }
}
