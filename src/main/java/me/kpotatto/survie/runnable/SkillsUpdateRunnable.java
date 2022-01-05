package me.kpotatto.survie.runnable;

import me.kpotatto.survie.Survie;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SkillsUpdateRunnable implements Runnable{

    private Player p;

    public SkillsUpdateRunnable(Player p){
        this.p = p;
    }

    @Override
    public void run() {
        p.sendActionBar(Component.text("§6Tentative de sauvegarde de vos données de jeu"));
        if(Survie.getInstance().skillsLoader.updatePlayer(p.getUniqueId())){
            p.sendActionBar(Component.text("§aVos données de jeu ont été sauvegardé avec succès!"));
        }else{
            p.sendActionBar(Component.text("§cUne erreur est survenu lors de la sauvegarde de vos données de jeu"));
        }
    }

}
