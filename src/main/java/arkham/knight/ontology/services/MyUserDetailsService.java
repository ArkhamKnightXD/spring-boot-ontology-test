package arkham.knight.ontology.services;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
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

    final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }


    public void createAdminUser(){

        List<Rol> rolList = new ArrayList<>();

        Rol rolUser = new Rol("ROLE_USER");
        Rol rolAdmin = new Rol("ROLE_ADMIN");

        rolList.add(rolUser);
        rolList.add(rolAdmin);

        userService.saveUserRol(rolAdmin);
        userService.saveUserRol(rolUser);

        User adminUser = new User("admin",bCryptPasswordEncoder.encode("1234"),true,rolList);

        userService.saveUser(adminUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> rolList = new ArrayList<>();

        User userAdminToFind = userService.findUserByUsername(username);

        for (Rol roles :userAdminToFind.getRolList()) {

            rolList.add(new SimpleGrantedAuthority(roles.getRol()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(rolList);

        return new org.springframework.security.core.userdetails.User(userAdminToFind.getUsername(),userAdminToFind.getPassword(), userAdminToFind.isAdmin(), true, true, true, grantedAuthorities);
    }
}
