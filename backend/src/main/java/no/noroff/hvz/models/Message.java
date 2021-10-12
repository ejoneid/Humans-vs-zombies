package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = false)
    private boolean isGlobal;

    @Column(nullable = false)
    private boolean isFaction;

    @Column
    private Date chatTime;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Player player;

    @JsonGetter("player")
    public Long playerGetter() {
        if (player != null) {
            return player.getId();
        }
        return null;
    }

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Game game;

    @JsonGetter("game")
    public Long gameGetter() {
        if (game != null) {
            return game.getId();
        }
        return null;
    }

    @ManyToOne
    @JoinColumn(name = "squad_message_id")
    private Squad squad;

    @JsonGetter("squad")
    public Long squadGetter() {
        if (squad != null) {
            return squad.getId();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public Date getChatTime() {
        return chatTime;
    }

    public void setChatTime(Date chatTime) {
        this.chatTime = chatTime;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public boolean isFaction() {
        return isFaction;
    }

    public void setFaction(boolean faction) {
        isFaction = faction;
    }
}
