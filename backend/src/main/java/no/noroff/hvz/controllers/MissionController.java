package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Mission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/mission")
public class MissionController {



    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions(@PathVariable Long gameID) {
        return null;
    }

    @GetMapping("/{missionID}")
    public ResponseEntity<Mission> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Mission> createNewMission(@PathVariable Long gameID) {
        return null;
    }

    @PutMapping("/{missionID}")
    public ResponseEntity<Mission> updateMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        return null;
    }

    @DeleteMapping("/{missionID}")
    public ResponseEntity<Mission> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        return null;
    }
}
