package me.kpotatto.survie.events;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.kpotatto.survie.Survie;
import me.kpotatto.survie.enchantments.CustomEnchantments;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.*;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent e){
        switch (e.getBlock().getType()){
            case DIAMOND_ORE:
            case ANCIENT_DEBRIS:
            case IRON_ORE:
            case GOLD_ORE:
            case COAL_ORE:
            case COPPER_ORE:
            case DEEPSLATE_COAL_ORE:
            case DEEPSLATE_COPPER_ORE:
            case DEEPSLATE_DIAMOND_ORE:
            case DEEPSLATE_EMERALD_ORE:
            case DEEPSLATE_GOLD_ORE:
            case DEEPSLATE_IRON_ORE:
            case DEEPSLATE_LAPIS_ORE:
            case DEEPSLATE_REDSTONE_ORE:
            case EMERALD_ORE:
            case LAPIS_ORE:
            case NETHER_GOLD_ORE:
            case NETHER_QUARTZ_ORE:
            case REDSTONE_ORE:
                Survie.getInstance().placeBlock.add(e.getBlock().getLocation());
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(!Survie.getInstance().placeBlock.contains(e.getBlock().getLocation())){
            int exp = 0;
            switch (e.getBlock().getType()){
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    exp = 300;
                    break;
                case DEEPSLATE_EMERALD_ORE:
                case EMERALD_ORE:
                    exp = 400;
                    break;
                case ANCIENT_DEBRIS:
                    exp = 550;
                    break;
                case IRON_ORE:
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                case DEEPSLATE_IRON_ORE:
                    exp = 200;
                    break;
                case COAL_ORE:
                case COPPER_ORE:
                case DEEPSLATE_COAL_ORE:
                case DEEPSLATE_COPPER_ORE:
                case DEEPSLATE_LAPIS_ORE:
                case LAPIS_ORE:
                case NETHER_QUARTZ_ORE:
                    exp = 50;
                    break;
                case DEEPSLATE_REDSTONE_ORE:
                case REDSTONE_ORE:
                    exp = 25;
                    break;
                case NETHER_GOLD_ORE:
                    exp = 10;
                default:
                    Survie.getInstance().skillsLoader.uuidMiningSkillsHashMap.get(e.getPlayer().getUniqueId()).addExperience();
                    break;
            }
            if(exp > 0) Survie.getInstance().skillsLoader.uuidMiningSkillsHashMap.get(e.getPlayer().getUniqueId()).addExperience(exp);
        }else{
            Survie.getInstance().placeBlock.remove(e.getBlock().getLocation());
        }

        //HAS AUTOSMELT ITEM IN HAND
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) return;
        Enchantment autoSmelt = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(Key.key("survie:autosmelt"));
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.AUTO_SMELT.enchantment())){
            Collection<ItemStack> drops = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer());
            for(ItemStack is : drops){
                Optional<ItemStack> furnaceRecipe = getFurnaceRecipe(is);
                if(furnaceRecipe.isPresent()){
                    ItemStack result = furnaceRecipe.get();
                    result.setAmount(is.getAmount());
                    dropItem(e.getBlock().getLocation(), e.getPlayer(), result);
                    e.setDropItems(false);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakItemDrop(BlockDropItemEvent e){
        //IF HAS TELEPATHY
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            dropItem(e.getBlock().getLocation(), e.getPlayer(), e.getItems());
            e.setCancelled(true);
        }
    }

    private void dropItem(Location location, Player p, ItemStack is){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is);
            remaining.values().forEach(dis -> location.getWorld().dropItemNaturally(location, dis));
            if(!remaining.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventaire plein! §cCertains de vos items ont été drops");;
        }else{
            location.getWorld().dropItemNaturally(location, is);
        }
    }
    private void dropItem(Location location, Player p, Item is){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is.getItemStack());
            remaining.values().forEach(dis -> location.getWorld().dropItemNaturally(location, dis));
            if(!remaining.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventaire plein! §cCertains de vos items ont été drops");;
        }else{
            location.getWorld().dropItemNaturally(location, is.getItemStack());
        }
    }
    private void dropItem(Location location, Player p, Collection<Item> iss){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            ArrayList<ItemStack> toDrop = new ArrayList<>();
            for(Item is: iss){
                HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is.getItemStack());
                toDrop.addAll(remaining.values());
            }
            if(!toDrop.isEmpty()) toDrop.forEach(is -> location.getWorld().dropItemNaturally(location, is));
            if(!toDrop.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventaire plein! §cCertains de vos items ont été drops");
        }else{
            iss.forEach(is -> location.getWorld().dropItemNaturally(location, is.getItemStack()));
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
