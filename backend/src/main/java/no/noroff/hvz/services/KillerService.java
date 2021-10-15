package no.noroff.hvz.services;

import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.mapper.CustomMapper;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.KillerRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KillerService {

    @Autowired
    private KillerRepository killerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CustomMapper mapper;

    public List<Kill> getAllKills(Long gameID) {
        if(!gameRepository.existsById(gameID)) {
            throw new NoSuchElementException("Could not find game with id: " + gameID);
        }
        return killerRepository.getKillsByGame_Id(gameID);
    }

    public List<Kill> getAllKills(Long gameID, Long killerID) {
        if(!gameRepository.existsById(gameID)) {
            throw new NoSuchElementException("Could not find game with id: " + gameID);
        }
        return killerRepository.getKillsByGame_IdAndKiller_Id(gameID, killerID);
    }

    public Kill getSpecificKill( Long gameID, Long killID) {
        if (!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        return killerRepository.findById(killID).get();
    }

    public Kill createNewKill(Long gameID, Kill kill) {
        kill.setGame(gameRepository.findById(gameID).get());
        kill.getVictim().setHuman(false);
        if (kill.getTimeOfDeath() == null) kill.setTimeOfDeath(new Date());
        kill = killerRepository.save(kill);
        return kill;
    }

    public Kill updateKill(Long gameID, Long killID, KillDTOReg killDto) {

        Kill updatedKill = getSpecificKill(gameID, killID);
        mapper.updateKillFromDto(killDto, updatedKill);
        updatedKill.setKiller(playerRepository.findById(killDto.getKillerID()).get());
        updatedKill.setVictim(playerRepository.getPlayerByGame_IdAndBiteCode(gameID, killDto.getBiteCode()));
        return killerRepository.save(updatedKill);
    }

    public Kill deleteKill(Long gameID, Long killID) {
        if (!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Kill deletedKill = killerRepository.findById(killID).get();
        killerRepository.deleteById(killID);
        return deletedKill;
    }
}
