package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.Rol;
import arkham.knight.ontology.models.User;
import arkham.knight.ontology.services.MyUserDetailsService;
import arkham.knight.ontology.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
//Este controlador maneja todos los posibles errores de url y tambien maneja el login
@Controller
public class URLController implements ErrorController {

    private final UserService userService;

    private final MyUserDetailsService myUserDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public URLController(UserService userService, MyUserDetailsService myUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.myUserDetailsService = myUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //es necesario implementar este metodo para que funcione el error controller, aunque esto esta deprecated
    @Override
    public String getErrorPath() {

        return "/error";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String landingPage() {

        return "/freemarker/authentication/landingPage";
    }


    @RequestMapping("/login")
    public String login(){

        User adminUser = userService.getUserByUsername("admin@hotmail.com");

        if (adminUser == null)
            myUserDetailsService.createAdminUser();

        return "/freemarker/authentication/login";
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerUserPage() {

        return "/freemarker/authentication/register";
    }


    @RequestMapping(value = "/sing-up", method = RequestMethod.POST)
    public String registerUser(@RequestParam String email, @RequestParam String username, @RequestParam String password) {

        List<Rol> userRol = new ArrayList<>();

        userRol.add(new Rol("ROLE_USER"));

        User userToCreate = new User(email, bCryptPasswordEncoder.encode(password), username,true, userRol);

        userService.saveUser(userToCreate);

        return "redirect:/login";
    }


    @RequestMapping("/login-error")
    public String loginError(Model model){

        model.addAttribute("title", "Login Failed");
        model.addAttribute("message", "User Not Found or Authorized");

        return "freemarker/errors/errorPage";
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {

                model.addAttribute("title", "Error 404");
                model.addAttribute("message", "Error 404 - The Page can't be found");

                return "freemarker/errors/errorPage";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {

                model.addAttribute("title", "Error 500");
                model.addAttribute("message", "Error 500 - Internal Server Error");

                return "freemarker/errors/errorPage";
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {

                model.addAttribute("title", "Error 403");
                model.addAttribute("message", "Error 403 - This Page is Forbidden");

                return "freemarker/errors/errorPage";
            }
        }

        return "error";
    }
}
