package no.noroff.hvz.dto.squad;

import java.util.List;

public class SquadDTO {

    private Long id;
    private String name;
    private List<PlayerDTO> members;

    public SquadDTO(Long id, String name, List<PlayerDTO> players) {
        this.id = id;
        this.name = name;
        this.members = players;
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

    public List<PlayerDTO> getMembers() {
        return members;
    }

    public void setMembers(List<PlayerDTO> members) {
        this.members = members;
    }
}
