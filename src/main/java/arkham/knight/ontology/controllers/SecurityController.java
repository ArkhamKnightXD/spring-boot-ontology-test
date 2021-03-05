package arkham.knight.ontology.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

    @RequestMapping("/login")
    public String login(Model model){

        model.addAttribute("title","Login");

        return "/freemarker/login";
    }
}
