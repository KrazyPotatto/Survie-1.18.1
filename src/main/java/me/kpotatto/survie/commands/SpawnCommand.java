package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.CooldownUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage("§6Spawn §7>> §4Error §7> §cYou need to be a player to execute this command");
            return true;
        }
        Player p = (Player) s;

        if(Survie.getInstance().teleportations.containsKey(p.getUniqueId())){
            p.sendMessage("§6Spawn §7>> §4Error §7> §cYou have an outstanding teleportation in progress. Please wait a few moments before teleporting to spawn again.");
            return true;
        }

        if(!CooldownUtils.asCooldownExpired(Survie.getInstance().spawnCooldown, p)){
            int timeRemaining = (int)( (Survie.getInstance().spawnCooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 );
            p.sendMessage("§6Spawn §7>> §4Error §7> §You need to wait " + timeRemaining + " seconds before using this command again.");
            return true;
        }

        TeleportationRequest request = new TeleportationRequest(p.getWorld().getSpawnLocation(), p, "§cSpawn §7>> §2Success §7> §aYou were teleported to spawn!");
        Survie.getInstance().teleportations.put(p.getUniqueId(), request);
        Survie.getInstance().spawnCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
        p.sendMessage("§6Spawn §7>> §2Success §7> §aYour teleportation will start in 5 seconds! Please stay still.");

        return true;
    }
}
