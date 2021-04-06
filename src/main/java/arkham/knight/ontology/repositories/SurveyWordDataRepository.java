package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.SurveyWordData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyWordDataRepository extends JpaRepository<SurveyWordData, Long> {

    List<SurveyWordData> findAllByLemma(String lemma);

    List<SurveyWordData> findAllByLemmaRAE(String lemmaRae);

    SurveyWordData findSurveyWordDataById(long id);
}
