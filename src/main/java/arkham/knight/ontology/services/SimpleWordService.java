package arkham.knight.ontology.services;

import arkham.knight.ontology.models.SimpleWord;
import arkham.knight.ontology.repositories.SimpleWordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimpleWordService {

    private final SimpleWordRepository simpleWordRepository;

    public SimpleWordService(SimpleWordRepository simpleWordRepository) {
        this.simpleWordRepository = simpleWordRepository;
    }


    public void saveSimpleWord(SimpleWord simpleWord){

        simpleWordRepository.save(simpleWord);
    }


    public List<SimpleWord> getAllSimpleWord(){

        return simpleWordRepository.findAll();
    }


    public SimpleWord getSimpleWordByWord(String word){

        return simpleWordRepository.findByWord(word);
    }


    public SimpleWord getSimpleWordById(long id){

        return simpleWordRepository.findById(id);
    }


    public void deleteSimpleWordById(Long id){

        simpleWordRepository.deleteById(id);
    }
}
