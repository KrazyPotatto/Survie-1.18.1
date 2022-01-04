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
            s.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous devez être un joueur pour exécuter cette commande");
            return true;
        }
        Player p = (Player) s;

        if(p.getLocation().getWorld() != Bukkit.getWorlds().get(0)){
            p.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous devez être dans l'overworld pour utiliser cette commande!");
            return true;
        }

        if(Survie.getInstance().teleportations.containsKey(p.getUniqueId())){
            p.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous avez une téléportation en cours, impossible de vous téléportez pour le moment.");
            return true;
        }

        if(!CooldownUtils.asCooldownExpired(Survie.getInstance().spawnCooldown, p)){
            int timeRemaining = (int)( (Survie.getInstance().spawnCooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 );
            p.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous devez attendre encore " + timeRemaining + " secondes avant d'utiliser cette commande.");
            return true;
        }

        TeleportationRequest request = new TeleportationRequest(p.getWorld().getSpawnLocation(), p, "§cSpawn §7>> §2Succès §7> §aVous avez été téléportez au spawn!");
        Survie.getInstance().teleportations.put(p.getUniqueId(), request);
        Survie.getInstance().spawnCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
        p.sendMessage("§6Spawn §7>> §2Succès §7> §aVotre téléportation commencera dans 5 secondes.");

        return true;
    }
}
