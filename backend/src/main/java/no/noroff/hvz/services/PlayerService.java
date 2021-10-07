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

    public List<Player> getAllPlayers(Long gameID) {
        List<Player> players = new ArrayList<>();
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.findById(gameID).get();
            players = new ArrayList<>(game.getPlayers());
        }
        return players;
    }

    public Player getSpecificPlayer( Long gameID, Long playerID) {
        Player player = new Player();
        if(playerRepository.existsById(playerID) && gameRepository.existsById(gameID)) {
            player = playerRepository.findById(playerID).get();
        }
        return player;
    }

    public Player createNewPlayer(Long gameID, Player player) {
        Player newPlayer = new Player();
        if(gameRepository.existsById(gameID)) {
            newPlayer = playerRepository.save(player);
        }
        return newPlayer;
    }

    public Player updatePlayer(Long gameID, Long playerID, Player player) {
        Player updatedPlayer = new Player();
        if(playerRepository.existsById(playerID) && gameRepository.existsById(gameID)) {
            updatedPlayer = playerRepository.save(player);
        }
        return updatedPlayer ;
    }

    public Player deletePlayer(Long gameID, Long playerID) {
        Player deletedPlayer = new Player();
        if(playerRepository.existsById(playerID) && gameRepository.existsById(gameID)) {
            deletedPlayer = playerRepository.findById(playerID).get();
            playerRepository.deleteById(playerID);
        }
        return deletedPlayer;
    }
}
