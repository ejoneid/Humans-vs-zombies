package no.noroff.hvz.controllers;

import no.noroff.hvz.dto.GameDTO;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames(@RequestHeader(required=false) String state) {
        List<GameDTO> games = new ArrayList<>();
        if (state != null) {
            games = gameService.getAllGames(state).stream().map(mapper::toGameTDO).collect(Collectors.toList());
        } else {
            games = gameService.getAllGames().stream().map(mapper::toGameTDO).collect(Collectors.toList());
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(games, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getSpecificGame(@PathVariable Long id) {
        HttpStatus status;
        Game game = gameService.getSpecificGame(id);
        if(game.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toGameTDO(game),status);
    }

    @PostMapping
    public ResponseEntity<GameDTO> createNewGame(@RequestBody Game game) {
        Game addedGame = gameService.createNewGame(game);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toGameTDO(addedGame), status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateSpecificGame(@PathVariable Long id, @RequestBody Game game) {
        HttpStatus status;
        if(!Objects.equals(id,game.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(mapper.toGameTDO(new Game()),status);
        }
        Game updatedGame = gameService.updateSpecificGame(id, game);
        if(updatedGame.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(mapper.toGameTDO(updatedGame),status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Game> deleteGame(@PathVariable Long id) {
        HttpStatus status;
        Game deletedGame = gameService.deleteGame(id);
        if(deletedGame.getId() == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(deletedGame, status);

    }

    @GetMapping("/{id}/chat")
    public ResponseEntity<List<Message>> getGameChat(@PathVariable Long id, @RequestHeader(required = false) Long playerID, @RequestHeader(required = false) Boolean human) {
        HttpStatus status;
        List<Message> messages = new ArrayList<>();
//        if (playerID.isPresent()) messages = gameService.getGameChat(id, playerID.get());
//        else if (human.isPresent()) messages = gameService.getGameChat(id, human.get());
        if (playerID != null) messages = gameService.getGameChat(id, playerID);
        else if (human != null) messages = gameService.getGameChat(id, human);
        else messages = gameService.getGameChat(id);
        if( messages == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(messages,status);
    }
}
