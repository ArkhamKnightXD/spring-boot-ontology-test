package arkham.knight.ontology.controllers;

import arkham.knight.ontology.models.User;
import arkham.knight.ontology.services.MyUserDetailsService;
import arkham.knight.ontology.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

//Este controlador maneja todos los posibles errores de url y tambien maneja el login
@Controller
public class URLController implements ErrorController {

    private final UserService userService;

    private final MyUserDetailsService myUserDetailsService;

    public URLController(UserService userService, MyUserDetailsService myUserDetailsService) {
        this.userService = userService;
        this.myUserDetailsService = myUserDetailsService;
    }

    //es necesario implementar este metodo para que funcione el error controller
    @Override
    public String getErrorPath() {

        return "/error";
    }


    @RequestMapping("/login")
    public String login(){

        User adminUser = userService.findUserByUsername("admin");

        if (adminUser == null){

            myUserDetailsService.createAdminUser();
        }

        return "/freemarker/login";
    }


    @RequestMapping("/login-error")
    public String errorPage404(Model model){

        model.addAttribute("title", "Login Failed");
        model.addAttribute("message", "User Not Authorized");

        return "/freemarker/error";
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

                return "freemarker/error";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {

                model.addAttribute("title", "Error 500");
                model.addAttribute("message", "Error 500 - Internal Server Error");

                return "freemarker/error";
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {

                model.addAttribute("title", "Error 403");
                model.addAttribute("message", "Error 403 - This Page is Forbidden");

                return "freemarker/error";
            }
        }

        return "error";
    }
}
