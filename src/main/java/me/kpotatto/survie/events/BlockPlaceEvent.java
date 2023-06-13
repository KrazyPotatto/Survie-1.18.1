package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.enchantments.CustomEnchantments;
import me.kpotatto.survie.utils.locks.ChestPlayerProfile;
import me.kpotatto.survie.utils.save.types.WorldLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.*;

import java.util.*;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent e){
        if(e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST) || e.getBlock().getType().equals(Material.BARREL)){
            Block block = e.getBlock();
            if(Survie.getInstance().lockingChestPlayers.containsKey(e.getPlayer().getUniqueId())){
                addChest(block, e.getPlayer());
                if(!Survie.getInstance().autoLockingChest.contains(e.getPlayer().getUniqueId())){
                    Survie.getInstance().lockingChestPlayers.remove(e.getPlayer().getUniqueId());
                    e.getPlayer().sendMessage("§6Lock Chest §7>> §1Info §7> §9This chest is now protected because you were it chest protection mode, which has been disabled.");
                }else {
                    e.getPlayer().sendMessage("§6Lock Chest §7>> §1Info §7> §9This chest is now protected because you were it chest protection mode. You are still in chest protection mode, because automatic mod is enabled.");
                }
            }
            return;
        }
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
        if(e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST) || e.getBlock().getType().equals(Material.BARREL)){
            Block block = e.getBlock();
            Block _double = null;
            boolean can = false;

            if(block.getState() instanceof Chest){
                Inventory inv = ((Chest) block.getState()).getInventory();
                if(inv instanceof DoubleChestInventory) {
                    DoubleChest c = ((DoubleChestInventory) inv).getHolder();
                    if (c!= null) {
                        block = ((Chest) c.getLeftSide()).getBlock();
                        _double = ((Chest) c.getRightSide()).getBlock();
                        if(Survie.getInstance().getLockedChests().containsChest(block.getLocation()) ||
                            Survie.getInstance().getLockedChests().containsChest(_double.getLocation()))
                            can = isChestOwner(block, e.getPlayer()) || isChestOwner(_double, e.getPlayer());
                    }
                } else {
                    if(Survie.getInstance().getLockedChests().containsChest(block.getLocation())){
                        can = isChestOwner(block, e.getPlayer());
                    }
                }
            }
            if(Survie.getInstance().getLockedChests().containsChest(block.getLocation()) ||
                (_double != null && Survie.getInstance().getLockedChests().containsChest(_double.getLocation()))) {
                e.setCancelled(true);
                if (!can) {
                    e.getPlayer().sendMessage("§6Lock Chest §7>> §4Error §7> §cThis chest is locked, and you don't have permissions to break it.");
                } else {
                    Survie.getInstance().getLockedChests().removeChest(new WorldLocation(block.getLocation()));
                    if (_double != null)
                        Survie.getInstance().getLockedChests().removeChest(new WorldLocation(_double.getLocation()));
                    e.getPlayer().sendMessage("§6Lock Chest §7>> §1Info §7> §9This chest's protection has been removed. Break it again to break it.");
                }
            }
            return;
        }
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
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.AUTO_SMELT.getEnchantment())){
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
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.getEnchantment())){
            dropItem(e.getBlock().getLocation(), e.getPlayer(), e.getItems());
            e.setCancelled(true);
        }
    }

    private void dropItem(Location location, Player p, ItemStack is){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.getEnchantment())){
            HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is);
            remaining.values().forEach(dis -> location.getWorld().dropItemNaturally(location, dis));
            if(!remaining.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventory full! §cSome items were dropped.");;
        }else{
            location.getWorld().dropItemNaturally(location, is);
        }
    }
    private void dropItem(Location location, Player p, Item is){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.getEnchantment())){
            HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is.getItemStack());
            remaining.values().forEach(dis -> location.getWorld().dropItemNaturally(location, dis));
            if(!remaining.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventory full! §cSome items were dropped.");
        }else{
            location.getWorld().dropItemNaturally(location, is.getItemStack());
        }
    }
    private void dropItem(Location location, Player p, Collection<Item> iss){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.getEnchantment())){
            ArrayList<ItemStack> toDrop = new ArrayList<>();
            for(Item is: iss){
                HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is.getItemStack());
                toDrop.addAll(remaining.values());
            }
            if(!toDrop.isEmpty()) toDrop.forEach(is -> location.getWorld().dropItemNaturally(location, is));
            if(!toDrop.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId()))
                p.sendActionBar("§cInventory full! §cSome items were dropped.");
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

    public boolean isChestOwner(Block block, Player p){
        boolean owner = false;

        if(Survie.getInstance().getLockedChests().containsChest(block.getLocation())){

            for(Map.Entry<UUID, List<ChestPlayerProfile>> profiles: Survie.getInstance().getLockedChests().playerProfiles.entrySet()){
                for(ChestPlayerProfile profile: profiles.getValue()){
                    if(profile.getLocation().equals(new WorldLocation(block.getLocation()))){
                        if(profiles.getKey().equals(p.getUniqueId())) {
                            owner = true;
                            break;
                        }
                    }
                }
                if(owner) break;
            }

        }

        return owner;
    }

    public void addChest(Block block, Player p){
        if(!Survie.getInstance().getLockedChests().containsChest(block.getLocation())) {
            if(!Survie.getInstance().getLockedChests().playerExists(p.getUniqueId()))
                Survie.getInstance().getLockedChests().createPlayer(p.getUniqueId());
            ChestPlayerProfile profile = Survie.getInstance().lockingChestPlayers.get(p.getUniqueId());
            profile.setLocation(new WorldLocation(block.getLocation()));
            Survie.getInstance().getLockedChests().addChest(p.getUniqueId(), profile);
        }
        if (!Survie.getInstance().autoLockingChest.contains(p.getUniqueId()))
            Survie.getInstance().lockingChestPlayers.remove(p.getUniqueId());
    }

}
