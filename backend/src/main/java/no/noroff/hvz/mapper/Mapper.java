package no.noroff.hvz.mapper;

import no.noroff.hvz.dto.*;
import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.AppUserRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import no.noroff.hvz.repositories.SquadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Mapper {

    private final String url = "/api/game/";

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SquadRepository squadRepository;

    @Autowired
    AppUserRepository appUserRepository;

    public MissionDTO toMissionDTO(Mission mission) {
        return new MissionDTO(mission.getId(),mission.getName(),mission.getDescription(),
                mission.getStartTime(), mission.getEndTime(), mission.isHuman(), mission.getLat(), mission.getLng());
    }

    public AppUserDTO toAppUserDTO(AppUser user) {
        return new AppUserDTO(user.getId(), user.getFirstName(), user.getLastName());
    }

    public AppUser toAppUser(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setId(appUserDTO.getId());
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setFirstName(appUserDTO.getFirstName());
        return appUser;
    }

    public GameDTO toGameTDO(Game game) {
        String gameUrl = url + game.getId();
        String squadsUrl = gameUrl + "/squad";
        String missionsUrl = gameUrl + "/mission";
        String killsUrl = gameUrl + "/kill";
        String chatUrl = gameUrl + "/chat";
        String playersUrl = gameUrl + "/player";
        return new GameDTO(game.getId(), game.getName(),game.getGameState(), game.getDescription(), game.getNw_lat(), game.getSe_lat(),
                game.getNw_long(),game.getSe_long(),squadsUrl, missionsUrl, killsUrl, chatUrl, playersUrl);
    }

    public KillDTO toKillTDO(Kill kill) {
        String killerName = kill.getKiller().getUser().getFirstName() + " " + kill.getKiller().getUser().getLastName();
        String victimName = kill.getVictim().getUser().getFirstName() + " " + kill.getVictim().getUser().getLastName();
        return new KillDTO(kill.getId(), kill.getTimeOfDeath(), kill.getStory(), kill.getLat(), kill.getLng(),
                killerName, victimName);
    }

    public Kill regKillDTO(RegKillDTO killDTO) {
        Kill kill = new Kill();
        Player killer = playerRepository.findById(killDTO.getKillerID()).get();
        List<Player> victim = playerRepository.findAll().stream().filter(p -> Objects.equals(p.getBiteCode(), killDTO.getBiteCode())).collect(Collectors.toList());
        if (victim.size() == 0) return null;
        kill.setKiller(killer);
        kill.setVictim(victim.get(0));
        kill.setTimeOfDeath(new Date());
        kill.setStory(killDTO.getStory());
        kill.setLat(killDTO.getLat());
        kill.setLng(killDTO.getLng());
        return kill;
    }

    public MessageDTO toMessageDTO(Message message) {
        String playerUrl = url + message.getGame().getId() + "/player/" + message.getPlayer().getId();
        return new MessageDTO(message.getId(), message.getMessage(),message.getChatTime(),playerUrl);
    }

    public PlayerDTOStandard toPlayerDTOStandard(Player player) {
        String killsUrl = url + player.getGame().getId() + "/kill/"; //TODO legge til searc parameter så vi får riktige kills
        return new PlayerDTOStandard(player.getId(),player.isHuman(),killsUrl, toAppUserDTO(player.getUser()));
    }

    public PlayerDTO toPlayerDTOFull(Player player) {
        String killsUrl = url + player.getGame().getId() + "/kill/"; //TODO legge til searc parameter så vi får riktige kills
        String messagesUrl = url + player.getGame().getId() + "/chat/"; //TODO legge til searc parameter så vi får riktige messages
        AppUserDTO userDTO = toAppUserDTO(player.getUser());
        return new PlayerDTOFull(player.getId(),player.isHuman(), player.getBiteCode(),
               userDTO ,killsUrl,messagesUrl);
    }

    public Player regPlayerDTO(RegPlayerDTO p) {
        Player player = new Player();
        if (!appUserRepository.existsById(p.getUserID())) return null;
        AppUser user = appUserRepository.findById(p.getUserID()).get();
        player.setUser(user);
        player.setHuman(true);
        player.setPatientZero(false);
        return player;
    }

    public SquadMemberDTO toSquadMemberDTO (SquadMember squadMember) {
        String playerUrl = url + squadMember.getSquad().getGame().getId() + "/player/" + squadMember.getPlayer().getId();
        String checkInsUrl = url + squadMember.getSquad().getGame().getId() + "/check-in/"; //TODO legge til searc parameter så vi får riktige checkIns
        return  new SquadMemberDTO(squadMember.getId(), squadMember.getRank(), playerUrl, checkInsUrl);
    }

    public SquadMember toSquadMember (SquadMemberFromDTO dto) {
        SquadMember member = new SquadMember();
        Player player = playerRepository.findById(dto.getPlayerID()).get();
        member.setPlayer(player);
        member.setRank(dto.getRank());
        return member;
    }

    public SquadDTO toSquadDTO(Squad squad) {
        String messagesUrl = url + squad.getGame().getId() + "/squad/" + squad.getId() + "/chat";
        String squadType;
        ArrayList<SquadMemberDTO> members = new ArrayList<>();
        for (SquadMember member: squad.getMembers()) {
            members.add(toSquadMemberDTO(member));
        }
        if(squad.isHuman()) {
            squadType = "Human";
        }
        else {
            squadType = "Zombie";
        }
        return new SquadDTO(squad.getId(), squad.getName(),squadType,messagesUrl,members);
    }

    public SquadCheckInDTO toSquadCheckInDTO(SquadCheckIn squadCheckIn) {
        SquadMemberDTO memberDTO = toSquadMemberDTO(squadCheckIn.getMember());
        return new SquadCheckInDTO(squadCheckIn.getId(), squadCheckIn.getStartTime(), squadCheckIn.getEndTime(),
                squadCheckIn.getLat(), squadCheckIn.getLng(), memberDTO);
    }

    public SquadViewDTO toSquadViewDTO(Squad squad) {
        String messagesUrl = url + squad.getGame().getId() + "/squad/" + squad.getId() + "/chat";
        List<PlayerViewDTO> players = new ArrayList<>();
        for (SquadMember m : squad.getMembers()) {
            players.add(toPlayerViewDTO(m.getPlayer()));
        }
        return new SquadViewDTO(squad.getId(), squad.getName(), messagesUrl, players);
    }

    public PlayerViewDTO toPlayerViewDTO(Player player) {
        return new PlayerViewDTO(player.getUser().getFirstName() + " " + player.getUser().getLastName(), player.isHuman());
    }
}
