package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.BanIDManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Ban implements CommandExecutor {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(@NotNull CommandSender sd, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            if (sd instanceof CommandBlock || args.length == 0) {
                return false;
            }

            JSONFile playerJson = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");

            Player player2 = Bukkit.getServer().getPlayer(args[0]);

            ManagedPlayer target;

            if(player2 == null) {
                OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(offlinePlayer.hasPlayedBefore()) {
                    target = new ManagedPlayer(offlinePlayer, playerJson);
                } else {
                    sd.sendMessage(mod + ChatColor.RED + "해당 유저를 찾을 수 없습니다.");
                    return false;
                }
            } else {
                target = new ManagedPlayer(player2, playerJson);
            }

            if (target.isBan()) {
                sd.sendMessage(mod + ChatColor.RED + "해당 유저는 이미 밴 상태입니다");
                return false;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }

            String reason = sb.toString();

            String id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSSSSS"));

            target.ban(reason, id);

            String moder = null;

            if(sd instanceof Player player) {
                moder = player.getUniqueId().toString();
            } else if (sd instanceof ConsoleCommandSender) {
                moder = "Server";
            }

            String saveReason;

            if(reason.isEmpty()) {
                saveReason = "No Reason Given";
            } else {
                saveReason = reason;
            }
            if(!target.isOffline()) {
                BanIDManager.setBan(id, target.getPlayer().getUniqueId(), moder, saveReason, LocalDateTime.now());


                sd.sendMessage(mod + ChatColor.YELLOW + target.getPlayer().getName() + ChatColor.RED + "님을 서버에서 차단했습니다: " + ChatColor.GOLD + saveReason);
            } else {
                BanIDManager.setBan(id, target.getOfflinePlayer().getUniqueId(), moder, saveReason, LocalDateTime.now());


                sd.sendMessage(mod + ChatColor.YELLOW + target.getOfflinePlayer().getName() + ChatColor.RED + "님을 서버에서 차단했습니다: " + ChatColor.GOLD + saveReason);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
