package arkham.knight.ontology.repositories;

import arkham.knight.ontology.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findRolById(long rolId);
}
