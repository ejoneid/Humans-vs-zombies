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
import java.util.stream.Collectors;

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

    /**
     * Method for putting in default info to a new user generated player
     * @param gameID
     * @param player
     * @return player || null
     */
    public Player createNewPlayer(Long gameID, Player player) {
        if(gameRepository.existsById(gameID)) {
            // Set the players default info
            player.setGame(gameRepository.findById(gameID).get());
            String randomBiteCode = createRandomBiteCode(10);
            // Create a random bitecode -> if the bitecode already exist creates a new one
            String finalRandomBiteCode = randomBiteCode;
            List<Player> existingBiteCode = playerRepository.findAll().stream().filter(p -> Objects.equals(p.getBiteCode(), finalRandomBiteCode)).collect(Collectors.toList());
            while (existingBiteCode.size() > 0) {
                randomBiteCode = createRandomBiteCode(10);
                String finalRandomBiteCode1 = randomBiteCode;
                existingBiteCode = playerRepository.findAll().stream().filter(p -> Objects.equals(p.getBiteCode(), finalRandomBiteCode1)).collect(Collectors.toList());
            }
            player.setBiteCode(randomBiteCode);
            player = playerRepository.save(player);
        }
        // If game exist -> return the new player, else return null
        return player;
    }

    /**
     * Method for creating a random bitecode
     * @param len
     * @return biteCode
     */
    public static String createRandomBiteCode(int len) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int v = 1 + (int) (Math.random() * 26);
            char c = (char) (v + 'a' - 1);
            name.append(c);
        }
        return name.toString();
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
