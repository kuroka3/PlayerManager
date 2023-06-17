package io.github.kuroka3.playermanager.Class;

import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class ManagedPlayer {
    private Player p;
    private OfflinePlayer op;
    private final JSONFile jsonFile;
    private int warns;
    private boolean ban;
    private String banid;
    private String banre;
    private boolean mute;
    private final JSONParser parser = new JSONParser();
    private final JSONObject jobj;
    private final boolean isOffline;

    public ManagedPlayer(Player player, JSONFile jsonfile) throws IOException {

        jsonFile = jsonfile;

        JSONObject tmobj = jsonFile.getJSONObject();

        p = player;
        jobj = (JSONObject) tmobj.get(p.getUniqueId().toString());
        warns = ((Long) jobj.get("warns")).intValue();
        ban = (boolean) jobj.get("ban");
        banid = (String) jobj.get("banid");
        banre = (String) jobj.get("banre");
        mute = (boolean) jobj.get("mute");
        isOffline = false;
    }

    public ManagedPlayer(OfflinePlayer player, JSONFile jsonfile) throws IOException {

        jsonFile = jsonfile;

        JSONObject tmobj = jsonFile.getJSONObject();

        op = player;
        jobj = (JSONObject) tmobj.get(op.getUniqueId().toString());
        warns = ((Long) jobj.get("warns")).intValue();
        ban = (boolean) jobj.get("ban");
        banid = (String) jobj.get("banid");
        banre = (String) jobj.get("banre");
        mute = (boolean) jobj.get("mute");
        isOffline = true;
    }

    public void warn() throws IOException {
        warns++;
        jobj.put("warns", warns);
        save();
    }
    
    public void setWarns(int i) throws IOException {
        
        if(i < 0) {
            return;
        }
        
        warns = i;
        jobj.put("warns", warns);
        save();
    }
    
    public int getWarns() {
        return warns;
    }

    public void ban(String re, String id) throws IOException {
        if (re.isEmpty()) {
            re = "No Reason Given";
        }
        
        ban = true;
        banre = re;
        banid = id;
        jobj.put("ban", ban);
        jobj.put("banre", banre);
        jobj.put("banid", banid);
        save();

        if(!isOffline) {
            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "당신은 이 서버에서 차단되었습니다", ChatColor.GOLD + banre, 20, 100, 0);
            Bukkit.getScheduler().runTaskLater(PlayerManager.getPlugin(PlayerManager.class), () -> {
                p.kickPlayer(ChatColor.RED + "당신은 이 서버에서 차단되었습니다\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + banre + ChatColor.GRAY + "\n\nBan ID: " + ChatColor.WHITE + banid);
            }, 100L);
        }
    }

    public void unban() throws IOException {
        if(!ban) {
            return;
        }
        ban = false;
        jobj.put("ban", ban);
        save();
    }

    public boolean isBan() {
        return ban;
    }

    public String getBanid() {
        return banid;
    }

    public Player getPlayer() {
        return p;
    }

    public OfflinePlayer getOfflinePlayer() {
        return op;
    }

    public String getBanReason() {
        return banre;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public boolean isMute() {
        return mute;
    }

    public void mute() throws IOException {
        if(isMute()) {
            return;
        }

        mute = true;
        jobj.put("mute", mute);
        save();
    }

    public void unmute() throws IOException {
        if(!isMute()) {
            return;
        }

        mute = false;
        jobj.put("mute", mute);
        save();
    }

    public void save() throws IOException {

        JSONObject tmobj = jsonFile.getJSONObject();

        if (!isOffline) {
            tmobj.put(p.getUniqueId().toString(), jobj);
        } else {
            tmobj.put(op.getUniqueId().toString(), jobj);
        }

        jsonFile.saveJSONFile(tmobj);
        
    }
}
