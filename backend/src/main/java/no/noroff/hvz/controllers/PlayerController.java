package no.noroff.hvz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game/{gameID}/player")
public class PlayerController {



    @GetMapping
    public ResponseEntity<> getAllPlayers(@PathVariable Long gameID) {

    }

    @GetMapping("/{playerID}")
    public ResponseEntity<> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID) {

    }

    @PostMapping
    public ResponseEntity<> createNewPlayer(@PathVariable Long gameID) {

    }

    @PutMapping("/{playerID}")
    public ResponseEntity<> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {

    }

    @DeleteMapping("/{playerID}")
    public ResponseEntity<> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {

    }
}
