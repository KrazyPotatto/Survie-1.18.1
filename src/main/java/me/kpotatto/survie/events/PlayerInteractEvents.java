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

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                    // USER WISHES TO LOCK A CHEST
                    e.setCancelled(true);
                    addChest(block, p);
                } else {
                    if(isChestLocked(block, p)){
                        e.setCancelled(true);
                        p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cCe coffre est verrouillé et vous n'avez pas les permissions de l'ouvrir.");
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

    private boolean isLockedForPlayer(Player p, Location loc){
        boolean locked = false;
        boolean finished = false;
        if(Survie.getInstance().getLockedChests().containsChest(loc)){
            for(Map.Entry<UUID, List<ChestPlayerProfile>> profiles: Survie.getInstance().getLockedChests().playerProfiles.entrySet()){
                for(ChestPlayerProfile profile: profiles.getValue()){
                    if(profile.getLocation().equals(new WorldLocation(loc))){
                        finished = true;
                        if(profiles.getKey().equals(p.getUniqueId()))
                            break;
                        if(profile.isWhitelistMode()){
                            if(!profile.getAccessList().contains(p.getUniqueId())){
                                locked = true;
                                break;
                            }
                        }else{
                            if(profile.getAccessList().contains(p.getUniqueId())){
                                locked = true;
                                break;
                            }
                        }
                    }
                }
                if(finished) break;
            }
        }
        return locked;
    }

    public void addChest(Block block, Player p){
        boolean success;
        if(!Survie.getInstance().getLockedChests().containsChest(block.getLocation())) {
            if(!Survie.getInstance().getLockedChests().playerExists(p.getUniqueId()))
                Survie.getInstance().getLockedChests().createPlayer(p.getUniqueId());
            ChestPlayerProfile profile = Survie.getInstance().lockingChestPlayers.get(p.getUniqueId());
            profile.setLocation(new WorldLocation(block.getLocation()));
            Survie.getInstance().getLockedChests().addChest(p.getUniqueId(), profile);
            success = true;
        }else{
            success = false;
        }
        if (!Survie.getInstance().autoLockingChest.contains(p.getUniqueId())) {
            Survie.getInstance().lockingChestPlayers.remove(p.getUniqueId());
            if(success){
                p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLe coffre a été verrouillé avec succès. Le mode protection de coffre a été désactivé.");
            }else{
                p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cCe coffre est déjà verrouillé, impossible de le verrouiller. Le mode protection de coffre a été désactivé.");
            }
        }else{
            if(success){
                p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLe coffre a été verrouillé avec succès. Le mode protection de coffre est encore actif, vous êtes en mode automatique.");
            }else{
                p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cCe coffre est déjà verrouillé, impossible de le verrouiller. Le mode protection de coffre est encore actif, vous êtes en mode automatique.");
            }
        }
    }

}
