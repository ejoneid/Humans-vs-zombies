package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.KillerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KillerService {

    @Autowired
    private KillerRepository killerRepository;
    @Autowired
    private GameRepository gameRepository;

    public List<Kill> getAllKills(Long gameID) {
        List<Kill> kills = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.findById(gameID).get();
            kills = new ArrayList<>(game.getKills());
        }
        return kills;
    }

    public List<Kill> getAllKills(Long gameID, Long killerID) {
        List<Kill> kills = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.findById(gameID).get();
            kills = game.getKills().stream().filter(k -> Objects.equals(k.getKiller().getId(), killerID)).collect(Collectors.toList());
        }
        return kills;
    }

    public Kill getSpecificKill( Long gameID, Long killID) {
        Kill kill = new Kill();
        if(gameRepository.existsById(gameID) && killerRepository.existsById(killID)) {
            kill = killerRepository.findById(killID).get();
        }
        return kill;
    }

    public Kill createNewKill(Long gameID, Kill kill) {
        if(gameRepository.existsById(gameID) && kill != null) {
            kill.setGame(gameRepository.findById(gameID).get());
            kill = killerRepository.save(kill);
        }
        return kill;
    }

    public Kill updateKill(Long gameID, Long killID, Kill kill) {

        Kill updatedKill = new Kill();
        if(gameRepository.existsById(gameID) && killerRepository.existsById(killID)) {
            updatedKill = killerRepository.save(kill);
        }
        return updatedKill;
    }

    public Kill deleteKill(Long gameID, Long killID) {
        Kill deletedKill = new Kill();
        if (gameRepository.existsById(gameID) && killerRepository.existsById(killID)) {
            deletedKill = killerRepository.findById(killID).get();
            killerRepository.deleteById(killID);
        }
        return deletedKill;
    }
}
