package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.runnable.SkillsUpdateRunnable;
import me.kpotatto.survie.utils.sql.IpLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class JoinEvent implements Listener {

    private final JavaPlugin pl;

    public JoinEvent(JavaPlugin pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e){
        Survie.getInstance().sqlUtils.checkConnection();
        IpLogger ipLogger = Survie.getInstance().ipLogger;
        if(ipLogger.playerExists(e.getUniqueId())){
            ipLogger.updatePlayer(e.getUniqueId(), e.getAddress().getHostAddress(), true);
        }else{
            ipLogger.createPlayer(e.getUniqueId(), e.getAddress().getHostAddress(), true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Survie.getInstance(), new MessageRunnable(e.getPlayer()), 20L, 20L);
        Survie.getInstance().joinMessageID.put(e.getPlayer().getUniqueId(), task.getTaskId());

        if(!Survie.getInstance().skillsLoader.playerExists(e.getPlayer().getUniqueId()))
            Survie.getInstance().skillsLoader.createPlayer(e.getPlayer().getUniqueId(), e.getPlayer().getName());
        //BukkitTask task = Bukkit.getScheduler().runTaskTimer(Survie.getInstance(), new SkillsUpdateRunnable(e.getPlayer()), 300 * 20L, 300 * 20L);
        BukkitTask taks = new SkillsUpdateRunnable(e.getPlayer(), this.pl).runTaskTimerAsynchronously(Survie.getInstance(), 300 * 20L, 300 * 20L);
        Survie.getInstance().skillsSaveTaskID.put(e.getPlayer().getUniqueId(), taks.getTaskId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        int taskID = Survie.getInstance().skillsSaveTaskID.get(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().cancelTask(Survie.getInstance().skillsSaveTaskID.get(e.getPlayer().getUniqueId()));
        if(!Bukkit.getScheduler().isQueued(taskID) && !Bukkit.getScheduler().isCurrentlyRunning(taskID)){
            pl.getLogger().log(Level.INFO, "Save skills task successfully canceled for player " + e.getPlayer().getDisplayName()+":"+e.getPlayer().getUniqueId().toString());
        }
        Survie.getInstance().skillsSaveTaskID.remove(e.getPlayer().getUniqueId());
        Survie.getInstance().skillsLoader.updatePlayer(e.getPlayer().getUniqueId());
        Survie.getInstance().ipLogger.setOnline(e.getPlayer().getUniqueId(), false);

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
