package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = true, length = 30)
    private String biteCode;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany
    @JoinColumn(name = "killer_id")
    private Set<Kill> kills;

//    @OneToOne
//    @JoinColumn(name = "player_id")
//    private Kill death;

//    @OneToOne
//    @JoinColumn(name = "player_id")
//    private SquadMember member;

    @OneToMany
    @JoinColumn(name = "chat_id")
    private Set<Message> messages;

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
}
