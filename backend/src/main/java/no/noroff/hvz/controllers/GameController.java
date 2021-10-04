package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/game")
public class GameController {




    @GetMapping
    public ResponseEntity<Set<Game>> getAllGames() {
        return null;
    }

    @GetMapping("{/id}")
    public ResponseEntity<Game> getSpecificGame(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Game> createNewGame(@RequestBody Game game) {
        return null;
    }

    @PutMapping("{/id}")
    public ResponseEntity<Game> updateSpecificGame(@PathVariable Long id, @RequestBody Game game) {
        return null;
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Game> deleteGame(@PathVariable Long id) {
        return null;
    }

    @GetMapping("{/id}/chat")
    public ResponseEntity<Set<Message>> getGameChat(@PathVariable Long id) {
        return null;
    }
}
