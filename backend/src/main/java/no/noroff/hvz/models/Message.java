package no.noroff.hvz.models;

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
    private boolean isZombie;

    @Column
    private Date chatTime;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Squad squad;

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

    public boolean isZombie() {
        return isZombie;
    }

    public void setZombie(boolean zombie) {
        isZombie = zombie;
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
}
