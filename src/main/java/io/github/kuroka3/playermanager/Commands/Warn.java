package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class Warn implements CommandExecutor {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(@NotNull CommandSender sd, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            if (sd instanceof CommandBlock || args.length == 0) {
                return false;
            }

            JSONFile playerJson = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");

            ManagedPlayer target = new ManagedPlayer(Bukkit.getServer().getPlayer(args[0]), playerJson);

            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }

            String reason = sb.toString();

            target.warn();

            String saveReason;

            if(reason.isEmpty()) {
                saveReason = "No Reason Given";
            } else {
                saveReason = reason;
            }

            sd.sendMessage(mod + ChatColor.YELLOW + target.getPlayer().getName() + ChatColor.RED + "님에게 경고를 1회 부여했습니다: " + ChatColor.GOLD + saveReason + ChatColor.YELLOW + " (총 " + target.getWarns() + "회)");
            if(!target.isOffline()) {
                target.getPlayer().sendMessage(mod + ChatColor.RED + "관리자에게 경고를 받았습니다: " + ChatColor.GOLD + saveReason + ChatColor.YELLOW + " (총 " + target.getWarns() + "회)");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
