package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Utils.BanIDManager;
import io.github.kuroka3.playermanager.Utils.CaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.UUID;

public class Case implements CommandExecutor {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(@NotNull CommandSender sd, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0) {
                sd.sendMessage(mod + ChatColor.RED + "Case 번호를 입력하십시오");
                return true;
            }

            if (args.length > 1) {
                sd.sendMessage(mod + ChatColor.RED + "불필요한 입력값을 제거하십시오");
                return true;
            }

            if (sd instanceof CommandBlock) {
                return true;
            }

            int Case = Integer.parseInt(args[0]);

            JSONObject info = CaseManager.getCase(Case);

            if (info == null) {
                sd.sendMessage(mod + ChatColor.RED + "해당 Case를 찾을 수 없습니다");
                return true;
            }

            String moder;
            try {
                moder = Bukkit.getOfflinePlayer((UUID.fromString((String) info.get("moder")))).getName();
            } catch (Exception e) {
                moder = (String) info.get("moder");
            }
            String target = Bukkit.getOfflinePlayer((UUID.fromString((String) info.get("target")))).getName();
            String reason = (String) info.get("reason");
            String tempban = (String) info.get("tempban");

            sd.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=");
            sd.sendMessage(ChatColor.YELLOW + "Case" + ChatColor.GRAY + " :: " + ChatColor.GREEN + Case);
            sd.sendMessage(ChatColor.YELLOW + "Moder" + ChatColor.GRAY + " :: " + ChatColor.GREEN + moder);
            sd.sendMessage(ChatColor.YELLOW + "Target" + ChatColor.GRAY + " :: " + ChatColor.GREEN + target);
            sd.sendMessage(ChatColor.YELLOW + "Reason" + ChatColor.GRAY + " :: " + ChatColor.GREEN + reason);
            if(tempban != null) sd.sendMessage(ChatColor.YELLOW + "TempBan" + ChatColor.GRAY + " :: " + ChatColor.GREEN + tempban);
            sd.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
