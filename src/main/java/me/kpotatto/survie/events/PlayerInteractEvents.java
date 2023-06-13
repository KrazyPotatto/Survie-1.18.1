package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.locks.ChestPlayerProfile;
import me.kpotatto.survie.utils.save.types.WorldLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class PlayerInteractEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Block block = e.getClickedBlock();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            //USER RIGHT-CLICKED ON BLOCK
            if(block != null && isLockableContainer(block.getType())){
                //USER CLICKED ON CHEST
                Player p = e.getPlayer();
                if(Survie.getInstance().lockingChestPlayers.containsKey(p.getUniqueId())){
                    e.setCancelled(true);
                    if(Survie.getInstance().lockingChestPlayers.get(p.getUniqueId()) == null) {
                        Optional<AbstractMap.SimpleEntry<UUID, ChestPlayerProfile>> opProfile = getProfile(block.getLocation());
                        if(opProfile.isPresent()){
                            ChestPlayerProfile profile = opProfile.get().getValue();
                            Survie.getInstance().lockingChestPlayers.replace(p.getUniqueId(), profile);
                            p.sendMessage("§6Lock Chest §7>> §2Success §7> §aThe profile of this chest has been copied.");
                        } else {
                            p.sendMessage("§6Lock Chest §7>> §4Error §7> §cThis chest is not locked, cannot copy profile.");
                        }
                    } else {
                        // USER WISHES TO LOCK A CHEST
                        addChest(block, p);
                    }
                } else {
                    if(isChestLocked(block, p)){
                        e.setCancelled(true);
                        p.sendMessage("§6Lock Chest §7>> §4Error §7> §cThis chest is locked and you do not have permissions to open it.");
                    }
                }
            }
        }
    }

    private boolean isLockableContainer(Material mat){
        return (mat.equals(Material.CHEST) || mat.equals(Material.TRAPPED_CHEST) || mat.equals(Material.BARREL));
    }

    public boolean isChestLocked(Block block, Player p){
        if(block.getState() instanceof Chest){
            Inventory inv = ((Chest) block.getState()).getInventory();
            if(inv instanceof DoubleChestInventory) {
                DoubleChest c = ((DoubleChestInventory) inv).getHolder();
                if (c!= null) {
                    Location lox = ((Chest) c.getLeftSide()).getLocation();
                    Location lov = ((Chest) c.getRightSide()).getLocation();
                    return isLockedForPlayer(p, lox) || isLockedForPlayer(p, lov);
                }
            }
        }

        return isLockedForPlayer(p, block.getLocation());
    }

    private Optional<AbstractMap.SimpleEntry<UUID, ChestPlayerProfile>> getProfile(Location loc){
        if(Survie.getInstance().getLockedChests().containsChest(loc)){
            for(Map.Entry<UUID, List<ChestPlayerProfile>> profiles: Survie.getInstance().getLockedChests().playerProfiles.entrySet()){
                for(ChestPlayerProfile profile: profiles.getValue()){
                    if(profile.getLocation().equals(new WorldLocation(loc))){
                        return Optional.of(new AbstractMap.SimpleEntry<>(profiles.getKey(), profile));
                    }
                }
            }
        }
        return Optional.empty();
    }
    private boolean isLockedForPlayer(Player p, Location loc){
        Optional<AbstractMap.SimpleEntry<UUID, ChestPlayerProfile>> opProfile = getProfile(loc);
        if(!opProfile.isPresent()){
            return false;
        }
        AbstractMap.SimpleEntry<UUID, ChestPlayerProfile> profile = opProfile.get();
        if(profile.getKey().equals(p.getUniqueId())) return false;
        if(profile.getValue().isWhitelistMode()){
            return !profile.getValue().getAccessList().contains(p.getUniqueId());
        }else{
            return profile.getValue().getAccessList().contains(p.getUniqueId());
        }
    }

    public void addChest(Block block, Player p){
        boolean success = false;
        if(!Survie.getInstance().getLockedChests().containsChest(block.getLocation())) {
            if(!Survie.getInstance().getLockedChests().playerExists(p.getUniqueId()))
                Survie.getInstance().getLockedChests().createPlayer(p.getUniqueId());
            ChestPlayerProfile profile = Survie.getInstance().lockingChestPlayers.get(p.getUniqueId()).clone();
            profile.setLocation(new WorldLocation(block.getLocation()));
            Survie.getInstance().getLockedChests().addChest(p.getUniqueId(), profile);
            success = true;
        }else{
            Optional<AbstractMap.SimpleEntry<UUID, ChestPlayerProfile>> opProfile = getProfile(block.getLocation());
            if(opProfile.isPresent()) {
                AbstractMap.SimpleEntry<UUID, ChestPlayerProfile> profile = opProfile.get();
                if(profile.getKey().equals(p.getUniqueId())) {
                    Survie.getInstance().getLockedChests().removeChest(profile.getValue().getLocation());
                    ChestPlayerProfile pp = Survie.getInstance().lockingChestPlayers.get(p.getUniqueId()).clone();
                    pp.setLocation(new WorldLocation(block.getLocation()));
                    Survie.getInstance().getLockedChests().addChest(p.getUniqueId(), pp);
                    success = true;
                }
            }
        }
        if (!Survie.getInstance().autoLockingChest.contains(p.getUniqueId())) {
            Survie.getInstance().lockingChestPlayers.remove(p.getUniqueId());
            if(success){
                p.sendMessage("§6Lock Chest §7>> §2Success §7> §aThe chest has been locked, chest protection mode has been disabled.");
            }else{
                p.sendMessage("§6Lock Chest §7>> §4Error §7> §cThis chest is already locked, cannot override. Chest protection mode has been disabled.");
            }
        }else{
            if(success){
                p.sendMessage("§6Lock Chest §7>> §2Success §7> §aThis chest has been locked. Chest protection mode is still active, because you enabled automatic mode.");
            }else{
                p.sendMessage("§6Lock Chest §7>> §4Error §7> §cThis chest is already locked, cannot override. Chest protection mode is still active, because you enabled automatic mode.");
            }
        }
    }

}
