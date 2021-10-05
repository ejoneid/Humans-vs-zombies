package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Message;
import no.noroff.hvz.models.Squad;
import no.noroff.hvz.models.SquadCheckIn;
import no.noroff.hvz.models.SquadMember;
import no.noroff.hvz.services.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/squad")
public class SquadController {

    @Autowired
    private SquadService squadService;

    @GetMapping
    public ResponseEntity<List<Squad>> getAllSquads(@PathVariable Long gameID) {
        return squadService.getAllSquads(gameID);
    }

    @GetMapping("/{squadID}")
    public ResponseEntity<Squad> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return squadService.getSpecificSquad(gameID, squadID);
    }

    @PostMapping
    public ResponseEntity<Squad> createNewSquad(@PathVariable Long gameID, @RequestBody Squad squad) {
        return squadService.createNewSquad(gameID, squad);
    }

    @PostMapping("/{squadID}/join")
    public ResponseEntity<Squad> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMember member) {
        return squadService.joinSquad(gameID, squadID, member);
    }

    @PutMapping("/{squadID}")
    public ResponseEntity<Squad> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody Squad squad) {
        return squadService.updateSquad(gameID, squadID, squad);
    }

    @DeleteMapping("/{squadID}")
    public ResponseEntity<Squad> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        return squadService.deleteSquad(gameID, squadID);
    }

    @GetMapping("/{squadID}/chat")
    public ResponseEntity<List<Message>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID) {
        return squadService.getSquadChat(gameID, squadID);
    }

    @PostMapping("/{squadID}/chat")
    public ResponseEntity<Message> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody Message message) {
        return squadService.createSquadChat(gameID, squadID, message);
    }

    @GetMapping("/{squadID}/check-in")
    public ResponseEntity<List<SquadCheckIn>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID) {
        return squadService.getSquadCheckIn(gameID, squadID);
    }

    @PostMapping("/{squadID}/check-in")
    public ResponseEntity<SquadCheckIn> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadCheckIn checkin) {
        return squadService.createSquadCheckIn(gameID, squadID, checkin);
    }
}
