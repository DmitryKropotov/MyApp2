package com.tutorialspoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public static String printHello(/*ModelMap model, @RequestParam("accountName") String accNam*/) {
        //model.addAttribute("message", "Hello Spring MVC Framework!");
        return "hello";
    }
}
