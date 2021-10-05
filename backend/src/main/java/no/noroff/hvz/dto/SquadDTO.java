package no.noroff.hvz.dto;

public class SquadDTO {

    private Long id;
    private String name;
    private String squadType;
    private String messagesUrl;
    private String membersUrl;

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

    public String getMembersUrl() {
        return membersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }
}
