package no.noroff.hvz.dto;

import java.util.List;

public class SquadDTO {

    private Long id;
    private String name;
    private String squadType;
    private String messagesUrl;
    private List<SquadMemberDTO> members;

    public SquadDTO(Long id, String name, String squadType, String messagesUrl, List<SquadMemberDTO> members) {
        this.id = id;
        this.name = name;
        this.squadType = squadType;
        this.messagesUrl = messagesUrl;
        this.members = members;
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

    public String getSquadType() {
        return squadType;
    }

    public void setSquadType(String squadType) {
        this.squadType = squadType;
    }

    public String getMessagesUrl() {
        return messagesUrl;
    }

    public void setMessagesUrl(String messagesUrl) {
        this.messagesUrl = messagesUrl;
    }

    public List<SquadMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<SquadMemberDTO> members) {
        this.members = members;
    }
}
