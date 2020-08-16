package arkham.knight.ontology.models;

public class Word {

    private String lema;

    private String definicion;
    private String ejemplo;
    private String marcaGramatical;
    private String marcaNivelSocioCultural;
    private String marcaVariacionEstilistica;
    private String locucion;
    private String tipoLocution;


    public Word() {
    }


    public String getLema() { return lema; }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getDefinicion() {
        return definicion;
    }

    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }

    public String getEjemplo() {
        return ejemplo;
    }

    public void setEjemplo(String ejemplo) {
        this.ejemplo = ejemplo;
    }

    public String getMarcaGramatical() {
        return marcaGramatical;
    }

    public void setMarcaGramatical(String marcaGramatical) {
        this.marcaGramatical = marcaGramatical;
    }

    public String getMarcaNivelSocioCultural() {
        return marcaNivelSocioCultural;
    }

    public void setMarcaNivelSocioCultural(String marcaNivelSocioCultural) { this.marcaNivelSocioCultural = marcaNivelSocioCultural; }

    public String getMarcaVariacionEstilistica() {
        return marcaVariacionEstilistica;
    }

    public void setMarcaVariacionEstilistica(String marcaVariacionEstilistica) { this.marcaVariacionEstilistica = marcaVariacionEstilistica; }

    public String getLocucion() {
        return locucion;
    }

    public void setLocucion(String locucion) {
        this.locucion = locucion;
    }

    public String getTipoLocution() {
        return tipoLocution;
    }

    public void setTipoLocution(String tipoLocution) {
        this.tipoLocution = tipoLocution;
    }
}
