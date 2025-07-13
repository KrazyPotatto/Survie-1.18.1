package me.kpotatto.survie.utils;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistrySet;
import me.kpotatto.survie.Survie;
import me.kpotatto.survie.enchantments.CustomEnchantments;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Bootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.compose().newHandler(evt -> {
            evt.registry().register(
              EnchantmentKeys.create(CustomEnchantments.AUTO_SMELT.key()),
              b -> b.description(Component.text("Auto Smelt"))
                      .supportedItems(evt.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_MINING))
                      .anvilCost(15)
                      .maxLevel(1)
                      .weight(10)
                      .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(10, 1))
                      .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 1))
                      .activeSlots(EquipmentSlotGroup.MAINHAND)
            );

            evt.registry().register(
                    EnchantmentKeys.create(CustomEnchantments.SOULBOUND.key()),
                    b -> b.description(Component.text("Soulbound"))
                            .supportedItems(evt.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_DURABILITY))
                            .anvilCost(35)
                            .maxLevel(1)
                            .weight(2)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(30, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(45, 1))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );

            List<TypedKey<ItemType>> beheadingTypes = List.of(
                    ItemTypeKeys.WOODEN_SWORD, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD,
                    ItemTypeKeys.WOODEN_AXE, ItemTypeKeys.STONE_AXE, ItemTypeKeys.IRON_AXE, ItemTypeKeys.GOLDEN_AXE, ItemTypeKeys.DIAMOND_AXE, ItemTypeKeys.NETHERITE_AXE,
                    ItemTypeKeys.CROSSBOW, ItemTypeKeys.BOW
            );
            evt.registry().register(
                    EnchantmentKeys.create(CustomEnchantments.BEHEADING.key()),
                    b -> b.description(Component.text("Beheading"))
                            .supportedItems(RegistrySet.keySet(RegistryKey.ITEM, beheadingTypes))
                            .anvilCost(25)
                            .maxLevel(1)
                            .weight(10)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(25, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(35, 1))
                            .activeSlots(EquipmentSlotGroup.MAINHAND)
            );

            evt.registry().register(
                    EnchantmentKeys.create(CustomEnchantments.VAMPIRISM.key()),
                    b -> b.description(Component.text("Vampirism"))
                            .supportedItems(RegistrySet.keySet(RegistryKey.ITEM, beheadingTypes))
                            .anvilCost(10)
                            .maxLevel(1)
                            .weight(15)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 1))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );

            List<TypedKey<ItemType>> teleTypes = List.of(
                    ItemTypeKeys.WOODEN_SWORD, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD,
                    ItemTypeKeys.WOODEN_AXE, ItemTypeKeys.STONE_AXE, ItemTypeKeys.IRON_AXE, ItemTypeKeys.GOLDEN_AXE, ItemTypeKeys.DIAMOND_AXE, ItemTypeKeys.NETHERITE_AXE,
                    ItemTypeKeys.WOODEN_PICKAXE, ItemTypeKeys.STONE_PICKAXE, ItemTypeKeys.IRON_PICKAXE, ItemTypeKeys.GOLDEN_PICKAXE, ItemTypeKeys.DIAMOND_PICKAXE, ItemTypeKeys.NETHERITE_PICKAXE,
                    ItemTypeKeys.WOODEN_SHOVEL, ItemTypeKeys.STONE_SHOVEL, ItemTypeKeys.IRON_SHOVEL, ItemTypeKeys.GOLDEN_SHOVEL, ItemTypeKeys.DIAMOND_SHOVEL, ItemTypeKeys.NETHERITE_SHOVEL,
                    ItemTypeKeys.WOODEN_HOE, ItemTypeKeys.STONE_HOE, ItemTypeKeys.IRON_HOE, ItemTypeKeys.GOLDEN_HOE, ItemTypeKeys.DIAMOND_HOE, ItemTypeKeys.NETHERITE_HOE,
                    ItemTypeKeys.CROSSBOW, ItemTypeKeys.BOW, ItemTypeKeys.MACE
            );
            evt.registry().register(
                    EnchantmentKeys.create(CustomEnchantments.TELEKINESIS.key()),
                    b -> b.description(Component.text("Telekinesis"))
                            .supportedItems(RegistrySet.keySet(RegistryKey.ITEM, teleTypes))
                            .anvilCost(10)
                            .maxLevel(1)
                            .weight(15)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(5, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 1))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );
        }));

    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new Survie();
    }
}
