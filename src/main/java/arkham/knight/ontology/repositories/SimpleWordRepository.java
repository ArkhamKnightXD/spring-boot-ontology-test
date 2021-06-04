package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.SimpleWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleWordRepository extends JpaRepository<SimpleWord, Long> {

    SimpleWord findByWord(String word);

    SimpleWord findById(long id);

    List<SimpleWord> findAllByWord(String word);
}
