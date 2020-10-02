package arkham.knight.ontology.models;

public class Word {

    private String lema;
    private String definicion;
    private String ejemplo;
    private String marcaGramatical;
    private String marcaGeografica;
    private String marcaNivelSocioCultural;
    private String marcaVariacionEstilistica;
    private String marcaValoracionSocial;
    private String marcaRegistrada;
    private String marcaRegistro;
    private String marcaConnotacion;
    private String pronunciacionHabitual;
    private String vigenciaUso;
    private String idiomaOrigen;


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

    public String getMarcaGramatical() {
        return marcaGramatical;
    }

    public void setMarcaGramatical(String marcaGramatical) {
        this.marcaGramatical = marcaGramatical;
    }

    public String getMarcaGeografica() {
        return marcaGeografica;
    }

    public void setMarcaGeografica(String marcaGeografica) {
        this.marcaGeografica = marcaGeografica;
    }

    public String getMarcaNivelSocioCultural() {
        return marcaNivelSocioCultural;
    }

    public void setMarcaNivelSocioCultural(String marcaNivelSocioCultural) { this.marcaNivelSocioCultural = marcaNivelSocioCultural; }

    public String getMarcaVariacionEstilistica() {
        return marcaVariacionEstilistica;
    }

    public void setMarcaVariacionEstilistica(String marcaVariacionEstilistica) { this.marcaVariacionEstilistica = marcaVariacionEstilistica; }

    public String getMarcaValoracionSocial() {
        return marcaValoracionSocial;
    }

    public void setMarcaValoracionSocial(String marcaValoracionSocial) { this.marcaValoracionSocial = marcaValoracionSocial; }

    public String getMarcaRegistrada() {
        return marcaRegistrada;
    }

    public void setMarcaRegistrada(String marcaRegistrada) {
        this.marcaRegistrada = marcaRegistrada;
    }

    public String getMarcaRegistro() {
        return marcaRegistro;
    }

    public void setMarcaRegistro(String marcaRegistro) {
        this.marcaRegistro = marcaRegistro;
    }

    public String getMarcaConnotacion() {
        return marcaConnotacion;
    }

    public void setMarcaConnotacion(String marcaConnotacion) {
        this.marcaConnotacion = marcaConnotacion;
    }

    public String getPronunciacionHabitual() {
        return pronunciacionHabitual;
    }

    public void setPronunciacionHabitual(String pronunciacionHabitual) { this.pronunciacionHabitual = pronunciacionHabitual; }

    public String getVigenciaUso() {
        return vigenciaUso;
    }

    public void setVigenciaUso(String vigenciaUso) {
        this.vigenciaUso = vigenciaUso;
    }

    public String getIdiomaOrigen() { return idiomaOrigen; }

    public void setIdiomaOrigen(String idiomaOrigen) { this.idiomaOrigen = idiomaOrigen; }
}
