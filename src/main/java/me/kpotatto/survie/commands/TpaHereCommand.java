package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.CooldownUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaHereCommand extends BukkitCommand {

    public TpaHereCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender s, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage("§6Spawn §7>> §4Erreur §7> §cVous devez être un joueur pour exécuter cette commande");
            return true;
        }
        Player p = (Player) s;

        if(args.length != 1){
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cVous devez spécifier le nom d'un joueur.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null){
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cLe joueur spécifier est hors ligne.");
            return true;
        }

        if(target.getUniqueId().equals(p.getUniqueId())){
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cVous ne pouvez pas vous envoyer une requête de téléportation!");
            return true;
        }

        if(Survie.getInstance().teleportations.containsKey(p.getUniqueId())){
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cVous avez une téléportation en cours, impossible de vous téléportez pour le moment.");
            return true;
        }

        if(!CooldownUtils.asCooldownExpired(Survie.getInstance().tpaCooldown, p)){
            int timeRemaining = (int)( (Survie.getInstance().tpaCooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 );
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cVous devez attendre encore " + timeRemaining + " secondes avant d'utiliser cette commande.");
            return true;
        }

        for(TpaRequest tpa : Survie.getInstance().tpaRequests){
            if(tpa.getTarget().getUniqueId().equals(p.getUniqueId())){
                p.sendMessage("§6TPA §7>> §4Erreur §7> §cLe joueur §e" + target.getDisplayName() + "§c a déjà une demande de téléportation en attente.");
                return true;
            }
        }

        Survie.getInstance().tpaCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 120000);
        Survie.getInstance().tpaHereRequests.add(new TpaRequest(target, p));
        p.sendMessage("§6TPA §7>> §2Succès §7> §aUne demande de téléportation a été envoyé à §e" + target.getDisplayName() + "§a!");
        target.sendMessage("§6TPA §7>> §2Réception §7> §aVous avez reçu une demande de téléportation de §e" + p.getDisplayName() + "§a pour vous téléporter à lui.");
        sendButtons(target);
        return true;
    }

    private void sendButtons(Player p){
        TextComponent acceptButton = new TextComponent("§aAccepter la requête");
        acceptButton.setBold(true);
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§aCliquer pour accepter la demande").create())));
        TextComponent seperateur = new TextComponent("§7 | ");
        seperateur.setBold(true);
        TextComponent refuserButton = new TextComponent("§cRefuser la requête");
        refuserButton.setBold(true);
        refuserButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpno"));
        refuserButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§cCliquer pour refuser la demande").create())));
        p.sendMessage(acceptButton, seperateur, refuserButton);
    }

}
