package no.noroff.hvz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game/{gameID}/mission")
public class MissionController {



    @GetMapping
    public ResponseEntity<> getAllMissions(@PathVariable Long gameID) {

    }

    @GetMapping("/{missionID}")
    public ResponseEntity<> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID) {

    }

    @PostMapping
    public ResponseEntity<> createNewMission(@PathVariable Long gameID) {

    }

    @PutMapping("/{missionID}")
    public ResponseEntity<> updateMission(@PathVariable Long gameID, @PathVariable Long missionID) {

    }

    @DeleteMapping("/{missionID}")
    public ResponseEntity<> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {

    }
}
