package arkham.knight.ontology.models;

import java.io.Serializable;
import java.util.List;

public class DRAEVariation implements Serializable {

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
