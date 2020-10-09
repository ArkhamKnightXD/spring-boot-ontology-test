package arkham.knight.ontology.services;

import org.semanticweb.owlapi.model.IRI;

public class URIService {

    public static final IRI ontologyIRI = IRI.create("http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#");

    public static final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";

    public static final String definitionURI = ontologyURI.concat("definicion");

    public static final String exampleURI = ontologyURI.concat("ejemplo");

    public static final String grammarMarkURI = ontologyURI.concat("marca_gramatical");

    public static final String markSocialCulturalLevelURI = ontologyURI.concat("marca_nivel_sociocultural");

    public static final String markStyleVariationURI = ontologyURI.concat("marca_variacion_estilistica");

    public static final String locutionURI = ontologyURI.concat("locucion");

    public static final String proverbialPhraseURI = ontologyURI.concat("frase_proverbial");

    public static final String markURI = ontologyURI.concat("marca_registrada");
}
