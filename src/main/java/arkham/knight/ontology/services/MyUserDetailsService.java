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

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepository;

    private final RolRepository rolRepository;

    public MyUserDetailsService(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }


    public void createAdminUser(){

        List<Rol> rolList = new ArrayList<>();

        Rol rolUser = new Rol("ROLE_USER");
        Rol rolAdmin = new Rol("ROLE_ADMIN");

        rolList.add(rolUser);
        rolList.add(rolAdmin);

        rolRepository.save(rolAdmin);
        rolRepository.save(rolUser);

        User adminUser = new User("admin",bCryptPasswordEncoder.encode("1234"),true, rolList);

        userRepository.save(adminUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> rolList = new ArrayList<>();

        User userAdminToFind = userRepository.findUserByUsername(username);

        for (Rol roles : userAdminToFind.getRolList()) {

            rolList.add(new SimpleGrantedAuthority(roles.getRol()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(rolList);

        return new org.springframework.security.core.userdetails.User(userAdminToFind.getUsername(),userAdminToFind.getPassword(), userAdminToFind.isAdmin(), true, true, true, grantedAuthorities);
    }
}
