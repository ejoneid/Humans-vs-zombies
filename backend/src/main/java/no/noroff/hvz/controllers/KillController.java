package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Kill;
import no.noroff.hvz.services.KillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/kill")
public class KillController {

    @Autowired
    private KillerService killerService;

    @GetMapping
    public ResponseEntity<List<Kill>> getAllKills(@PathVariable Long gameID) {
        return killerService.getAllKills(gameID);
    }

    @GetMapping("/{killID}")
    public ResponseEntity<Kill> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {
        return killerService.getSpecificKill(gameID, killID);
    }

    @PostMapping
    public ResponseEntity<Kill> createNewKill(@PathVariable Long gameID, @RequestBody Kill kill) {
        return killerService.createNewKill(gameID, kill);
    }

    @PutMapping("/{killID}")
    public ResponseEntity<Kill> updateKill(@PathVariable Long gameID, @PathVariable Long killID, @RequestBody Kill kill) {
        return killerService.updateKill(gameID, killID, kill);
    }

    @DeleteMapping("/{killID}")
    public ResponseEntity<Kill> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {
        return killerService.deleteKill(gameID, killID);
    }
}
