package io.github.kuroka3.playermanager.Utils

import io.github.kuroka3.playermanager.PlayerManager
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Properties

object SettingsManager {

    val settings: Properties = Properties()
    lateinit var file: File

    //TODO : Language Settings

    fun load() {
        file = File("${PlayerManager.instance.dataFolder}/config.properties")

        if(!file.isFile) {
            file.createNewFile()
            setDefault()
        }

        settings.load(FileReader(file))
    }

    private fun setDefault() {
        settings.setProperty("blockOriginalCommands", "true")
        settings.store(FileWriter(file), null)
    }

    fun get(key: String): String? {
        try {
            if(settings.getProperty(key) == null) {
                throw NullPointerException()
            }

            return settings.getProperty(key)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun get(key: String, default: Any): String? {
        try {

            if(settings.getProperty(key) == null) {
                settings.setProperty(key, default.toString())
                settings.store(FileWriter(file), null)
            }

            return settings.getProperty(key)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}