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

    private static List<User> userDb =new ArrayList<>();

//    final UserRepository userRepository;
//
//    final RolRepository rolRepository;
//
//    public UserService(UserRepository userRepository, RolRepository rolRepository) {
//        this.userRepository = userRepository;
//        this.rolRepository = rolRepository;
//    }
//
//
//    public void saveUser(User user){
//
//        userRepository.save(user);
//    }

    public void saveUser(User user){

        userDb.add(user);
    }


    public User findUserByUsername(String username){
        for (User userToFind: userDb) {

            if (userToFind.getUsername().equalsIgnoreCase(username))
                return userToFind;
        }
        return new User();
    }
//
//
//    public List<User> findAllUsers(){
//
//        return userRepository.findAll();
//    }
//
//
//    public User findUserByUsername(String username){
//
//        return userRepository.findUserByUsername(username);
//    }
//
//
//    public User findUserById(long id){
//
//        return userRepository.findUserById(id);
//    }
//
//
//    public void deleteUserById(Long id){
//
//        userRepository.deleteById(id);
//    }
//
//
//    public void saveUserRol(Rol userRol){
//
//        rolRepository.save(userRol);
//    }
//
//
//    public List<Rol> findAllRoles(){
//
//        return rolRepository.findAll();
//    }
//
//    public List<Rol> findAllRolesById(List<Long> idRoles){
//
//        return rolRepository.findAllById(idRoles);
//    }
//
//
//    public void deleteAllUser(){
//
//        userRepository.deleteAll();
//    }
//
//
//    public void deleteAllRoles(){
//
//        rolRepository.deleteAll();
//    }
}
