package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Kill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date timeOfDeath;

    @Column
    private String story;

    @Column(length = 20)
    private String lat;

    @Column(length = 20)
    private String lng;

    @ManyToOne
    @JoinColumn(name = "kill_id")
    private Game game;

    @JsonGetter("game")
    public Long gameGetter() {
        if (game != null) {
            return game.getId();
        }
        return null;
    }

    @ManyToOne
    @JoinColumn(name = "killer_id", nullable = false)
    private Player killer;

    @JsonGetter("killer")
    public Long killerGetter() {
        if (killer != null) {
            return killer.getId();
        }
        return null;
    }

    @OneToOne
    @JoinColumn(name = "victim_id", nullable = false)
    private Player victim;

    @JsonGetter("victim")
    public Long victimGetter() {
        if (victim != null) {
            return victim.getId();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(Date timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public Player getVictim() {
        return victim;
    }

    public void setVictim(Player victim) {
        this.victim = victim;
    }
}
