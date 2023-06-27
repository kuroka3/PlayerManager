package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.CaseManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnBan implements CommandExecutor {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(@NotNull CommandSender sd, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            if (sd instanceof CommandBlock) {
                return true;
            }

            if (args.length == 0) {
                sd.sendMessage(mod + ChatColor.RED + "플레이어를 지정해 주십시오");
                return true;
            }

            if (!sd.hasPermission("playermanager.ban") && !(sd instanceof ConsoleCommandSender)) {
                sd.sendMessage(mod + ChatColor.RED + "이 명령어를 사용할 권한이 없습니다");
                return true;
            }

            JSONFile playerJson = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");

            ManagedPlayer target = new ManagedPlayer(Bukkit.getServer().getOfflinePlayer(args[0]), playerJson);

            if (!target.isBan()) {
                sd.sendMessage(mod + ChatColor.RED + "해당 유저는 밴이 되어있지 않습니다");
                return true;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }

            String reason = sb.toString();

            String saveReason;

            if(reason.isEmpty()) {
                saveReason = "No Reason Given";
            } else {
                saveReason = reason;
            }

            String moder = null;

            if(sd instanceof Player player) {
                moder = player.getUniqueId().toString();
            } else if (sd instanceof ConsoleCommandSender) {
                moder = "Server";
            }

            target.unban();

            CaseManager.addCase(5, moder, target.getOfflinePlayer().getUniqueId(), reason, null);

            sd.sendMessage(mod + ChatColor.YELLOW + target.getOfflinePlayer().getName() + ChatColor.GREEN + "님의 차단을 해제하였습니다: " + ChatColor.GOLD + saveReason);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            sd.sendMessage(mod + ChatColor.RED + "Check Console: An Exception occured");
            return true;
        }
    }
}
