package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.runnable.SkillsUpdateRunnable;
import me.kpotatto.survie.utils.sql.IpLogger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

public class JoinEvent implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e){
        IpLogger ipLogger = Survie.getInstance().ipLogger;
        if(ipLogger.playerExists(e.getUniqueId())){
            ipLogger.updatePlayer(e.getUniqueId(), e.getAddress().getHostAddress());
        }else{
            ipLogger.createPlayer(e.getUniqueId(), e.getAddress().getHostAddress());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Survie.getInstance(), new MessageRunnable(e.getPlayer()), 20L, 20L);
        Survie.getInstance().joinMessageID.put(e.getPlayer().getUniqueId(), task.getTaskId());

        if(!Survie.getInstance().skillsLoader.playerExists(e.getPlayer().getUniqueId()))
            Survie.getInstance().skillsLoader.createPlayer(e.getPlayer().getUniqueId());
        //BukkitTask task = Bukkit.getScheduler().runTaskTimer(Survie.getInstance(), new SkillsUpdateRunnable(e.getPlayer()), 300 * 20L, 300 * 20L);
        BukkitTask taks = new SkillsUpdateRunnable(e.getPlayer()).runTaskTimerAsynchronously(Survie.getInstance(), 300 * 20L, 300 * 20L);
        Survie.getInstance().skillsSaveTaskID.put(e.getPlayer().getUniqueId(), taks.getTaskId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        int taskID = Survie.getInstance().skillsSaveTaskID.get(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().cancelTask(Survie.getInstance().skillsSaveTaskID.get(e.getPlayer().getUniqueId()));
        if(!Bukkit.getScheduler().isQueued(taskID) && !Bukkit.getScheduler().isCurrentlyRunning(taskID)){
            System.out.println("Save skills task successfully canceled for player " + e.getPlayer().getDisplayName()+":"+e.getPlayer().getUniqueId().toString());
        }
        Survie.getInstance().skillsSaveTaskID.remove(e.getPlayer().getUniqueId());
        Survie.getInstance().skillsLoader.updatePlayer(e.getPlayer().getUniqueId());

    }

}

class MessageRunnable implements Runnable{

    Player p;
    int timer = 90;

    MessageRunnable(Player p){
        this.p = p;
    }

    @Override
    public void run() {
        if(timer == 0){
            Bukkit.getScheduler().cancelTask(Survie.getInstance().joinMessageID.get(p.getUniqueId()));
            return;
        }
        p.sendActionBar("§6Nouvelle commande /skills ! Le wiki a été mis à jour: /wiki");
        timer--;
    }
}
