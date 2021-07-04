package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.SimpleWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleWordRepository extends JpaRepository<SimpleWord, Long> {

    SimpleWord findById(long id);
}
