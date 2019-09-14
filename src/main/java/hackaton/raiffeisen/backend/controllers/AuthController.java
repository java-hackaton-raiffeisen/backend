package hackaton.raiffeisen.backend.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping(path = "/signup")
    public void signUp(){

    }

    @PostMapping(path = "/login")
    public void login(){

    }

}
