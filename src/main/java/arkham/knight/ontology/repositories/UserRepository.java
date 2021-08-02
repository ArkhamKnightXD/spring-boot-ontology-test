package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserById(long id);

    List<User> findAllByRolList_Rol(String rol);
}
