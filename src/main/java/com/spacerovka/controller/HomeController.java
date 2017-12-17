package com.spacerovka.controller;

import com.spacerovka.dto.Greeting;
import com.spacerovka.service.GreetingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Svetotulichka on 17.12.2017.
 */
@Controller
public class HomeController {

    private final GreetingService service;
    private final AtomicLong counter = new AtomicLong();

    public HomeController(GreetingService service) {
        this.service = service;
    }

    @RequestMapping("/")
    @ResponseBody
    public String greeting() {
        return service.greet();
    }

    @RequestMapping(value = "/api/greet", method = RequestMethod.GET)
    @ResponseBody
    public Greeting sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), "Hello, " + name);
    }

    @RequestMapping(value = "/ui/greet", method = RequestMethod.GET)
    public String uiGreet(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name, Model model) {
        model.addAttribute("name", name);
        return "greet";
    }

    @RequestMapping(value = "/ui/redirect", method = RequestMethod.GET)
    public String redirect(final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("name", "Guest");
        return "redirect:ui/guest/greet";
    }


    @RequestMapping(value="/ui/guest/greet", method=RequestMethod.GET)
    public String uiGreetGuest(@ModelAttribute("name") String name) {
        return "greet";
    }
}
