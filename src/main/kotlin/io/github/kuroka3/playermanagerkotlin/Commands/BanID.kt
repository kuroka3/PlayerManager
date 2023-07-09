package io.github.kuroka3.playermanagerkotlin.Commands

import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import io.github.kuroka3.playermanagerkotlin.Utils.BanIDManager
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.json.simple.JSONObject
import java.util.*

object BanID {
    fun registerKommand() {

        val PlayerManagerKotlin = PlayerManagerKotlin.instance

        val mod: TextComponent = text("[").color(TextColor.color(0x00aaaaaa)).append(
            text("!").color(TextColor.color(0x00ff55ff))
        ).append(
            text("] ").color(TextColor.color(0x00aaaaaa))
        )

        PlayerManagerKotlin.kommand {
            register("banid") {
                then("id" to string()) {
                    executes {
                        val id: String by it
                        val info: JSONObject? = BanIDManager.getBanInfo(id)

                        if(info == null) {
                            sender.sendMessage(mod.append(text("해당 BanID를 찾을 수 없습니다").color(TextColor.color(0x00ff55555))))
                            return@executes
                        }

                        val moder = if(info["moder"] == "Server") "Server" else Bukkit.getOfflinePlayer(UUID.fromString(info["moder"].toString())).name!!
                        val target = Bukkit.getOfflinePlayer(UUID.fromString(info["target"].toString())).name!!
                        val reason = info["reason"] as String
                        val date = info["time"] as String
                        val isTemp = info["isTemp"] as Boolean
                        val period = info["period"] as String?

                        sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                        sender.sendMessage(text("BanID").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(id).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Moder").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(moder).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Target").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(target).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Reason").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(reason).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Time").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(date).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("isTempBan").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(isTemp).color(TextColor.color(0x0055ff55))))
                        if(period != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(period).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                    }

                    then("asUUID") {
                        executes {
                            val id: String by it
                            val info: JSONObject? = BanIDManager.getBanInfo(id)

                            if(info == null) {
                                sender.sendMessage(mod.append(text("해당 BanID를 찾을 수 없습니다").color(TextColor.color(0x00ff55555))))
                                return@executes
                            }

                            val moder = info["moder"] as String
                            val target = info["target"] as String
                            val reason = info["reason"] as String
                            val date = info["time"] as String
                            val isTemp = info["isTemp"] as Boolean
                            val period = info["period"] as String?

                            sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                            sender.sendMessage(text("BanID").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(id).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Moder").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(moder).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Target").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(target).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Reason").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(reason).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Time").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(date).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("isTempBan").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(isTemp).color(TextColor.color(0x0055ff55))))
                            if(period != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(period).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                        }
                    }
                }
            }
        }
    }
}