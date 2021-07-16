package arkham.knight.ontology.models;

import java.io.Serializable;

public class DefinitionResponse  implements Serializable {

    private String definitionNumber;
    private String wordClassType;
    private String definition;


    public DefinitionResponse() {
    }


    public String getDefinitionNumber() { return definitionNumber; }

    public void setDefinitionNumber(String definitionNumber) { this.definitionNumber = definitionNumber; }

    public String getWordClassType() { return wordClassType; }

    public void setWordClassType(String wordClassType) { this.wordClassType = wordClassType; }

    public String getDefinition() { return definition; }

    public void setDefinition(String definition) { this.definition = definition; }


    @Override
    public String toString() {

        return "DefinitionResponse{" +
                "definitionNumber='" + definitionNumber + '\'' +
                ", wordClassType='" + wordClassType + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
