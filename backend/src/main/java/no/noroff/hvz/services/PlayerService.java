package no.noroff.hvz.services;

import no.noroff.hvz.dto.player.PlayerDTOPUT;
import no.noroff.hvz.mapper.CustomMapper;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import no.noroff.hvz.repositories.SquadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SquadRepository squadRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CustomMapper customMapper;

    public Set<Player> getAllPlayers(Long gameID) {
        Game game = gameRepository.findById(gameID).get();
        Set<Player> players = game.getPlayers();
        return players;
    }

    public Player getSpecificPlayer( Long gameID, Long playerID) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        return playerRepository.findById(playerID).get();
    }

    public Player getPlayerByGameAndUser(Long gameId, AppUser user) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndUser(game,user)) {
            player = playerRepository.getPlayerByGameAndUser(game,user);
        }
        return player;
    }

    public Player getPlayerByGameAndBiteCode(Long gameId, String biteCode) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndBiteCode(game,biteCode)) {
            player = playerRepository.getPlayerByGameAndBiteCode(game,biteCode);
        }
        return player;
    }

    /**
     * Method for putting in default info to a new user generated player
     * @param gameID
     * @param player
     * @exception NoSuchElementException
     * @return player
     */
    public Player createNewPlayer(Long gameID, Player player) {
        Game game = gameRepository.findById(gameID).get();
        player.setGame(game);
        player.setBiteCode(createRandomBiteCode(10));
        player = playerRepository.save(player);
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

    public Player updatePlayer(Long gameID, Long playerID, PlayerDTOPUT playerDto) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Player player = playerRepository.findById(playerID).get();
        customMapper.updatePlayerFromDto(playerDto, player);
        return playerRepository.save(player);
    }

    public Player deletePlayer(Long gameID, Long playerID) {
        Player deletedPlayer = new Player();
        if(playerRepository.existsById(playerID) && gameRepository.existsById(gameID)) {
            deletedPlayer = playerRepository.findById(playerID).get();
            playerRepository.deleteById(playerID);
        }
        return deletedPlayer;
    }

    public Squad getPlayerSquad(Long gameID, Long playerID) {
        Squad squad = null;
        if (gameRepository.existsById(gameID) && playerRepository.existsById(playerID)) {
            List<Squad> squads = squadRepository.findAll().stream().toList();
            for (Squad s : squads) {
                List<SquadMember> members = s.getMembers().stream().toList();
                for (SquadMember m : members) {
                    if (Objects.equals(m.getPlayer().getId(), playerID)) {
                        squad = s;
                        break;
                    }
                }
            }
        }
        return squad;
    }
}
