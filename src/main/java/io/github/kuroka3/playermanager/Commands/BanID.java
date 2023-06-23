package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Utils.BanIDManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.UUID;

public class BanID implements CommandExecutor {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(@NotNull CommandSender sd, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0) {
                sd.sendMessage(mod + ChatColor.RED + "BanID를 입력하십시오");
                return true;
            }

            if (args.length > 1) {
                sd.sendMessage(mod + ChatColor.RED + "불필요한 입력값을 제거하십시오");
                return true;
            }

            if (sd instanceof CommandBlock) {
                return true;
            }

            String BanID = args[0];

            JSONObject info = BanIDManager.getBanInfo(BanID);

            if (info == null) {
                sd.sendMessage(mod + ChatColor.RED + "해당 BanID를 찾을 수 없습니다");
                return true;
            }

            String moder = Bukkit.getOfflinePlayer((UUID.fromString((String) info.get("moder")))).getName();
            String target = Bukkit.getOfflinePlayer((UUID.fromString((String) info.get("target")))).getName();
            String reason = (String) info.get("reason");
            String date = (String) info.get("time");

            sd.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=");
            sd.sendMessage(ChatColor.YELLOW + "BanID" + ChatColor.GRAY + " :: " + ChatColor.GREEN + BanID);
            sd.sendMessage(ChatColor.YELLOW + "Moder" + ChatColor.GRAY + " :: " + ChatColor.GREEN + moder);
            sd.sendMessage(ChatColor.YELLOW + "Target" + ChatColor.GRAY + " :: " + ChatColor.GREEN + target);
            sd.sendMessage(ChatColor.YELLOW + "Reason" + ChatColor.GRAY + " :: " + ChatColor.GREEN + reason);
            sd.sendMessage(ChatColor.YELLOW + "Time" + ChatColor.GRAY + " :: " + ChatColor.GREEN + date);
            sd.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
