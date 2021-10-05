package no.noroff.hvz.dto;

public class SquadMemberDTO {
    private Long id;
    private String rank;
    private String playerUrl;
    private String squadUrl;
    private String checkInsUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public String getSquadUrl() {
        return squadUrl;
    }

    public void setSquadUrl(String squadUrl) {
        this.squadUrl = squadUrl;
    }

    public String getCheckInsUrl() {
        return checkInsUrl;
    }

    public void setCheckInsUrl(String checkInsUrl) {
        this.checkInsUrl = checkInsUrl;
    }
}
