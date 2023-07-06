package io.github.kuroka3.playermanagerkotlin.Event

import io.github.kuroka3.playermanagerkotlin.Class.ManagedPlayer
import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import io.github.kuroka3.playermanagerkotlin.Utils.JSONFile
import io.papermc.paper.event.player.AbstractChatEvent
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MutedChatEvent: Listener {
    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        try {
            val managedPlayer = ManagedPlayer(Bukkit.getOfflinePlayer(e.player.uniqueId), JSONFile("${PlayerManagerKotlin.instance.dataFolder}/players.json"))
            val mod: TextComponent = text("[").color(TextColor.color(0x00aaaaaa)).append(
                text("!").color(TextColor.color(0x00ff55ff))
            ).append(
                text("] ").color(TextColor.color(0x00aaaaaa))
            )

            if(managedPlayer.mute) {
                e.isCancelled = true
                e.player.sendMessage(
                    mod.append(
                        text("당신은 채팅을 칠 수 없습니다: ").color(TextColor.color(0x00ff5555))).append(
                        text("muted").color(TextColor.color(0x00ffaa00)))
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}