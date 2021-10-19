package no.noroff.hvz.dto.user;

import no.noroff.hvz.dto.player.PlayerDTOFull;
import no.noroff.hvz.models.Player;

import java.util.Set;

public class AppUserDTOFull {
    private String openID;
    private String firstName;
    private String lastName;
    private Set<PlayerDTOFull> players;

    public AppUserDTOFull(String openID, String firstName, String lastName, Set<PlayerDTOFull> players) {
        this.openID = openID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.players = players;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
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
