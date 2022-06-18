package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.locks.ChestPlayerProfile;
import me.kpotatto.survie.utils.save.types.WorldLocation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§6Lock Chests §7>> §4Erreur §7> §cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }
        Player p = (Player) sender;
        if(args.length <= 1) {
            UUID player = p.getUniqueId();
            if(args.length == 1 && args[0].equalsIgnoreCase("details") && isLockingActive(player)){
                ChestPlayerProfile profile = Survie.getInstance().lockingChestPlayers.get(player);
                p.sendMessage(String.format("§dInformations sur le mode protection de coffre:\n§aType: %s\n§aListe d'accès / refus:\n%s",
                        profile.isWhitelistMode() ? "Whitelist": "Blacklist",
                        afficherListeJoueur(profile.getAccessList())));
                return true;
            }
            switch (label) {
                case "lc":
                case "lockchest":
                    if (isLockingActive(player)) {
                        // REMOVE FROM LIST
                        Survie.getInstance().lockingChestPlayers.remove(player);
                        Survie.getInstance().autoLockingChest.remove(player);
                        sender.sendMessage("§6Lock Chest §7>> §2Succès §7> §aVous avez désactiver le mode de verrouillage de coffre!");
                    } else {
                        // ADD TO THE LIST
                        Survie.getInstance().lockingChestPlayers.put(player, new ChestPlayerProfile(null, new ArrayList<>(), isBlacklistMode(args)));
                        sender.sendMessage("§6Lock Chest §7>> §2Succès §7> §aVous avez activer le mode de verrouillage de coffre!");
                    }
                    break;
                case "alc":
                case "autolockchest":
                    if (isLockingActive(player)) {
                        // REMOVE FROM LIST
                        Survie.getInstance().lockingChestPlayers.remove(player);
                        Survie.getInstance().autoLockingChest.remove(player);
                        sender.sendMessage("§6Lock Chest §7>> §2Succès §7> §aVous avez désactiver le mode de verrouillage de coffre!");
                    } else {
                        // ADD TO THE LIST
                        Survie.getInstance().lockingChestPlayers.put(player, new ChestPlayerProfile(null, new ArrayList<>(), isBlacklistMode(args)));
                        Survie.getInstance().autoLockingChest.add(player);
                        sender.sendMessage("§6Lock Chest §7>> §2Succès §7> §aVous avez activer le mode de verrouillage de coffre automatique!");
                    }
                    break;
            }
        }else if(args.length == 2){
            if (!isLockingActive(p.getUniqueId())) {
                p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cVous devez être en mode protection de coffre pour accéder à ces sous-commandes");
                return true;
            }
            ChestPlayerProfile profile = Survie.getInstance().lockingChestPlayers.get(p.getUniqueId());
            if(args[0].equalsIgnoreCase("type")){
                if(args[1].equalsIgnoreCase("whitelist")){
                    p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLa liste d'accès est maintenant de type whitelist");
                    profile.setWhitelistMode(true);
                    return true;
                }else if(args[1].equalsIgnoreCase("blacklist")){
                    p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLa liste d'accès est maintenant de type blacklist");
                    profile.setWhitelistMode(false);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                Optional<OfflinePlayer> optionalOtherPlayer = Arrays.stream(Bukkit.getOfflinePlayers()).filter(op -> Objects.requireNonNull(op.getName()).equalsIgnoreCase(args[1])).findFirst();
                if (optionalOtherPlayer.isPresent()) {
                    UUID otherPlayer = optionalOtherPlayer.get().getUniqueId();
                    if (args[0].equalsIgnoreCase("add")) {
                        p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLe joueur a été ajouté à la " + (profile.isWhitelistMode() ? "whitelist" : "blacklist"));
                        profile.getAccessList().add(otherPlayer);
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        p.sendMessage("§6Lock Chest §7>> §2Succès §7> §aLe joueur a été supprimé de la " + (profile.isWhitelistMode() ? "whitelist" : "blacklist"));
                        profile.getAccessList().remove(otherPlayer);
                    }
                    return true;
                } else {
                    p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cLe joueur spécifié n'existe pas ou s'est jamais connecté sur le serveur.");
                    return true;
                }
            }else {
                p.sendMessage("§6Lock Chest §7>> §4Erreur §7> §cSeul les arguments: add, remove, type sont reconnu.");
            }
        }
        return false;
    }

    private boolean isBlacklistMode(@NotNull String[] args){
        return args.length >= 1 && args[0].equalsIgnoreCase("blacklist");
    }

    private boolean isLockingActive(@NotNull UUID uuid){
        return Survie.getInstance().lockingChestPlayers.containsKey(uuid);
    }

    private String afficherListeJoueur(List<UUID> liste){
        StringBuilder builder = new StringBuilder();

        for(UUID uuid: liste){
            builder.append("§e* ").append(trouverNomJoueur(uuid)).append("\n");
        }

        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    private String trouverNomJoueur(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

}
