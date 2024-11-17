package pk.ztp.filmbase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationTestController {

    @GetMapping("/test")
    public String publicTest() {
        return "public test";
    }

    @GetMapping("/user/test")
    public String securedTest() {
        return "secured test";
    }
}
