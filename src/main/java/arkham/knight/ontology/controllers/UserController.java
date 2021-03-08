package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "/freemarker/user/users";
    }


    @RequestMapping(value = "/creation", method = RequestMethod.GET)
    public String creationUserPage(Model model) {

        model.addAttribute("roles", userService.getAllRoles());

        return "/freemarker/user/createUser";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam Long idRol) {

        Rol userRol = userService.getRolById(idRol);

        List<Rol> usersRoles = new ArrayList<>();

        usersRoles.add(userRol);

        User userToCreate = new User(username, bCryptPasswordEncoder.encode(password), true, email, usersRoles);

        userService.saveUser(userToCreate);

        return "redirect:/users/";
    }


    @RequestMapping(value = "/edition", method = RequestMethod.GET)
    public String editionUserPage(Model model, @RequestParam Long id)  {

        User userToEdit = userService.getUserById(id);

        model.addAttribute("user", userToEdit);
        model.addAttribute("roles", userService.getAllRoles());

        return "/freemarker/user/editUser";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editUser(@RequestParam Long id, @RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam Long idRol) {

        User userToEdit = userService.getUserById(id);

        Rol userRol = userService.getRolById(idRol);

        List<Rol> usersRoles = new ArrayList<>();

        usersRoles.add(userRol);

        userToEdit.setUsername(username);
        userToEdit.setPassword(bCryptPasswordEncoder.encode(password));
        userToEdit.setEmail(email);
        userToEdit.setRolList(usersRoles);

        userService.saveUser(userToEdit);
        
        return "redirect:/users/";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String editUser(@RequestParam Long id) {

        userService.deleteUserById(id);

        return "redirect:/users/";
    }
}
