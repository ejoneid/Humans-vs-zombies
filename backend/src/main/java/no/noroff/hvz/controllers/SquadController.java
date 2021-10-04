package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Message;
import no.noroff.hvz.models.Squad;
import no.noroff.hvz.models.SquadCheckIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/squad")
public class SquadController {



    @GetMapping
    public ResponseEntity<List<Squad>> getAllSquads(@PathVariable Long gameID) {
        return null;
    }

    @GetMapping("/{squadID}")
    public ResponseEntity<Squad> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Squad> createNewSquad(@PathVariable Long gameID) {
        return null;
    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<Squad> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @PutMapping("/{squadID}")
    public ResponseEntity<Squad> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @DeleteMapping("/{squadID}")
    public ResponseEntity<Squad> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @GetMapping("/{squadID}/chat")
    public ResponseEntity<List<Message>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @PostMapping("/{squadID}/chat")
    public ResponseEntity<Message> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @GetMapping("/{squadID}/check-in")
    public ResponseEntity<List<SquadCheckIn>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }

    @PostMapping("/{squadID}/check-in")
    public ResponseEntity<SquadCheckIn> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {
        return null;
    }
}
