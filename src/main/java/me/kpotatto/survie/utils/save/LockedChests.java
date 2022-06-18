package me.kpotatto.survie.utils.save;

import me.kpotatto.survie.utils.locks.ChestPlayerProfile;
import me.kpotatto.survie.utils.save.types.WorldLocation;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class LockedChests extends SavableObject<LockedChests>{

    public Map<UUID, List<ChestPlayerProfile>> playerProfiles;

    public LockedChests(String fileName) {
        super(fileName);
        this.playerProfiles = new HashMap<>();
    }

    public boolean playerExists(UUID uuid){
        return this.playerProfiles.containsKey(uuid);
    }

    public void createPlayer(UUID uuid){
        this.playerProfiles.put(uuid, new ArrayList<>());
    }

    public void addChest(UUID uuid, ChestPlayerProfile profile){
        this.playerProfiles.get(uuid).add(profile);
        this.save();
    }

    public boolean containsChest(WorldLocation location){
        return playerProfiles.values().stream().anyMatch(l -> l.stream().anyMatch(p -> p.getLocation().equals(location)));
    }

    public boolean containsChest(Location location){
        return containsChest(new WorldLocation(location));
    }

    public void removeChest(WorldLocation location){
        for(Map.Entry<UUID, List<ChestPlayerProfile>> profiles: playerProfiles.entrySet()){
            profiles.setValue(profiles.getValue().stream().filter(p -> !p.getLocation().equals(location)).collect(Collectors.toList()));
        }
        //playerProfiles.values().forEach(l -> l = l.stream().filter(p -> !p.getLocation().equals(location)).collect(Collectors.toList()));
        this.save();
    }
}
