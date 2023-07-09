package io.github.kuroka3.playermanagerkotlin.Event

import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import io.github.kuroka3.playermanagerkotlin.Utils.SettingsManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent


class CommandEvent : Listener {

    val mod: TextComponent = text("[").color(TextColor.color(0x00aaaaaa)).append(
        text("!").color(TextColor.color(0x00ff55ff))
    ).append(
        text("] ").color(TextColor.color(0x00aaaaaa))
    )

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if(e.message.contains("/ban") && !e.message.contains("/banid") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            e.player.sendMessage(mod.append(text("서버 기본 관리 명령어의 사용이 차단 되어있습니다.").color(TextColor.color(0x00ff5555))))
        }
    }

    @EventHandler
    fun onCommand(e: ServerCommandEvent) {
        if(e.command.startsWith("ban") && !e.command.startsWith("banid") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            PlayerManagerKotlin.instance.logger.warning("서버 기본 관리 명령어의 사용이 차단 되어있습니다.")
        }
    }
}