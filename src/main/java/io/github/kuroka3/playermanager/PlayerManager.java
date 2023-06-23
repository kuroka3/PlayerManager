package io.github.kuroka3.playermanager;

import io.github.kuroka3.playermanager.Commands.*;
import io.github.kuroka3.playermanager.Event.ChatEvent;
import io.github.kuroka3.playermanager.Event.ConnectEvent;
import io.github.kuroka3.playermanager.Log.Logger;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.logging.Level;

public final class PlayerManager extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "PlayerManager Enabled");

        PlayerManager.getPlugin(PlayerManager.class).getDataFolder().mkdir();

        Logger.createLogFile();

        this.getCommand("managerban").setExecutor(new Ban());
        this.getCommand("managerwarn").setExecutor(new Warn());
        this.getCommand("managerunban").setExecutor(new UnBan());
        this.getCommand("managermute").setExecutor(new Mute());
        this.getCommand("managerunmute").setExecutor(new UnMute());
        this.getCommand("banid").setExecutor(new BanID());
        this.getCommand("tempban").setExecutor(new TempBan());

        this.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ConnectEvent(), this);

        JSONFile playerJSON = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");

        try {
            if (!playerJSON.isFile()) {
                playerJSON.createNewFile();
                JSONObject jobj = new JSONObject();
                playerJSON.saveJSONFile(jobj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
