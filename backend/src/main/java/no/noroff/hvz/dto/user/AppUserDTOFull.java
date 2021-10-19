package no.noroff.hvz.dto.user;

import no.noroff.hvz.dto.player.PlayerDTOFull;
import no.noroff.hvz.models.Player;

import java.util.Set;

public class AppUserDTOFull implements AppUserDTO{
    private Long id;
    private String firstName;
    private String lastName;
    private Set<PlayerDTOFull> players;

    public AppUserDTOFull(Long id, String firstName, String lastName, Set<PlayerDTOFull> players) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<PlayerDTOFull> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerDTOFull> players) {
        this.players = players;
    }
}
