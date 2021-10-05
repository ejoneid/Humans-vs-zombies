package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Player;
import no.noroff.hvz.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers(@PathVariable Long gameID) {
        return playerService.getAllPlayers(gameID);
    }

    @GetMapping("/{playerID}")
    public ResponseEntity<Player> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return playerService.getSpecificPlayer(gameID, playerID);
    }

    @PostMapping
    public ResponseEntity<Player> createNewPlayer(@PathVariable Long gameID, @RequestBody Player player) {
        return playerService.createNewPlayer(gameID, player);
    }

    @PutMapping("/{playerID}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID, @RequestBody Player player) {
        return playerService.updatePlayer(gameID, playerID, player);
    }

    @DeleteMapping("/{playerID}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        return playerService.deletePlayer(gameID, playerID);
    }
}
