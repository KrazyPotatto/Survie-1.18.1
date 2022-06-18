package me.kpotatto.survie.utils.save.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

public class WorldLocation {

    private final UUID worldUUID;
    private final long x, y, z;
    private final float yaw, pitch;

    public WorldLocation(Location location){
        this.worldUUID = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public WorldLocation(UUID worldUUID, long x, long y, long z, float yaw, float pitch){
        this.worldUUID = worldUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location getLocation(){
        return new Location(Bukkit.getWorld(worldUUID), x, y, z, yaw, pitch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldLocation that = (WorldLocation) o;
        return x == that.x && y == that.y && z == that.z && worldUUID.equals(that.worldUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldUUID, x, y, z);
    }
}
