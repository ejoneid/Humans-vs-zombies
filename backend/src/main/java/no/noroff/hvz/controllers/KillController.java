package no.noroff.hvz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game/{gameID}/kill")
public class KillController {



    @GetMapping
    public ResponseEntity<> getAllKills(@PathVariable Long gameID) {

    }

    @GetMapping("/{killID}")
    public ResponseEntity<> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {

    }

    @PostMapping
    public ResponseEntity<> createNewKill(@PathVariable Long gameID) {

    }

    @PutMapping("/{killID}")
    public ResponseEntity<> updateKill(@PathVariable Long gameID, @PathVariable Long killID) {

    }

    @DeleteMapping("/{killID}")
    public ResponseEntity<> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {

    }
}
