package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;

    public ResponseEntity<List<Player>> getAllPlayers(Long gameID) {
        HttpStatus status;
        List<Player> players = new ArrayList<>();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(players,status);
        }
        Game game = gameRepository.findById(gameID).get();
        players = new ArrayList<>(game.getPlayers());
        status = HttpStatus.OK;
        return new ResponseEntity<>(players, status);
    }

    public ResponseEntity<Player> getSpecificPlayer( Long gameID, Long playerID) {
        HttpStatus status;
        Player player = new Player();
        if(!playerRepository.existsById(playerID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(player,status);
        }
        player = playerRepository.findById(playerID).get();
        status = HttpStatus.OK;
        return new ResponseEntity<>(player, status);
    }

    public ResponseEntity<Player> createNewPlayer(Long gameID, Player player) {
        HttpStatus status;
        Player newPlayer = new Player();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(newPlayer,status);
        }
        newPlayer = playerRepository.save(player);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(newPlayer,status);
    }

    public ResponseEntity<Player> updatePlayer(Long gameID, Long playerID, Player player) {
        HttpStatus status;
        Player updatedPlayer = new Player();
        if(!playerRepository.existsById(playerID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(updatedPlayer,status);
        }
        if(!Objects.equals(playerID,player.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(updatedPlayer,status);
        }
        updatedPlayer = playerRepository.save(player);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedPlayer, status);
    }

    public ResponseEntity<Player> deletePlayer(Long gameID, Long playerID) {
        HttpStatus status;
        Player deletedPlayer = new Player();
        if(!playerRepository.existsById(playerID) || !gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(deletedPlayer,status);
        }
        deletedPlayer = playerRepository.findById(playerID).get();
        playerRepository.deleteById(playerID);
        status = HttpStatus.OK;
        return new ResponseEntity<>(deletedPlayer, status);
    }
}
