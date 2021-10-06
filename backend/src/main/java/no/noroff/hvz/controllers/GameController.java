package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getSpecificGame(@PathVariable Long id) {
        return gameService.getSpecificGame(id);
    }

    @PostMapping
    public ResponseEntity<Game> createNewGame(@RequestBody Game game) {
        return gameService.createNewGame(game);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateSpecificGame(@PathVariable Long id, @RequestBody Game game) {
        return gameService.updateSpecificGame(id, game);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Game> deleteGame(@PathVariable Long id) {
        return gameService.deleteGame(id);
    }

    @GetMapping("/{id}/chat")
    public ResponseEntity<List<Message>> getGameChat(@PathVariable Long id) {
        return gameService.getGameChat(id);
    }
}
