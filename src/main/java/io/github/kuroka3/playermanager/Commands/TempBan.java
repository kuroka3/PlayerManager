package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.BanIDManager;
import io.github.kuroka3.playermanager.Utils.CaseManager;
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

public class TempBan implements CommandExecutor {

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

            if (args.length < 3 || !(args[2].equalsIgnoreCase("seconds") || args[2].equalsIgnoreCase("minutes") || args[2].equalsIgnoreCase("hours") || args[2].equalsIgnoreCase("days") || args[2].equalsIgnoreCase("months") || args[2].equalsIgnoreCase("years"))) {
                sd.sendMessage(mod + ChatColor.RED + "사용법이 알맞지 않습니다");
                return true;
            }

            if (!sd.hasPermission("playermanager.ban") && !(sd instanceof ConsoleCommandSender)) {
                sd.sendMessage(mod + ChatColor.RED + "이 명령어를 사용할 권한이 없습니다");
                return true;
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
                    return true;
                }
            } else {
                target = new ManagedPlayer(player2, playerJson);
            }

            if (target.isBan()) {
                sd.sendMessage(mod + ChatColor.RED + "해당 유저는 이미 밴 상태입니다");
                return true;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 3; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }

            String reason = sb.toString();

            String id = String.valueOf(
                    Long.parseLong(
                            LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("yyyyMMddhhmmssSS")
                            )
                    )*5
            );

            LocalDateTime toban = LocalDateTime.now();

            if(args[2].equalsIgnoreCase("seconds")) toban = toban.plusSeconds(Long.parseLong(args[1]));
            if(args[2].equalsIgnoreCase("minutes")) toban = toban.plusMinutes(Long.parseLong(args[1]));
            if(args[2].equalsIgnoreCase("hours")) toban = toban.plusHours(Long.parseLong(args[1]));
            if(args[2].equalsIgnoreCase("days")) toban = toban.plusDays(Long.parseLong(args[1]));
            if(args[2].equalsIgnoreCase("months")) toban = toban.plusMonths(Long.parseLong(args[1]));
            if(args[2].equalsIgnoreCase("years")) toban = toban.plusYears(Long.parseLong(args[1]));

            target.tempban(reason, id, toban);

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
                CaseManager.addCase(4, moder, target.getPlayer().getUniqueId(), saveReason, args[1] + args[2]);


                sd.sendMessage(mod + ChatColor.YELLOW + target.getPlayer().getName() + ChatColor.RED + "님을 서버에서 차단했습니다: " + ChatColor.GOLD + saveReason);
            } else {
                BanIDManager.setBan(id, target.getOfflinePlayer().getUniqueId(), moder, saveReason, LocalDateTime.now());
                CaseManager.addCase(4, moder, target.getOfflinePlayer().getUniqueId(), saveReason, args[1] + args[2]);


                sd.sendMessage(mod + ChatColor.YELLOW + target.getOfflinePlayer().getName() + ChatColor.RED + "님을 서버에서 차단했습니다: " + ChatColor.GOLD + saveReason);
            }



            return true;
        } catch (Exception e) {
            e.printStackTrace();
            sd.sendMessage(mod + ChatColor.RED + "Check Console: An Exception occured");
            return true;
        }
    }
}
