package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.TpaRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpYesCommand extends BukkitCommand {

    public TpYesCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender s, @NotNull String cmds, @NotNull String[] args) {

        if(!(s instanceof Player)) {
            s.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous devez être un joueur pour exécuter cette commande");
            return true;
        }

        Player p = (Player) s;

        for(TpaRequest tpa : Survie.getInstance().tpaRequests){
            if(tpa.getTarget().getUniqueId().equals(p.getUniqueId())){
                tpa.accept();
                Survie.getInstance().tpaRequests.remove(tpa);
                return true;
            }
        }

        for(TpaRequest tpa : Survie.getInstance().tpaHereRequests){
            if(tpa.getSender().getUniqueId().equals(p.getUniqueId())){
                tpa.accept();
                Survie.getInstance().tpaHereRequests.remove(tpa);
                return true;
            }
        }

        p.sendMessage("§6TPA §7>> §4Erreur §7> §cVous n'avez pas de requête de téléportation en attente!");
        return true;
    }

}
