package no.noroff.hvz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {




    @GetMapping
    public ResponseEntity<> getAllGames() {

    }

    @GetMapping("{/id}")
    public ResponseEntity<> getSpecificGame(@PathVariable Long id) {

    }

    @PostMapping
    public ResponseEntity<> createNewGame(@RequestBody Game game) {

    }

    @PutMapping("{/id}")
    public ResponseEntity<> updateSpecificGame(@PathVariable Long id, @RequestBody Game game) {

    }

    @DeleteMapping("{/id}")
    public ResponseEntity<> deleteGame(@PathVariable Long id) {

    }

    @GetMapping("{/id}/chat")
    public ResponseEntity<> getGameChat(@PathVariable Long id) {

    }
}
