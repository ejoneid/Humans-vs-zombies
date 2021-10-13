package no.noroff.hvz.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SquadMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private int rank;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToMany
    @JoinColumn(name = "member_id")
    private Set<SquadCheckIn> checkIns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<SquadCheckIn> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(Set<SquadCheckIn> checkIns) {
        this.checkIns = checkIns;
    }
}
