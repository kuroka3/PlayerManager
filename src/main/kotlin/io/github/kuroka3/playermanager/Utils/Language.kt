package io.github.kuroka3.playermanager.Utils

import io.github.kuroka3.playermanager.PlayerManager
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.net.URL

object Language {
    private val langs: JSONObject = JSONObject()
    lateinit var langlist: JSONArray

    fun load() {
        val langsfile = JSONFile("${PlayerManager.instance.dataFolder}/lang/langs.json")
        if(!langsfile.isFile) {
            langsfile.createNewFile()
            langsfile.writeText(URL("https://pastebin.com/raw/VK2D4fvM").readText())
            File("${PlayerManager.instance.dataFolder}/lang/ko-kr.json").writeText(URL("https://pastebin.com/raw/rTnLiHQQ").readText())
            File("${PlayerManager.instance.dataFolder}/lang/en-us.json").writeText(URL("https://pastebin.com/raw/T03PZgG9").readText())
        }

        langlist = (langsfile.jsonObject?.get("list") ?: throw FileNotFoundException("langs.json File not found")) as JSONArray

        val dir = File("${PlayerManager.instance.dataFolder}/lang")

        val list = dir.list()
        list.forEach {
            if(!langlist.contains(it.replace(".json", ""))) return@forEach

            val file = JSONFile("${PlayerManager.instance.dataFolder}/lang/$it")

            langs[it.replace(".json", "")] = file.jsonObject
        }
    }

    operator fun get(lang: String, key: String): String {
        try {
            return (langs[lang] as JSONObject)[key] as String
        } catch (e: Exception) {
            e.printStackTrace()
            return "Text not found"
        }
    }
}