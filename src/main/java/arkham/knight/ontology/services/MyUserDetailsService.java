package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.repositories.RolRepository;
import arkham.knight.ontology.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RolRepository rolRepository;

    private final BCryptPasswordEncoder passwordEncoder;


    public MyUserDetailsService(UserRepository userRepository, RolRepository rolRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }


    public void createDefaultAdminUsers(){

        List<Rol> rolListUser = new ArrayList<>();
        List<Rol> rolListAdmin = new ArrayList<>();

        Rol rolUser = new Rol("ROLE_USER");
        Rol rolAdmin = new Rol("ROLE_ADMIN");

        rolListUser.add(rolUser);
        rolListAdmin.add(rolAdmin);

        rolRepository.saveAll(rolListUser);
        rolRepository.saveAll(rolListAdmin);

        User karUser = new User("karvinjimenez@gmail.com", passwordEncoder.encode("1234"),
                "Karvin",true, rolListAdmin);

        User luisUser = new User("luis@gmail.com", passwordEncoder.encode("1234"),
                "Luis",true, rolListAdmin);

        User testUser = new User("test@gmail.com", passwordEncoder.encode("1234"),
                "test",true, rolListUser);

        User adminUser = new User("admin@hotmail.com", passwordEncoder.encode("1234"),
                "admin",true, rolListAdmin);

        userRepository.save(adminUser);
        userRepository.save(karUser);
        userRepository.save(luisUser);
        userRepository.save(testUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> rolList = new ArrayList<>();

        User userAdminToFind = userRepository.findUserByUsername(username);

        for (Rol roles : userAdminToFind.getRolList()) {

            rolList.add(new SimpleGrantedAuthority(roles.getRol()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(rolList);

        return new org.springframework.security.core.userdetails.User(userAdminToFind.getUsername(),
                userAdminToFind.getPassword(), userAdminToFind.isAdmin(), true,
                true, true, grantedAuthorities);
    }
}
