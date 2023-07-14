package io.github.kuroka3.playermanager.Event

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SettingsManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.json.simple.JSONObject
import java.time.*

class ConnectEvent : Listener {
    @EventHandler
    fun onConnect(e: PlayerLoginEvent) {
        try {
            if (e.result != PlayerLoginEvent.Result.ALLOWED) {
                return
            }

            val playerJson = PlayerManager.instance.playerJSONFile

            if(!playerJson.isFile || playerJson.isEmpty) {
                playerJson.createNewFile()
                playerJson.saveJSONFile(JSONObject())
            }

            val tempobj: JSONObject = playerJson.jsonObject!!

            val userObject: JSONObject? = tempobj[e.player.uniqueId.toString()] as JSONObject?

            if(userObject == null) {
                val `object` = JSONObject()

                `object`["warns"] = 0
                `object`["ban"] = false
                `object`["banre"] = ""
                `object`["banid"] = ""
                `object`["mute"] = false
                `object`["temp"] = false
                `object`["tounban"] = "-1"
                `object`["lang"] = SettingsManager["defaultLanguage", "en-us"]

                tempobj[e.player.uniqueId.toString()] = `object`
            } else if (userObject["name"] != e.player.name) {
                userObject["name"] = e.player.name
                tempobj[e.player.uniqueId.toString()] = userObject
            }

            playerJson.saveJSONFile(tempobj)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            val p = ManagedPlayer(e.player as OfflinePlayer, PlayerManager.instance.playerJSONFile)

            if(p.ban) {
                if(p.temp) {
                    if(p.isToUnban(LocalDateTime.now())) {
                        p.unban(true)
                    } else {
                        e.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                            text("${Language[p.lang, "player.banned"]}\n\n${p.getNokori(LocalDateTime.now(), Language[p.lang, "player.tempban"])}\n\n").color(
                                TextColor.color(0x00ff5555)
                            ).append(
                                text("Reason: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(p.banre).color(TextColor.color(0x00ffffff))).append(
                                text("\n\nBan ID: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(p.banid).color(TextColor.color(0x00ffffff))))
                    }
                } else {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                        text("${Language[p.lang, "player.banned"]}\n\n")
                            .color(TextColor.color(0x00ff5555)).append(
                                text("Reason: ").color(TextColor.color(0x00aaaaaa))
                            ).append(
                                text(p.banre)
                                    .color(TextColor.color(0x00ffffff))
                            ).append(
                                text("\n\nBan ID: ").color(TextColor.color(0x00aaaaaa))
                            ).append(
                                text(p.banid)
                                    .color(TextColor.color(0x00ffffff))
                            ))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, text(Language[SettingsManager["defaultLanguage", "en-us"].toString(), "connect.failed"]).color(TextColor.color(0x00ff55555)))
        }
    }
}
