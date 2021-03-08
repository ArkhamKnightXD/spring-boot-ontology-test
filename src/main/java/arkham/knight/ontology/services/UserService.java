package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.repositories.RolRepository;
import arkham.knight.ontology.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

//basic db
//    private static List<User> userDb =new ArrayList<>();

    final UserRepository userRepository;

    final RolRepository rolRepository;

    public UserService(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }


    public void saveUser(User user){

        userRepository.save(user);
    }


    public List<User> getAllUsers(){

        return userRepository.findAll();
    }


    public User getUserByUsername(String username){

        return userRepository.findUserByUsername(username);
    }


    public User getUserById(long id){

        return userRepository.findUserById(id);
    }


    public void deleteUserById(Long id){

        userRepository.deleteById(id);
    }


    public List<Rol> getAllRoles(){

        return rolRepository.findAll();
    }


    public Rol getRolById(long idRol){

        return rolRepository.findRolById(idRol);
    }


//basic save method
//    public void saveUser(User user){
//
//        userDb.add(user);
//    }


//    public User findUserByUsername(String username){
//        for (User userToFind: userDb) {
//
//            if (userToFind.getUsername().equalsIgnoreCase(username))
//                return userToFind;
//        }
//        return new User();
//    }
}
