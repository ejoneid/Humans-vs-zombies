package no.noroff.hvz.dto.squad;

import no.noroff.hvz.dto.player.PlayerViewDTO;

import java.util.List;

public class SquadViewDTO {

    private Long id;
    private String name;
    private String messageURL;
    private List<PlayerViewDTO> players;

    public SquadViewDTO(Long id, String name, String messageURL, List<PlayerViewDTO> players) {
        this.id = id;
        this.name = name;
        this.messageURL = messageURL;
        this.players = players;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageURL() {
        return messageURL;
    }

    public void setMessageURL(String messageURL) {
        this.messageURL = messageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PlayerViewDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerViewDTO> players) {
        this.players = players;
    }
}
