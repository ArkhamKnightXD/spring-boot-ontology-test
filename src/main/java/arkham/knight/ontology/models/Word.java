package arkham.knight.ontology.models;

import java.util.List;

public class Word {

    private String lema;
    private String definicion;
    private String ejemplo;
    private String clasePadre;
    private List<String> sinonimos;
    private String lemaRAE;


    public Word() {
    }


    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getDefinicion() { return definicion; }

    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }

    public String getEjemplo() {
        return ejemplo;
    }

    public void setEjemplo(String ejemplo) {
        this.ejemplo = ejemplo;
    }

    public String getClasePadre() { return clasePadre; }

    public void setClasePadre(String clasePadre) { this.clasePadre = clasePadre; }

    public List<String> getSinonimos() { return sinonimos; }

    public void setSinonimos(List<String> sinonimos) { this.sinonimos = sinonimos; }

    public String getLemaRAE() { return lemaRAE; }

    public void setLemaRAE(String lemaRAE) { this.lemaRAE = lemaRAE; }
}
