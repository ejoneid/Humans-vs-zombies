package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Set;


@Entity
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean isHuman;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Game game;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "squad_message_id")
    private Set<Message> messages;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "squad_id")
    private Set<SquadMember> members;

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

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<SquadMember> getMembers() {
        return members;
    }

    public void setMembers(Set<SquadMember> members) {
        this.members = members;
    }
}
