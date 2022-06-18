package me.kpotatto.survie.utils.locks;

import me.kpotatto.survie.utils.save.types.WorldLocation;

import java.util.List;
import java.util.UUID;

public class ChestPlayerProfile {

    private WorldLocation location;
    private List<UUID> accessList;
    private boolean whitelistMode;

    public ChestPlayerProfile(WorldLocation chests, List<UUID> whitelist, boolean blacklistMode) {
        this.location = chests;
        this.accessList = whitelist;
        this.whitelistMode = !blacklistMode;
    }

    public WorldLocation getLocation() {
        return location;
    }

    public void setLocation(WorldLocation location) {
        this.location = location;
    }

    public List<UUID> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<UUID> accessList) {
        this.accessList = accessList;
    }

    public boolean isWhitelistMode() {
        return whitelistMode;
    }

    public void setWhitelistMode(boolean whitelistMode) {
        this.whitelistMode = whitelistMode;
    }



}
