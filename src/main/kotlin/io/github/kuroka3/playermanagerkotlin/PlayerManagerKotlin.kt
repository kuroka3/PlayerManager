package io.github.kuroka3.playermanagerkotlin

import io.github.kuroka3.PlayerManagerKotlinkotlin.Event.ConnectEvent
import io.github.kuroka3.playermanagerkotlin.Commands.*
import io.github.kuroka3.playermanagerkotlin.Event.MutedChatEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerManagerKotlin : JavaPlugin() {

    companion object {
        lateinit var instance: PlayerManagerKotlin
    }

    override fun onEnable() {
        instance = this

        logger.info("Hello world!")

        if(!dataFolder.exists()) {
            dataFolder.mkdir()
            logger.warning("DONT LOAD THIS PLUGIN BY RELOAD")
            logger.warning("PLEASE RESTART SERVER AFTER ADD THIS PLUGIN")
        }

        server.pluginManager.registerEvents(ConnectEvent(), this)
        server.pluginManager.registerEvents(MutedChatEvent(), this)

        Ban.registerKommand()
        Mute.registerKommand()
        TempBan.registerKommand()
        Warn.registerKommand()
        UnBan.registerKommand()
    }
}
