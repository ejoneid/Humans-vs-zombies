package no.noroff.hvz.dto.message;

public class MessageDTOreg {

    private String message;
    private boolean faction;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFaction() {
        return faction;
    }

    public void setFaction(boolean faction) {
        this.faction = faction;
    }
}
