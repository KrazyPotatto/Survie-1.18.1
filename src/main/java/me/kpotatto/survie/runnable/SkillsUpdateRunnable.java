package me.kpotatto.survie.runnable;

import me.kpotatto.survie.Survie;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SkillsUpdateRunnable extends BukkitRunnable {

    private UUID uuid;

    public SkillsUpdateRunnable(Player p){
        this.uuid = p.getUniqueId();
    }

    @Override
    public void run() {
        if(Bukkit.getPlayer(uuid) == null || !Survie.getInstance().skillsSaveTaskID.containsValue(this.getTaskId())){
            this.cancel();
            System.out.println("In task: Canceled skills save task for player " + Bukkit.getOfflinePlayer(uuid).getName()+":"+uuid.toString());
            return;
        }
        boolean send = !Survie.getInstance().disabledActionBar.contains(uuid);
        Survie.getInstance().skillsLoader.updatePlayer(uuid);
        if(send) Bukkit.getPlayer(uuid).sendActionBar(Component.text("§aGame data successfully saved!"));
    }

}
