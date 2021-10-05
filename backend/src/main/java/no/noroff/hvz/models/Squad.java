package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @JsonGetter("game")
    public Long gameGetter() {
        if (game != null) {
            return game.getId();
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "message_id")
    private Set<Message> messages;

    @JsonGetter("messages")
    public List<Long> playersGetter() {
        if (messages != null) {
            return messages.stream().map(Message::getId).collect(Collectors.toList());
        }
        return null;
    }

    @OneToMany
    @JoinColumn(name = "member_id")
    private Set<SquadMember> members;

    @JsonGetter("members")
    public List<Long> membersGetter() {
        if (members != null) {
            return members.stream().map(SquadMember::getId).collect(Collectors.toList());
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
