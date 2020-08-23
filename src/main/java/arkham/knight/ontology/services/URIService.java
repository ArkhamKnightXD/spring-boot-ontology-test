package arkham.knight.ontology.services;

import org.springframework.stereotype.Service;

@Service
public class URIService {

    public final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";

    public final String definitionURI = ontologyURI.concat("definicion");

    public final String exampleURI = ontologyURI.concat("ejemplo");

    public final String grammarMarkURI = ontologyURI.concat("marca_gramatical");

    public final String marcaNivelSocioCulturalURI = ontologyURI.concat("marca_nivel_sociocultural");

    public final String marcaVariacionEstilisticaURI = ontologyURI.concat("marca_variacion_estilistica");

    public final String locutionURI = ontologyURI.concat("locucion");

    public final String locutionTypeURI = ontologyURI.concat("tipo_locucion");
}
