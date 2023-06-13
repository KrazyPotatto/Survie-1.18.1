package me.kpotatto.survie.skills;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;

import java.util.Objects;

public class MiningSkills extends Skills{

    public MiningSkills(int experience, OfflinePlayer p) {
        super(experience, p);
    }

    @Override
    public int levelFunction(int exp) {
        return (int)Math.sqrt(exp / 115f);
    }

    @Override
    public int expForLevel(int level){
        return (int) Math.pow(level, 2) * 115;
    }

    @Override
    public void levelUP() {
        if(this.player.isOnline()){
            Objects.requireNonNull(this.player.getPlayer()).sendTitle("§aLevel UP", "§aMining Lvl: §e" + this.level);
            Objects.requireNonNull(this.player.getPlayer()).playSound(this.player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
        }
    }

}
