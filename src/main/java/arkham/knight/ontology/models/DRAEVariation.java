package arkham.knight.ontology.models;

import java.util.List;

public class DRAEVariation {

    private String variation;
    private List<DRAEDefinition> definitions;

    public String getVariation() { return variation; }

    public List<DRAEDefinition> getDefinitions() { return definitions; }

    @Override
    public String toString() {
        return "DRAEVariation{" +
                "variation='" + variation + '\'' +
                ", definitions=" + definitions +
                '}';
    }
}
