package no.noroff.hvz.controllers;

import no.noroff.hvz.models.Mission;
import no.noroff.hvz.services.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/game/{gameID}/mission")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions(@PathVariable Long gameID) {
        return missionService.getAllMissions(gameID);
    }

    @GetMapping("/{missionID}")
    public ResponseEntity<Mission> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        return missionService.getSpecificMission(gameID,missionID);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Mission> createNewMission(@PathVariable Long gameID, @RequestBody Mission mission) {
        return missionService.createNewMission(gameID, mission);
    }

    @PutMapping("/{missionID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Mission> updateMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestBody Mission mission) {
        return missionService.updateMission(gameID, missionID, mission);
    }

    @DeleteMapping("/{missionID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Mission> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        return missionService.deleteMission(gameID, missionID);
    }
}
