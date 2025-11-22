package space.bielsolososdev.rdl.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/error/404")
    public String error404(@RequestParam(required = false) String slug, Model model) {
        if (slug != null && !slug.isEmpty()) {
            model.addAttribute("slug", slug);
        }
        return "error/404";
    }
}
