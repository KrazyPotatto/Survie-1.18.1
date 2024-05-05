package me.kpotatto.survie.runnable;

import me.kpotatto.survie.Survie;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.logging.Level;

public class SkillsUpdateRunnable extends BukkitRunnable {

    private final UUID uuid;
    private final JavaPlugin pl;

    public SkillsUpdateRunnable(Player p, JavaPlugin pl){
        this.uuid = p.getUniqueId();
        this.pl = pl;
    }

    @Override
    public void run() {
        if(Bukkit.getPlayer(uuid) == null || !Survie.getInstance().skillsSaveTaskID.containsValue(this.getTaskId())){
            this.cancel();
            pl.getLogger().log(Level.WARNING, "In task: Canceled skills save task for player " + Bukkit.getOfflinePlayer(uuid).getName()+":"+uuid.toString());
            return;
        }
        boolean send = !Survie.getInstance().disabledActionBar.contains(uuid);
        Survie.getInstance().skillsLoader.updatePlayer(uuid);
        if(send) Bukkit.getPlayer(uuid).sendActionBar(Component.text("§aVos données de jeu ont été sauvegardé avec succès!"));
    }

}
