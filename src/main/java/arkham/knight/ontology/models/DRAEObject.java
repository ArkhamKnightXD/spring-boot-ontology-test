package arkham.knight.ontology.models;

import java.util.List;

public class DRAEObject {

    private String word;
    private String etymology;
    private List<DRAEDefinition> definitions;
    private List<DRAEVariation> variations;


    public String getWord() { return word; }

    public String getEtymology() { return etymology; }

    public List<DRAEDefinition> getDefinitions() { return definitions; }

    public List<DRAEVariation> getVariations() { return variations; }


    @Override
    public String toString() {
        return "DRAEObject{" +
                "word='" + word + '\'' +
                ", etymology='" + etymology + '\'' +
                ", definitions=" + definitions +
                ", variations=" + variations +
                '}';
    }
}
