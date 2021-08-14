package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping(value = "/")
    public String indexPage(Model model, Principal principal) {

        User actualUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("loggedUsername", actualUser.getNameToShow());
        model.addAttribute("users", userService.getAllUsers());

        return "/freemarker/user/users";
    }


    @GetMapping("/creation")
    public String creationUserPage(Model model) {

        model.addAttribute("roles", userService.getAllRoles());

        return "/freemarker/user/createUser";
    }


    @PostMapping("/create")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam Long idRol) {

        Rol userRol = userService.getRolById(idRol);

        List<Rol> usersRoles = new ArrayList<>();

        usersRoles.add(userRol);

        User userToCreate = new User(username, bCryptPasswordEncoder.encode(password), "user", true, usersRoles);

        userService.saveUser(userToCreate);

        return "redirect:/users/";
    }


    @GetMapping("/edition")
    public String editionUserPage(Model model, @RequestParam long id)  {

        User userToEdit = userService.getUserById(id);

        model.addAttribute("user", userToEdit);
        model.addAttribute("roles", userService.getAllRoles());

        return "/freemarker/user/editUser";
    }


    @PostMapping("/edit")
    public String editUser(@RequestParam long id, @RequestParam String username, @RequestParam String password, @RequestParam Long idRol) {

        User userToEdit = userService.getUserById(id);

        Rol userRol = userService.getRolById(idRol);

        List<Rol> usersRoles = new ArrayList<>();

        usersRoles.add(userRol);

        userToEdit.setUsername(username);
        userToEdit.setPassword(bCryptPasswordEncoder.encode(password));
        userToEdit.setRolList(usersRoles);

        userService.saveUser(userToEdit);
        
        return "redirect:/users/";
    }


    @GetMapping("/delete")
    public String deleteUser(@RequestParam long id) {

        userService.deleteUserById(id);

        return "redirect:/users/";
    }
}
