package arkham.knight.ontology.models;

import java.io.Serializable;
import java.util.List;

public class DRAEDefinition implements Serializable {

    private String category;
    private String definition;
    private List<String> origin;
    private List<String> notes;
    private List<String> example;

    //Es necesario poner los getter en las clases obtenidas de drae pues sino me da error a la hora de mandarla por el endpoint ya que los campos son privados y se necesita
    //o los campos publicos o que la clase por lo menos tenga getters, esto pasa porque esto objeto lo cree desde un json
    public String getCategory() {
        return category;
    }

    public String getDefinition() {
        return definition;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public List<String> getNotes() {
        return notes;
    }

    public List<String> getExample() {
        return example;
    }

    @Override
    public String toString() {
        return "DRAEDefinition{" +
                "category='" + category + '\'' +
                ", definition='" + definition + '\'' +
                ", origin=" + origin +
                ", notes=" + notes +
                ", example=" + example +
                '}';
    }
}
