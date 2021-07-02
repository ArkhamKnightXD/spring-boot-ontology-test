package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.SurveyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyWordRepository extends JpaRepository<SurveyWord, Long> {

    SurveyWord findByLemma(String lemma);

    SurveyWord findSurveyWordById(long id);

    List<SurveyWord> findAllByLemma(String lemma);

    List<SurveyWord> findAllByLemmaRAE(String lemmaRae);
}
