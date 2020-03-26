package de.chriis.vatlog.mutes;

public class Mute {

    private String reason, bannedByUuid, server;
    private long time, until, id;
    private boolean active;

    public Mute(String reason, String bannedByUuid, long time, long until, long id, boolean active, String server) {
        this.id = id;
        this.bannedByUuid = bannedByUuid;
        this.reason = reason;
        this.time = time;
        this.until = until;
        this.active = active;
        this.server = server;
    }

    public long getId() {
        return id;
    }

    public String getBannedByUuid() {
        return bannedByUuid;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public long getUntil() {
        return until;
    }

    public String getServer() {
        return server;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBannedByUuid(String bannedByUuid) {
        this.bannedByUuid = bannedByUuid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isActive() {
        return active;
    }
}
