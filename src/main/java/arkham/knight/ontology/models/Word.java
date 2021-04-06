package arkham.knight.ontology.models;

import java.io.Serializable;

public class Word implements Serializable {

    private String lema;
    private String definicion;
    private String ejemplo;
    private String clasePadre;
    private String sinonimos;
    private String lemaRAE;
    private String definicionRAE;
    private String totalRespuestasN;
    private String cantidadVotacionesI;


    public Word() {
    }

    public Word(String lema, String definicion, String ejemplo, String clasePadre, String sinonimos, String lemaRAE, String totalRespuestasN, String cantidadVotacionesI) {
        this.lema = lema;
        this.definicion = definicion;
        this.ejemplo = ejemplo;
        this.clasePadre = clasePadre;
        this.sinonimos = sinonimos;
        this.lemaRAE = lemaRAE;
        this.totalRespuestasN = totalRespuestasN;
        this.cantidadVotacionesI = cantidadVotacionesI;
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

    public String getSinonimos() { return sinonimos; }

    public void setSinonimos(String sinonimos) { this.sinonimos = sinonimos; }

    public String getLemaRAE() { return lemaRAE; }

    public void setLemaRAE(String lemaRAE) { this.lemaRAE = lemaRAE; }

    public String getDefinicionRAE() { return definicionRAE; }

    public void setDefinicionRAE(String definicionRAE) { this.definicionRAE = definicionRAE; }

    public String getTotalRespuestasN() { return totalRespuestasN; }

    public void setTotalRespuestasN(String totalRespuestasN) { this.totalRespuestasN = totalRespuestasN; }

    public String getCantidadVotacionesI() { return cantidadVotacionesI; }

    public void setCantidadVotacionesI(String  cantidadVotacionesI) { this.cantidadVotacionesI = cantidadVotacionesI; }
}
