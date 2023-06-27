package io.github.kuroka3.playermanager.Event;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.json.simple.JSONObject;

import java.time.*;

public class ConnectEvent implements Listener {

    @EventHandler
    public void onConnect(PlayerLoginEvent e) {

        try {

            JSONFile playerJson = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");

            JSONObject jsonObject = playerJson.getJSONObject();

            JSONObject userObject = (JSONObject) jsonObject.get(e.getPlayer().getUniqueId().toString());

            if(userObject == null) {
                JSONObject object = new JSONObject();

                object.put("name", e.getPlayer().getName());
                object.put("warns", 0);
                object.put("ban", false);
                object.put("banre", "");
                object.put("banid", "");
                object.put("mute", false);
                object.put("temp", false);
                object.put("tounban", "-1");

                jsonObject.put(e.getPlayer().getUniqueId().toString(), object);
            } else if(!userObject.get("name").equals(e.getPlayer().getName())) {
                userObject.put("name", e.getPlayer().getName());

                jsonObject.put(e.getPlayer().getUniqueId().toString(), userObject);
            }

            playerJson.saveJSONFile(jsonObject);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            ManagedPlayer p = new ManagedPlayer(e.getPlayer(), new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json"));

            if (p.isBan()) {
                if(p.isTemp()) {
                    if(p.isTounabn(LocalDateTime.now())) {
                        p.unban();
                    } else {
                        LocalDateTime tounban = p.getTounban();

                        LocalDate tounbanDate = tounban.toLocalDate();
                        LocalTime tounbanTime = tounban.toLocalTime();

                        Period pr = Period.between(LocalDate.now(), tounbanDate);
                        Duration dr = Duration.between(LocalTime.now(), tounbanTime);

                        int[] nokori = {pr.getYears(), pr.getMonths(), pr.getDays(), dr.toHoursPart(), dr.toMinutesPart(), dr.toSecondsPart()};

                        for (int i = 0; i < nokori.length; i++) {
                            if(nokori[i]<0) nokori[i] = nokori[i]*-1;
                        }

                        e.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                                ChatColor.RED + "당신은 이 서버에서 차단되었습니다\n\n" +
                                        ChatColor.RED + nokori[0] + "년 " + nokori[1] + "개월 " + nokori[2] + "일 " + nokori[3] + "시간 " + nokori[4] + "분 " + nokori[5] + "초 남음\n\n" +
                                        ChatColor.GRAY + "Reason: " + ChatColor.WHITE + p.getBanReason() +
                                        ChatColor.GRAY + "\n\nBan ID: " + ChatColor.WHITE + p.getBanid());
                    }
                } else {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "당신은 이 서버에서 차단되었습니다\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + p.getBanReason() + ChatColor.GRAY + "\n\nBan ID: " + ChatColor.WHITE + p.getBanid());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
