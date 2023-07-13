package io.github.kuroka3.playermanager.Event

import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.SettingsManager
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
        if(e.message.startsWith("/ban") && !e.message.startsWith("/banid") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            e.player.sendMessage(mod.append(text("서버 기본 관리 명령어의 사용이 차단 되어있습니다.").color(TextColor.color(0x00ff5555))))
            e.player.sendMessage(mod.append(text("[ /defaultban ] 를 사용하십시오.").color(TextColor.color(0x0055ff55))))
        }
        else if(e.message.startsWith("/kick") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            e.player.sendMessage(mod.append(text("서버 기본 관리 명령어의 사용이 차단 되어있습니다.").color(TextColor.color(0x00ff5555))))
            e.player.sendMessage(mod.append(text("[ /managerkick ] 를 사용하십시오.").color(TextColor.color(0x0055ff55))))
        }
    }

    @EventHandler
    fun onCommand(e: ServerCommandEvent) {
        if(e.command.startsWith("ban") || e.command.startsWith("/ban") && !e.command.startsWith("banid") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            PlayerManager.instance.logger.warning("서버 기본 관리 명령어의 사용이 차단 되어있습니다.")
            PlayerManager.instance.logger.warning("[ /defaultban ] 를 사용하십시오.")
        }
        else if(e.command.startsWith("kick") || e.command.startsWith("/kick") && SettingsManager.get("blockOriginalCommands", true).toBoolean()) {
            e.isCancelled = true
            PlayerManager.instance.logger.warning("서버 기본 관리 명령어의 사용이 차단 되어있습니다.")
            PlayerManager.instance.logger.warning("[ /managerkick ] 를 사용하십시오.")
        }
    }
}