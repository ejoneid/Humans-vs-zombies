package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = false, length = 30)
    private String biteCode;

    @Column(nullable = false)
    private boolean isPatientZero;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Game game;

    @JsonGetter("game")
    public Long gameGetter() {
        if (game != null) {
            return game.getId();
        }
        return null;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @JsonGetter("user")
    public Long userGetter() {
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "killer_id")
    private Set<Kill> kills;

    @JsonGetter("kills")
    public List<Long> killsGetter() {
        if (kills != null) {
            return kills.stream().map(Kill::getId).collect(Collectors.toList());
        }
        return null;
    }

//    @OneToOne
//    @JoinColumn(name = "player_id")
//    private Kill death;

//    @OneToOne
//    @JoinColumn(name = "player_id")
//    private SquadMember member;

    @OneToMany
    @JoinColumn(name = "chat_id")
    private Set<Message> messages;

    @JsonGetter("messages")
    public List<String> messagesGetter() {
        if (messages != null) {
            return messages.stream().map(Message::getMessage).collect(Collectors.toList());
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public String getBiteCode() {
        return biteCode;
    }

    public void setBiteCode(String biteCode) {
        this.biteCode = biteCode;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
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

    public boolean isPatientZero() {
        return isPatientZero;
    }

    public void setPatientZero(boolean patientZero) {
        isPatientZero = patientZero;
    }
}
