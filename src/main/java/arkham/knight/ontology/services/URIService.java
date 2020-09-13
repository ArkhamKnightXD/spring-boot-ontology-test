package arkham.knight.ontology.services;

import org.springframework.stereotype.Service;

@Service
public class URIService {

    public final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";

    public final String definitionURI = ontologyURI.concat("definicion");

    public final String exampleURI = ontologyURI.concat("ejemplo");

    public final String grammarMarkURI = ontologyURI.concat("marca_gramatical");

    public final String markCulturalLevelURI = ontologyURI.concat("marca_nivel_sociocultural");

    public final String markStyleValueURI = ontologyURI.concat("marca_variacion_estilistica");

    public final String locutionURI = ontologyURI.concat("locucion");

    public final String locutionTypeURI = ontologyURI.concat("tipo_locucion");

    public final String phraseURI = ontologyURI.concat("frase_proverbial");

    public final String phraseTypeURI = ontologyURI.concat("tipo_frase_proverbial");

    public final String markURI = ontologyURI.concat("marca_registrada");

    public final String registerMarkURI = ontologyURI.concat("marca_registro");

    public final String habitualPronunciationURI = ontologyURI.concat("pronunciacion_habitual");

    public final String useValidityURI = ontologyURI.concat("vigencia_uso");
}
