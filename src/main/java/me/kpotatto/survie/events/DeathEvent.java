package me.kpotatto.survie.events;

import me.kpotatto.survie.enchantments.CustomEnchantments;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        String coords = "§6X: §e" + (int)loc.getX() + " §6Z: §e" + (int)loc.getZ() + " §6Y: §e" + (int)loc.getY();
        p.sendMessage("§6Death §7>> §2Coordinates §7> " + coords);

        /**
         * FOR THE SOULBOUND ENCHANTMENT
         */
        List<ItemStack> soulboundItems = new ArrayList<>();
        e.getDrops().stream().filter(is -> is != null && is.getEnchantments().containsKey(CustomEnchantments.SOULBOUND.getEnchantment())).forEach(soulboundItems::add);
        soulboundItems.forEach(is -> {
            e.getItemsToKeep().add(is);
            e.getDrops().remove(is);
        });
    }

}
