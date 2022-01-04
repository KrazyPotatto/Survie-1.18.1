package me.kpotatto.survie.events;

import me.kpotatto.survie.enchantments.CustomEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent e){
        if(e.getBlockPlaced().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER) && e.getBlockPlaced().getLocation().getY() < 128){
            if(e.getBlockPlaced().getType().equals(Material.BLACK_BED) ||
                e.getBlockPlaced().getType().equals(Material.BLUE_BED) ||
                e.getBlockPlaced().getType().equals(Material.BROWN_BED) ||
                e.getBlockPlaced().getType().equals(Material.CYAN_BED) ||
                e.getBlockPlaced().getType().equals(Material.GRAY_BED) ||
                e.getBlockPlaced().getType().equals(Material.GREEN_BED) ||
                e.getBlockPlaced().getType().equals(Material.LIGHT_BLUE_BED) ||
                e.getBlockPlaced().getType().equals(Material.LIGHT_GRAY_BED) ||
                e.getBlockPlaced().getType().equals(Material.LIME_BED) ||
                e.getBlockPlaced().getType().equals(Material.MAGENTA_BED) ||
                e.getBlockPlaced().getType().equals(Material.ORANGE_BED) ||
                e.getBlockPlaced().getType().equals(Material.PINK_BED) ||
                e.getBlockPlaced().getType().equals(Material.PURPLE_BED) ||
                e.getBlockPlaced().getType().equals(Material.RED_BED) ||
                e.getBlockPlaced().getType().equals(Material.WHITE_BED) ||
                e.getBlockPlaced().getType().equals(Material.YELLOW_BED) ||
                e.getBlockPlaced().getType().equals(Material.TNT)){
                e.getPlayer().sendMessage("§6Build §7>> §4Erreur §7> §cVous ne pouvez pas poser de lit ou de TnT dans le nether");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        //HAS AUTOSMELT ITEM IN HAND
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.AUTO_SMELT.getEnchantment())){
            Collection<ItemStack> drops = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer());
            for(ItemStack is : drops){
                Optional<ItemStack> furnaceRecipe = getFurnaceRecipe(is);
                if(furnaceRecipe.isPresent()){
                    ItemStack result = furnaceRecipe.get();
                    result.setAmount(is.getAmount());
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), result);
                }else{
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), is);
                }
            }
            e.setDropItems(false);
        }
    }

    private Optional<ItemStack> getFurnaceRecipe(ItemStack is){
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (!(recipe instanceof FurnaceRecipe)) continue;
            if (((FurnaceRecipe) recipe).getInput().getType() != is.getType()) continue;
            return Optional.of(recipe.getResult());
        }
        return Optional.empty();
    }

}
