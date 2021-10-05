package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String gameState;

    @Column(length = 20)
    private String nw_lat;

    @Column(length = 20)
    private String se_lat;

    @Column(length = 20)
    private String nw_long;

    @Column(length = 20)
    private String se_long;

    @OneToMany
    @JoinColumn(name = "squad_id")
    private Set<Squad> squads;

    @JsonGetter("squads")
    public List<Long> squadGetter() {
        if (squads != null) {
            return squads.stream().map(Squad::getId).collect(Collectors.toList());
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "mission_id")
    private Set<Mission> missions;

    @JsonGetter("missions")
    public List<Long> missionsGetter() {
        if (missions != null) {
            return missions.stream().map(Mission::getId).collect(Collectors.toList());
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "kill_id")
    private Set<Kill> kills;

    @JsonGetter("kills")
    public List<Long> killsGetter() {
        if (kills != null) {
            return kills.stream().map(Kill::getId).collect(Collectors.toList());
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "message_id")
    private Set<Message> messages;

    @JsonGetter("messages")
    public List<Long> messagesGetter() {
        if (messages != null) {
            return messages.stream().map(Message::getId).collect(Collectors.toList());
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "player_id")
    private Set<Player> players;

    @JsonGetter("players")
    public List<Long> playersGetter() {
        if (players != null) {
            return players.stream().map(Player::getId).collect(Collectors.toList());
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getNw_lat() {
        return nw_lat;
    }

    public void setNw_lat(String nw_lat) {
        this.nw_lat = nw_lat;
    }

    public String getSe_lat() {
        return se_lat;
    }

    public void setSe_lat(String se_lat) {
        this.se_lat = se_lat;
    }

    public String getNw_long() {
        return nw_long;
    }

    public void setNw_long(String nw_long) {
        this.nw_long = nw_long;
    }

    public String getSe_long() {
        return se_long;
    }

    public void setSe_long(String se_long) {
        this.se_long = se_long;
    }

    public Set<Squad> getSquads() {
        return squads;
    }

    public void setSquads(Set<Squad> squads) {
        this.squads = squads;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }

    public Set<Kill> getKills() {
        return kills;
    }

    public void setKills(Set<Kill> kills) {
        this.kills = kills;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
