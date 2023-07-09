package io.github.kuroka3.PlayerManagerKotlinkotlin.Event

import io.github.kuroka3.playermanagerkotlin.Class.ManagedPlayer
import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import io.github.kuroka3.playermanagerkotlin.Utils.JSONFile
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

            val playerJson = PlayerManagerKotlin.instance.playerJSONFile

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
            val p: ManagedPlayer = ManagedPlayer(e.player as OfflinePlayer, PlayerManagerKotlin.instance.playerJSONFile)

            if(p.ban) {
                if(p.temp) {
                    if(p.isToUnban(LocalDateTime.now())) {
                        p.unban(true)
                    } else {
                        e.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                            text("당신은 이 서버에서 차단되었습니다\n\n${p.getNokori(LocalDateTime.now(), "d일 h시간 m분 s초 남음")}\n\n").color(TextColor.color(0x00ff5555)).append(
                                text("Reason: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(p.banre).color(TextColor.color(0x00ffffff))).append(
                                text("\n\nBan ID: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(p.banid).color(TextColor.color(0x00ffffff))))
                    }
                } else {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                        text("당신은 이 서버에서 차단되었습니다\n\n").color(TextColor.color(0x00ff5555)).append(
                            text("Reason: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(p.banre).color(TextColor.color(0x00ffffff))).append(
                            text("\n\nBan ID: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(p.banid).color(TextColor.color(0x00ffffff))))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, text("서버 연결 중 PlayerManager 플러그인에서 문제가 발생하였습니다\n\n서버 관리자에게 문의해 주십시오").color(TextColor.color(0x00ff55555)))
        }
    }
}
