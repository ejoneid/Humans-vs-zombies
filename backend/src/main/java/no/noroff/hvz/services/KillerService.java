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

@Service
public class KillerService {

    @Autowired
    private KillerRepository killerRepository;
    @Autowired
    private GameRepository gameRepository;

    public ResponseEntity<List<Kill>> getAllKills(Long gameID) {
        HttpStatus status;
        List<Kill> kills = new ArrayList<>();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(kills,status);
        }
        Game game = gameRepository.findById(gameID).get();
        kills = new ArrayList<>(game.getKills());
        status = HttpStatus.OK;
        return new ResponseEntity<>(kills, status);
    }

    public ResponseEntity<Kill> getSpecificKill( Long gameID, Long killID) {
        HttpStatus status;
        Kill kill = new Kill();
        if(!gameRepository.existsById(gameID) || !killerRepository.existsById(killID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(kill,status);
        }
        kill = killerRepository.findById(killID).get();
        status = HttpStatus.OK;
        return new ResponseEntity<>(kill, status);
    }

    public ResponseEntity<Kill> createNewKill(Long gameID, Kill kill) {
        HttpStatus status;
        Kill addedKill = new Kill();
        if(!gameRepository.existsById(gameID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(addedKill,status);
        }
        addedKill = killerRepository.save(kill);
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedKill, status);
    }

    public ResponseEntity<Kill> updateKill(Long gameID, Long killID, Kill kill) {
        HttpStatus status;
        Kill updatedKill = new Kill();
        if(!gameRepository.existsById(gameID) || !killerRepository.existsById(killID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(updatedKill,status);
        }
        if(!Objects.equals(killID,kill.getId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(updatedKill,status);
        }
        updatedKill = killerRepository.save(kill);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedKill, status);
    }

    public ResponseEntity<Kill> deleteKill(Long gameID, Long killID) {
        HttpStatus status;
        Kill deletedKill = new Kill();
        if (!gameRepository.existsById(gameID) || !killerRepository.existsById(killID)) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(deletedKill, status);
        }
        deletedKill = killerRepository.findById(killID).get();
        killerRepository.deleteById(killID);
        status = HttpStatus.OK;
        return new ResponseEntity<>(deletedKill, status);
    }
}
