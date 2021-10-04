package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/player")
public class PlayerController {



    @GetMapping
    public ResponseEntity<Set<Player>> getAllPlayers(@PathVariable Long gameID) {
        return null;
    }

    @GetMapping("/{playerID}")
    public ResponseEntity<Player> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Player> createNewPlayer(@PathVariable Long gameID) {
        return null;
    }

    @PutMapping("/{playerID}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return null;
    }

    @DeleteMapping("/{playerID}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return null;
    }
}
