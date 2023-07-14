package io.github.kuroka3.playermanager.Utils

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

object SendersLang {
    operator fun get(sender: CommandSender, isPlayer: Boolean): String {
        if(isPlayer) {
            return ManagedPlayer(sender as OfflinePlayer, PlayerManager.instance.playerJSONFile).lang
        } else {
            return SettingsManager["defaultLanguage", "en-us"].toString()
        }
    }
}