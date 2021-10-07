package no.noroff.hvz.dto;

public class SquadMemberDTO {
    private Long id;
    private String rank;
    private String playerUrl;
    private String checkInsUrl;

    public SquadMemberDTO(Long id, String rank, String playerUrl, String checkInsUrl) {
        this.id = id;
        this.rank = rank;
        this.playerUrl = playerUrl;
        this.checkInsUrl = checkInsUrl;
    }

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

    public String getCheckInsUrl() {
        return checkInsUrl;
    }

    public void setCheckInsUrl(String checkInsUrl) {
        this.checkInsUrl = checkInsUrl;
    }
}
