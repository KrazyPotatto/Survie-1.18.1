package me.kpotatto.survie.skills;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;

import java.util.Objects;

public class FightingSkills extends Skills{

    public FightingSkills(int experience, OfflinePlayer p) {
        super(experience, p);
    }

    @Override
    public int levelFunction(int exp) {
        return (int)Math.pow(exp / 10f, 1f/3f);
    }

    @Override
    public int expForLevel(int level){
        return (int) (Math.pow(level, 3) * 10);
    }

    @Override
    public void levelUP() {
        if(this.player.isOnline()){
            Objects.requireNonNull(this.player.getPlayer()).sendTitle("§aLevel UP", "§aCombat Lvl: §e" + this.level);
            Objects.requireNonNull(this.player.getPlayer()).playSound(this.player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
        }
    }

}
