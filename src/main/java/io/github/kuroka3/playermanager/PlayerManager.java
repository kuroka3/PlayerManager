package io.github.kuroka3.playermanager;

import io.github.kuroka3.playermanager.Commands.Ban;
import io.github.kuroka3.playermanager.Commands.UnBan;
import io.github.kuroka3.playermanager.Commands.Warn;
import io.github.kuroka3.playermanager.Event.ConnectEvent;
import io.github.kuroka3.playermanager.Event.JoinQuitEvent;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

public final class PlayerManager extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("Enabled");

        PlayerManager.getPlugin(PlayerManager.class).getDataFolder().mkdir();

        this.getCommand("managerban").setExecutor(new Ban());
        this.getCommand("managerwarn").setExecutor(new Warn());
        this.getCommand("managerunban").setExecutor(new UnBan());

        this.getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);
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
