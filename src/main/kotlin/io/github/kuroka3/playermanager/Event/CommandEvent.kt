package io.github.kuroka3.playermanager.Event

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SettingsManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.OfflinePlayer
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
        if(e.message.startsWith("/ban") && !e.message.startsWith("/banid") && SettingsManager["blockOriginalCommands", true].toBoolean()) {
            e.isCancelled = true
            val pl = ManagedPlayer(e.player as OfflinePlayer, PlayerManager.instance.playerJSONFile)
            e.player.sendMessage(mod.append(text(Language[pl.lang, "defaultcmd.block"]).color(TextColor.color(0x00ff5555))))
            e.player.sendMessage(mod.append(text(Language[pl.lang, "defaultban.suggest"]).color(TextColor.color(0x0055ff55))))
        }
        else if(e.message.startsWith("/kick") && SettingsManager["blockOriginalCommands", true].toBoolean()) {
            e.isCancelled = true
            val pl = ManagedPlayer(e.player as OfflinePlayer, PlayerManager.instance.playerJSONFile)
            e.player.sendMessage(mod.append(text(Language[pl.lang, "defaultcmd.block"]).color(TextColor.color(0x00ff5555))))
            e.player.sendMessage(mod.append(text(Language[pl.lang, "managerkick.suggest"]).color(TextColor.color(0x0055ff55))))
        }
    }

    @EventHandler
    fun onCommand(e: ServerCommandEvent) {
        if(e.command.startsWith("ban") || e.command.startsWith("/ban") && !e.command.startsWith("banid") && SettingsManager["blockOriginalCommands", true].toBoolean()) {
            e.isCancelled = true
            PlayerManager.instance.logger.warning(Language[SettingsManager["defaultLanguage", "en-us"].toString(), "defaultcmd.block"])
            PlayerManager.instance.logger.warning(Language[SettingsManager["defaultLanguage", "en-us"].toString(), "defaultban.suggest"])
        }
        else if(e.command.startsWith("kick") || e.command.startsWith("/kick") && SettingsManager["blockOriginalCommands", true].toBoolean()) {
            e.isCancelled = true
            PlayerManager.instance.logger.warning(Language[SettingsManager["defaultLanguage", "en-us"].toString(), "defaultcmd.block"])
            PlayerManager.instance.logger.warning(Language[SettingsManager["defaultLanguage", "en-us"].toString(), "managerkick.suggest"])
        }
    }
}