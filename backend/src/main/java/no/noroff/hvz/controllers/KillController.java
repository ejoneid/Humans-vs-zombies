package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Kill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/kill")
public class KillController {



    @GetMapping
    public ResponseEntity<List<Kill>> getAllKills(@PathVariable Long gameID) {
        return null;
    }

    @GetMapping("/{killID}")
    public ResponseEntity<Kill> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Kill> createNewKill(@PathVariable Long gameID) {
        return null;
    }

    @PutMapping("/{killID}")
    public ResponseEntity<Kill> updateKill(@PathVariable Long gameID, @PathVariable Long killID) {
        return null;
    }

    @DeleteMapping("/{killID}")
    public ResponseEntity<Kill> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {
        return null;
    }
}
