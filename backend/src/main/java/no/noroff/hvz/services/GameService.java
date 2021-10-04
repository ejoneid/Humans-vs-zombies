package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameRepository.findAll();
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(games, status);
    }

    public ResponseEntity<Game> getSpecificGame(Long id) {
        HttpStatus status;
        Game game = new Game();
        if(!gameRepository.existsById(id)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(game,status);
        }
        game = gameRepository.findById(id).get();
        status = HttpStatus.OK;
        return new ResponseEntity<>(game,status);
    }

    public ResponseEntity<Game> createNewGame(Game game) {
        Game addedGame = gameRepository.save(game);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedGame, status);
    }

    public ResponseEntity<Game> updateSpecificGame( Long id, Game game) {
        HttpStatus status;
        Game updatedGame = new Game();
        if(!gameRepository.existsById(id)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(updatedGame,status);
        }
        if(!Objects.equals(id,game.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(updatedGame,status);
        }
        updatedGame = gameRepository.save(game);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedGame,status);
    }

    public ResponseEntity<Game> deleteGame(Long id) {
        HttpStatus status;
        Game deletedGame = new Game();
        if(!gameRepository.existsById(id)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(deletedGame,status);
        }
        deletedGame= gameRepository.findById(id).get();
        gameRepository.deleteById(id);
        status = HttpStatus.OK;
        return new ResponseEntity<>(deletedGame, status);
    }

    public ResponseEntity<List<Message>> getGameChat(Long id) {
        HttpStatus status;
        List<Message> messages = new ArrayList<>();
        if(!gameRepository.existsById(id)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(messages,status);
        }
        messages = new ArrayList<>(gameRepository.findById(id).get().getMessages());
        status = HttpStatus.OK;
        return new ResponseEntity<>(messages,status);
    }

}
