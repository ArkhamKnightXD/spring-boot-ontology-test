package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.SurveyWord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyWordRepository extends JpaRepository<SurveyWord, Long> {

    SurveyWord findSurveyWordById(long id);

    @Override
    List<SurveyWord> findAll(Sort sort);
}
