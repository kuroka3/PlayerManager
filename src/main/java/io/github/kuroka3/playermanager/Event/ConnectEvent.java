package io.github.kuroka3.playermanager.Event;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "당신은 이 서버에서 차단되었습니다\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + p.getBanReason() + ChatColor.GRAY + "\n\nBan ID: " + ChatColor.WHITE + p.getBanid());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
