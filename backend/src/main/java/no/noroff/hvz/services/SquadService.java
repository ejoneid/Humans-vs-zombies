package no.noroff.hvz.services;

import no.noroff.hvz.dto.squad.SquadDTOUpdate;
import no.noroff.hvz.mapper.CustomMapper;
import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SquadService {

    @Autowired
    SquadRepository squadRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    SquadCheckInRepository squadCheckInRepository;

    @Autowired
    SquadMemberRepository squadMemberRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    CustomMapper customMapper;

    public List<Squad> getAllSquads(Long gameID) {
        List<Squad> squads = new ArrayList<>();
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.getById(gameID);
            squads = new ArrayList<>(game.getSquads());
        }
        return squads;
    }



    public Squad getSpecificSquad(Long gameID, Long squadID) {
        if (!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        return squadRepository.findById(squadID).get();
    }

    public Squad getSquadByPlayer(Long gameID, Long playerID) {
        Squad squad = new Squad();
        if (playerRepository.existsById(playerID) && gameRepository.existsById(gameID)) {
            SquadMember member= squadMemberRepository.getByPlayer(playerRepository.getById(playerID));
            squad = squadRepository.getById(member.getSquad().getId());
        }
        return squad;
    }

    public Squad createNewSquad(Long gameID, Squad squad) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        squad.setGame(gameRepository.getById(gameID));
        Squad createdSquad = squadRepository.save(squad);
        return createdSquad;
    }

    public SquadMember joinSquad(Long gameID, Long squadID, SquadMember member) {
        SquadMember addedSquadMember = null;
        if(squadRepository.existsById(squadID) && gameRepository.existsById(gameID)) {
            member.setSquad(squadRepository.getById(squadID));
            addedSquadMember = squadMemberRepository.save(member);
        }
        return addedSquadMember;
    }

    public Squad updateSquad(Long gameID, Long squadID, SquadDTOUpdate squadDTO) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Squad updatedSquad = squadRepository.findById(squadID).get();
        customMapper.updateSquadFromDto(squadDTO, updatedSquad);
        updatedSquad = squadRepository.save(updatedSquad);
        return updatedSquad;
    }

    public Squad deleteSquad(Long gameID, Long squadID) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Squad deletedSquad = squadRepository.findById(squadID).get();
        squadRepository.deleteById(squadID);
        return deletedSquad;
    }

    public List<Message> getSquadChat(Long gameID, Long squadID) {
        List<Message> chat = null;
        if(gameRepository.existsById(gameID) && squadRepository.existsById(squadID)) {
            Squad squad = squadRepository.getById(squadID);
            chat = squad.getMessages().stream()
                    .sorted(Comparator.comparing(Message::getChatTime)).collect(Collectors.toList());
        }
        return chat;
    }

    public Message createSquadChat(Long gameID, Long squadID, AppUser user, Message message) {
        Message chat = null;
        if(!(gameRepository.existsById(gameID) && squadRepository.existsById(squadID))) throw new NoSuchElementException();
        //TODO fiks dette, skal ikke være nødvendig hvis message objektet lages fra messageDTO
        message.setGame(gameRepository.getById(gameID));
        message.setSquad(squadRepository.getById(squadID));
        message.setUser(user);
        message.setHuman(playerRepository.getPlayerByGameAndUser(gameRepository.getById(gameID),user).isHuman());
        message.setGlobal(false);
        message.setFaction(false);
        message.setChatTime(new Date());
        chat = messageRepository.save(message);
        return chat;
    }

    public List<SquadCheckIn> getSquadCheckIn(Long gameID, Long squadID) {
        List<SquadCheckIn> checkins = null;
        if(gameRepository.existsById(gameID) && squadRepository.existsById(squadID)) {
            Squad squad = squadRepository.getById(squadID);
            Set<SquadMember> members = squad.getMembers();
            for (SquadMember m : members) {
                checkins.addAll(m.getCheckIns());
            }
        }
        return checkins;
    }

    public SquadCheckIn createSquadCheckIn(Long gameID, Long squadID, SquadCheckIn checkin) {
        SquadCheckIn addedSquadCheckIn = null;
        if(gameRepository.existsById(gameID) && squadRepository.existsById(squadID)) {
            addedSquadCheckIn = squadCheckInRepository.save(checkin);
        }
        return addedSquadCheckIn;
    }

    public boolean isMemberOfSquad(Squad squad, Player player){
        for(SquadMember member : squad.getMembers()) {
            if(member.getPlayer().getId().equals(player.getId())) {
                return true;
            }
        }
        return false;
    }

    public SquadMember getSquadMemberByPlayer(Player player) {
        return squadMemberRepository.getByPlayer(player);
    }
}
