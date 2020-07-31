package arkham.knight.ontology.controllers;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.FileNotFoundException;

@RequestMapping("/find")
@Controller
public class OntologyController {

    @Autowired
    private OntologyService ontologyService;

    private final String ontologyURI = "http://www.semanticweb.org/luis_/ontologies/2020/6/untitled-ontology-2#";


    @RequestMapping("/")
    public String getIndividualPropertiesAndValues(Model model, @RequestParam(defaultValue = "morirsoñando") String individualName) throws FileNotFoundException {

        String individualURI = ontologyURI.concat(individualName);

        Individual individual = ontologyService.readOntologyFileAndReturnTheModel().getIndividual(individualURI);

        String definitionURI = ontologyURI.concat("definicion");

        String exampleURI = ontologyURI.concat("ejemplo");

        String grammarMarkURI = ontologyURI.concat("marca_gramatical");

        String markSocialCulturalURI = ontologyURI.concat("marca_gramatical");


        Property definitionProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(definitionURI);

        Property exampleProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(exampleURI);

        Property grammarMarkProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(grammarMarkURI);

        Property markSocialCulturalProperty = ontologyService.readOntologyFileAndReturnTheModel().getProperty(markSocialCulturalURI);


        RDFNode definitionPropertyValue = individual.getPropertyValue(definitionProperty);

        RDFNode examplePropertyValue = individual.getPropertyValue(exampleProperty);

        RDFNode grammarMarkPropertyValue = individual.getPropertyValue(grammarMarkProperty);

        RDFNode markSocialCulturalPropertyValue = individual.getPropertyValue(markSocialCulturalProperty);



        model.addAttribute("definicion", definitionPropertyValue.toString());

        if (grammarMarkPropertyValue != null)
            model.addAttribute("marca_gramatical", grammarMarkPropertyValue.toString());

        if (examplePropertyValue != null)
            model.addAttribute("ejemplo", examplePropertyValue.toString());

        /*if (markSocialCulturalPropertyValue!= null)
            jsonObject.put("ejemplo", markSocialCulturalPropertyValue.toString());*/

        return "/freemarker/summary";
    }
}
