package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.skills.FightingSkills;
import me.kpotatto.survie.skills.MiningSkills;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)){
            s.sendMessage("§cMust be a player to execute this command");
            return true;
        }
        Player p = (Player) s;

        MiningSkills miningSkills = Survie.getInstance().skillsLoader.uuidMiningSkillsHashMap.get(p.getUniqueId());
        FightingSkills fightingSkills = Survie.getInstance().skillsLoader.uuidFightingSkillsHashMap.get(p.getUniqueId());

        int expLvlMining = miningSkills.getExperience() - miningSkills.expForLevel(miningSkills.getLevel());
        int expLvlFighting = fightingSkills.getExperience() - fightingSkills.expForLevel(fightingSkills.getLevel());

        int expNextLvlMining = miningSkills.expForLevel(miningSkills.getLevel() + 1) - miningSkills.expForLevel(miningSkills.getLevel());
        int expNextLvlFighting = fightingSkills.expForLevel(fightingSkills.getLevel() + 1) - fightingSkills.expForLevel(fightingSkills.getLevel());

        p.sendMessage("§6=================================================");
        p.sendMessage("§aMining (Niv. " + miningSkills.getLevel() + "): " + expLvlMining + "/" + expNextLvlMining);
        p.sendMessage("§aCombat (Niv. " + fightingSkills.getLevel() + "): " + expLvlFighting + "/" + expNextLvlFighting);
        p.sendMessage("§6=================================================");

        return false;
    }

}
