package no.noroff.hvz.services;

import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.MessageRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    MessageRepository messageRepository;

    /**
     * Method for getting all games from the DB
     * @return List og games
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    /**
     * Method for getting a list of games with a specific state
     * @param state
     * @return List of games
     */
    public List<Game> getAllGames(String state) {
        return gameRepository.findAll().stream()
                .filter(g -> Objects.equals(g.getGameState(), state))
                .collect(Collectors.toList());
    }

    /**
     * Method for getting a specific game based on ID
     * @param id
     * @return The requested game
     * @throws NoSuchElementException
     */
    public Game getSpecificGame(Long id) throws NoSuchElementException {
        return gameRepository.findById(id).get();
    }

    /**
     * Method for creating a new game
     * @param game
     * @return The saved game/error
     */
    public Game createNewGame(Game game) {
        return gameRepository.save(game);
    }

    public Game updateSpecificGame( Long id, Game game) throws NoSuchElementException {
        if(!gameRepository.existsById(id)) {
            throw new NoSuchElementException("Did not find game with id of " + id);
        }
        return gameRepository.save(game);
    }

    /**
     * Method for deleting a game from the DB
     * @param id
     * @return The deleted game
     * @throws NoSuchElementException
     */
    public Game deleteGame(Long id) throws NoSuchElementException {
        if(!gameRepository.existsById(id)) {
            throw new NoSuchElementException("Did not find game with id of " + id);
        }
        Game deletedGame = gameRepository.findById(id).get();
        gameRepository.deleteById(id);
        return deletedGame;
    }

    /**
     * Method for getting the global chat from a game
     * @param id
     * @return List of messages
     */
    public List<Message> getGameChat(Long id) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return gameRepository.findById(id).get().getMessages()
                // Filter messages by global and faction param
                .stream().filter(Message::isGlobal).filter(m -> !m.isFaction())
                // Sort messages by time sent
                .sorted(Comparator.comparing(Message::getChatTime))
                .collect(Collectors.toList());
    }

    /**
     * Method for getting a players sent messages
     * @param id
     * @param playerID
     * @return List of messages
     */
    public List<Message> getGameChat(Long id, Long playerID) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return gameRepository.findById(id).get().getMessages()
                // Filter by playerID
                .stream().filter(g -> Objects.equals(g.getUser().getId(), playerID))
                // Sort by time sent
                .sorted(Comparator.comparing(Message::getChatTime))
                .collect(Collectors.toList());
    }

    /**
     * Method for getting a faction chat
     * @param id
     * @param human
     * @return List of messages
     */
    public List<Message> getGameChat(Long id, Boolean human) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return gameRepository.findById(id).get().getMessages()
                // Filter messages by human param and the state of the player who sent the message
                .stream().filter(g -> Objects.equals(
                        playerRepository.getPlayerByGameAndUser(gameRepository.getById(id),g.getUser()).isHuman(), human))
                // Filter messages by global and faction
                .filter(Message::isGlobal).filter(Message::isFaction)
                // Sort by time sent
                .sorted(Comparator.comparing(Message::getChatTime))
                .collect(Collectors.toList());
    }

    /**
     * Method for sending a chat message
     * @param id
     * @param message
     * @param user
     * @return The saved message/error
     */
    public Message createNewChat(Long id, Message message, AppUser user) {
        if (!gameRepository.existsById(id)) {
            throw new NoSuchElementException("Did not find game with id of " + id);
        }
        Game game = gameRepository.findById(id).get();
        // Adds data to the  message
        message.setGame(game);
        message.setChatTime(new Date());
        message.setGlobal(true);
        if (user != null) {
            message.setUser(user);
            message.setHuman(playerRepository.getPlayerByGameAndUser(game,user).isHuman());
        }
        return messageRepository.save(message);
    }
}
