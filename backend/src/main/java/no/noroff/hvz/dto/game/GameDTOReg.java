package no.noroff.hvz.dto.game;

@SuppressWarnings("unused")
public class GameDTOReg {

    private String name;
    private String description;
    private String nw_lat;
    private String se_lat;
    private String nw_long;
    private String se_long;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNw_lat() {
        return nw_lat;
    }

    public void setNw_lat(String nw_lat) {
        this.nw_lat = nw_lat;
    }

    public String getSe_lat() {
        return se_lat;
    }

    public void setSe_lat(String se_lat) {
        this.se_lat = se_lat;
    }

    public String getNw_long() {
        return nw_long;
    }

    public void setNw_long(String nw_long) {
        this.nw_long = nw_long;
    }

    public String getSe_long() {
        return se_long;
    }

    public void setSe_long(String se_long) {
        this.se_long = se_long;
    }
}
