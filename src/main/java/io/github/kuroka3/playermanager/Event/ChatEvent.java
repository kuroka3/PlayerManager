package io.github.kuroka3.playermanager.Event;

import io.github.kuroka3.playermanager.Class.ManagedPlayer;
import io.github.kuroka3.playermanager.PlayerManager;
import io.github.kuroka3.playermanager.Utils.JSONFile;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatEvent implements Listener {

    private static String mod = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        try {
            ManagedPlayer p = new ManagedPlayer(e.getPlayer(), new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json"));

            if(p.isMute()) {
               e.setCancelled(true);

               p.getPlayer().sendMessage(mod + ChatColor.RED + "채팅을 칠 권한이 없습니다");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
