package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.User;
import arkham.knight.ontology.services.MyUserDetailsService;
import arkham.knight.ontology.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

    final UserService userService;

    final MyUserDetailsService myUserDetailsService;

    public SecurityController(UserService userService, MyUserDetailsService myUserDetailsService) {
        this.userService = userService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @RequestMapping("/login")
    public String login(Model model){

        User adminUser = userService.findUserByUsername("admin");

        if (adminUser == null){

            userService.deleteAllRoles();

            myUserDetailsService.createAdminUser();
        }

        model.addAttribute("title","Login");

        return "/freemarker/login";
    }
}
