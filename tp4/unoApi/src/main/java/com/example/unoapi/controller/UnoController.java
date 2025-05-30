package com.example.unoapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UnoController {
    @GetMapping ("/")
    public String saludo(){
        return "index";
    }
}

//la play no es la play del modelo
//hay que hacer un controller y los servicios que usa el controler Tambien tenemos que hacer los test de la comunicacion, ver que pasa.
