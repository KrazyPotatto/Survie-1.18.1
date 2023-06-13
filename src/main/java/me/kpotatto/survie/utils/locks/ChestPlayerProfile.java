package me.kpotatto.survie.utils.locks;

import me.kpotatto.survie.utils.save.types.WorldLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChestPlayerProfile implements Cloneable {

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

    @Override
    public ChestPlayerProfile clone() {
        try {
            return (ChestPlayerProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            List<UUID> cloneAccess = new ArrayList<>(this.accessList);
            WorldLocation cloneLocation = new WorldLocation(this.location.getWorldUUID(), this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
            return new ChestPlayerProfile(cloneLocation, cloneAccess, this.whitelistMode);
        }
    }

}
