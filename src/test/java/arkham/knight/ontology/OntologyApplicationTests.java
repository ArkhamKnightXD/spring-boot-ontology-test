package arkham.knight.ontology;

import arkham.knight.ontology.services.OntologyService;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OntologyApplicationTests {

   /* @Test
    void testTweetSearch(OntologyService ontologyService) {

         //print the ontology
        //ontologyService.readOntologyFileAndReturnTheModel().write(System.out,"RDF/XML-ABBREV");

       final String textExample = "Yo soy un jornalero que esperaba beber un morirsoñando em el entretiempo para despues ponerme a motoconchar echadías";

        for (Individual individual: ontologyService.getAllIndividualByName(ontologyService.getAllIndividualLocalName(),"text")) {

            System.out.println(individual.getLocalName());
        }

        //mientras tanto este test estara asi
        assertEquals(true, true);
    }*/


    @Test
    public void testStringComparator(){

        boolean comparator = false;
        String string = "pancakes";

        // Esta la implementacion en java del metodo like de sql
        //En este caso al string se le debe de agregar .* que es el equivalente a % esto quiere decir que no importa las palabras demas que haya
        String string2 = ".*panca.*";

        //y con el metodo matches aqui hago las comparaciones
        if (string.matches(string2)){

            comparator = true;
        }

        //siempre y cuando sea true este test no fallara
        assertTrue(comparator);
    }
}
