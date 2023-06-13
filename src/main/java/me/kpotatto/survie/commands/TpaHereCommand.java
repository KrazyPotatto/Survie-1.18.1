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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage("§6Spawn §7>> §4Error §7> §cYou need to be a player to use this command.");
            return true;
        }
        Player p = (Player) s;

        if(args.length != 1){
            p.sendMessage("§6TPA §7>> §4Error §7> §cPlease specify the name of a player.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null){
            p.sendMessage("§6TPA §7>> §4Erreur §7> §cLe joueur spécifier est hors ligne.");
            return true;
        }

        if(target.getUniqueId().equals(p.getUniqueId())){
            p.sendMessage("§6TPA §7>> §4Error §7> §cYou cannot send yourself a teleportation request!");
            return true;
        }

        if(Survie.getInstance().teleportations.containsKey(p.getUniqueId())){
            p.sendMessage("§6TPA §7>> §4Error §7> §cYou have an outstanding teleportation in request. Please wait a few moments before trying again.");
            return true;
        }

        if(!CooldownUtils.asCooldownExpired(Survie.getInstance().tpaCooldown, p)){
            int timeRemaining = (int)( (Survie.getInstance().tpaCooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 );
            p.sendMessage("§6TPA §7>> §4Error §7> §cPlease wait " + timeRemaining + " seconds before using this command again.");
            return true;
        }

        for(TpaRequest tpa : Survie.getInstance().tpaRequests){
            if(tpa.getTarget().getUniqueId().equals(p.getUniqueId())){
                p.sendMessage("§6TPA §7>> §4Error §7> §e" + target.getDisplayName() + "§c already have a pending teleportation request.");
                return true;
            }
        }

        Survie.getInstance().tpaCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 120000);
        Survie.getInstance().tpaHereRequests.add(new TpaRequest(target, p));
        p.sendMessage("§6TPA §7>> §2Success §7> §aTeleportation request sent to §e" + target.getDisplayName() + "§a!");
        target.sendMessage("§6TPA §7>> §2Reception §7> §aYou received a teleportation request from §e" + p.getDisplayName() + "§a to teleport at his position.");
        sendButtons(target);
        return true;
    }

    private void sendButtons(Player p){
        TextComponent acceptButton = new TextComponent("§aAccept this request");
        acceptButton.setBold(true);
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§aClick here to accept this request").create())));
        TextComponent seperateur = new TextComponent("§7 | ");
        seperateur.setBold(true);
        TextComponent refuserButton = new TextComponent("§cDeny this request");
        refuserButton.setBold(true);
        refuserButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpno"));
        refuserButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§cClick here to deny this request").create())));
        p.sendMessage(acceptButton, seperateur, refuserButton);
    }

}
