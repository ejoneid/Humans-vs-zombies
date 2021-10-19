package no.noroff.hvz.dto.squad;

public class SquadDTOUpdate {
    private String name;
    private boolean isHuman;

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
}
