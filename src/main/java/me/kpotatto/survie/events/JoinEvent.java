package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Survie.getInstance(), new MessageRunnable(e.getPlayer()), 20L, 20L);
        Survie.getInstance().joinMessageID.put(e.getPlayer().getUniqueId(), task.getTaskId());
    }

}

class MessageRunnable implements Runnable{

    Player p;
    int timer = 45;

    MessageRunnable(Player p){
        this.p = p;
    }

    @Override
    public void run() {
        if(timer == 0){
            Bukkit.getScheduler().cancelTask(Survie.getInstance().joinMessageID.get(p.getUniqueId()));
            return;
        }
        p.sendActionBar("§6Le /tpa a été refait, quelques bugs peuvent être présents");
        timer--;
    }
}
