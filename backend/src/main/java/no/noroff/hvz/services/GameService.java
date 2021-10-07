package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.MessageRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    MessageRepository messageRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
    public List<Game> getAllGames(String state) {
        return gameRepository.findAll().stream().filter(g -> Objects.equals(g.getGameState(), state)).collect(Collectors.toList());
    }

    public Game getSpecificGame(Long id) {
        Game game = new Game();
        if(!gameRepository.existsById(id)) {
            return game;
        }
        game = gameRepository.findById(id).get();
        return game;
    }

    public Game createNewGame(Game game) {
        return gameRepository.save(game);
    }

    public Game updateSpecificGame( Long id, Game game) {
        Game updatedGame = new Game();
        if(!gameRepository.existsById(id)) {
            return updatedGame;
        }
        updatedGame = gameRepository.save(game);
        return updatedGame;
    }

    public Game deleteGame(Long id) {
        Game deletedGame = new Game();
        if(!gameRepository.existsById(id)) {
            return deletedGame;
        }
        deletedGame= gameRepository.findById(id).get();
        gameRepository.deleteById(id);
        return deletedGame;
    }

    public List<Message> getGameChat(Long id) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return new ArrayList<>(gameRepository.findById(id).get().getMessages());
    }

    public List<Message> getGameChat(Long id, Long playerID) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return gameRepository.findById(id).get().getMessages().stream().filter(g -> Objects.equals(g.getPlayer().getId(), playerID)).collect(Collectors.toList());
    }

    public List<Message> getGameChat(Long id, Boolean human) {
        if(!gameRepository.existsById(id)) {
            return null;
        }
        return gameRepository.findById(id).get().getMessages().stream().filter(g -> Objects.equals(g.getPlayer().isHuman(), human)).collect(Collectors.toList());
    }

    public Message createNewChat(Long id, Message message, Long playerID) {
        if (!gameRepository.existsById(id)) {
            return null;
        }
        Game game = gameRepository.findById(id).get();
        message.setGame(game);
        if (playerID != null) {
            message.setPlayer(playerRepository.findById(playerID).get());
        }
        messageRepository.save(message);
        Set<Message> chat = game.getMessages();
        chat.add(message);
        game.setMessages(chat);
        gameRepository.save(game);
        return message;
    }
}
