package com.example.unoapi.controller;

import com.example.unoapi.service.UnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class UnoController {
    @Autowired
    UnoService unoService;

    @GetMapping("/prubeaMapping")
    public ResponseEntity<String> holaMundo(){
        return new ResponseEntity<>("Los fantasticos alumnos de la tarde", HttpStatus.OK);
    }
    @PostMapping ("newmatch")
    public ResponseEntity newmatch(@RequestParam List<String> players){
        return ResponseEntity.ok(unoService.newMatch(players));
    }

    @GetMapping("playerhand/{matchId}")
    public ResponseEntity playerHand(@PathVariable UUID matchId){
        //tenemos que implementar el servicion playerhand utilizando el modelo
        return ResponseEntity.ok(unoService.playerHand(matchId).stream().map(each -> each.asJson()));
    }

    /*@PostMapping("play/{matchId}/{player}")
    public ResponseEntity play(@PathVariable UUID matchId, @PathVariable String player){

    } -> 3*/
    //drawcard -> 4
    //activecard -> 2
}


//la play no es la play del modelo
//hay que hacer un controller y los servicios que usa el controler Tambien tenemos que hacer los test de la comunicacion, ver que pasa.
