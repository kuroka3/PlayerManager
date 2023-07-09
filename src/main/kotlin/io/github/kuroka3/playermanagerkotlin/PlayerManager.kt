package io.github.kuroka3.playermanagerkotlin

import io.github.kuroka3.PlayerManagerKotlinkotlin.Event.ConnectEvent
import io.github.kuroka3.playermanagerkotlin.Commands.*
import io.github.kuroka3.playermanagerkotlin.Event.CommandEvent
import io.github.kuroka3.playermanagerkotlin.Event.MutedChatEvent
import io.github.kuroka3.playermanagerkotlin.Utils.BanIDManager
import io.github.kuroka3.playermanagerkotlin.Utils.JSONFile
import io.github.kuroka3.playermanagerkotlin.Utils.SettingsManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class PlayerManager : JavaPlugin() {

    companion object {
        lateinit var instance: PlayerManager
    }

    lateinit var playerJSONFile: JSONFile

    override fun onEnable() {
        instance = this
        playerJSONFile = JSONFile("$dataFolder/data/players.json")

        logger.info("Hello world!")

        if(!dataFolder.exists()) {
            dataFolder.mkdir()
            File("$dataFolder/data").mkdir()
            logger.warning("DONT LOAD THIS PLUGIN BY RELOAD")
            logger.warning("PLEASE RESTART SERVER AFTER ADD THIS PLUGIN")
        }

        server.pluginManager.registerEvents(ConnectEvent(), this)
        server.pluginManager.registerEvents(MutedChatEvent(), this)
        server.pluginManager.registerEvents(CommandEvent(), this)

        Ban.registerKommand()
        Mute.registerKommand()
        TempBan.registerKommand()
        Warn.registerKommand()
        UnBan.registerKommand()
        BanID.registerKommand()

        SettingsManager.load()
        BanIDManager.setInit()
    }
}
