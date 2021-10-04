package no.noroff.hvz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game/{gameID}/squad")
public class SquadController {



    @GetMapping
    public ResponseEntity<> getAllSquads(@PathVariable Long gameID) {

    }

    @GetMapping("/{squadID}")
    public ResponseEntity<> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @PostMapping
    public ResponseEntity<> createNewSquad(@PathVariable Long gameID) {

    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @PutMapping("/{squadID}")
    public ResponseEntity<> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @DeleteMapping("/{squadID}")
    public ResponseEntity<> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @GetMapping("/{squadID}/chat")
    public ResponseEntity<> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @PostMapping("/{squadID}/chat")
    public ResponseEntity<> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @GetMapping("/{squadID}/check-in")
    public ResponseEntity<> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {

    }

    @PostMapping("/{squadID}/check-in")
    public ResponseEntity<> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {

    }
}
