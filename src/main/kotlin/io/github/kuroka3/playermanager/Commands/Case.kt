package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.*
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.json.simple.JSONObject
import java.util.*

object Case {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = text("[").color(TextColor.color(0x00aaaaaa)).append(
            text("!").color(TextColor.color(0x00ff55ff))
        ).append(
            text("] ").color(TextColor.color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("case") {
                requires { isConsole || isPlayer }
                then("case" to int()) {
                    executes {
                        val case: Int by it
                        val info: JSONObject? = CaseManager.getCase(case)

                        if(info == null) {
                            sender.sendMessage(mod.append(text(Language[SendersLang[sender, isPlayer], "case.notfound"]).color(TextColor.color(0x00ff55555))))
                            return@executes
                        }

                        val moder = if(info["moder"] == "Server") "Server" else Bukkit.getOfflinePlayer(UUID.fromString(info["moder"].toString())).name!!
                        val target = Bukkit.getOfflinePlayer(UUID.fromString(info["target"].toString())).name!!
                        val reason = info["reason"] as String
                        val date = info["time"] as String
                        val typeint = info["type"] as Long
                        val type = when(typeint) {
                            0L -> "kick"
                            1L -> "ban"
                            2L -> "warn"
                            3L -> "mute"
                            4L -> "TempBan"
                            5L -> "unBan"
                            6L -> "unMute"
                            7L -> "unWarn"
                            else -> "unKnown Type"
                        }
                        val tempban = info["tempban"] as String?

                        sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                        sender.sendMessage(text("Case").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(case).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Type").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(type).color(TextColor.color(0x0055ff55))))
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
                        if(tempban != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(tempban).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                    }

                    then("asUUID") {
                        executes {
                            val case: Int by it
                            val info: JSONObject? = CaseManager.getCase(case)

                            if(info == null) {
                                sender.sendMessage(mod.append(text(Language[SendersLang[sender, isPlayer], "case.notfound"]).color(TextColor.color(0x00ff55555))))
                                return@executes
                            }

                            val moder = info["moder"] as String
                            val target = info["target"] as String
                            val reason = info["reason"] as String
                            val date = info["time"] as String
                            val typeint = info["type"] as Long
                            val type = when(typeint) {
                                0L -> "kick"
                                1L -> "ban"
                                2L -> "warn"
                                3L -> "mute"
                                4L -> "TempBan"
                                5L -> "unBan"
                                6L -> "unMute"
                                7L -> "unWarn"
                                else -> "unKnown Type"
                            }
                            val tempban = info["tempban"] as String?

                            sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                            sender.sendMessage(text("Case").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(case).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Type").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(type).color(TextColor.color(0x0055ff55))))
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
                            if(tempban != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(tempban).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                        }
                    }
                }
                then("latest") {
                    executes {
                        val case = CaseManager.getLatest()
                        val info: JSONObject? = CaseManager.getCase(case)

                        if(info == null) {
                            sender.sendMessage(mod.append(text(Language[SendersLang[sender, isPlayer], "case.notfound"]).color(TextColor.color(0x00ff55555))))
                            return@executes
                        }

                        val moder = if(info["moder"] == "Server") "Server" else Bukkit.getOfflinePlayer(UUID.fromString(info["moder"].toString())).name!!
                        val target = Bukkit.getOfflinePlayer(UUID.fromString(info["target"].toString())).name!!
                        val reason = info["reason"] as String
                        val date = info["time"] as String
                        val typeint = info["type"] as Long
                        val type = when(typeint) {
                            0L -> "kick"
                            1L -> "ban"
                            2L -> "warn"
                            3L -> "mute"
                            4L -> "TempBan"
                            5L -> "unBan"
                            6L -> "unMute"
                            7L -> "unWarn"
                            else -> "unKnown Type"
                        }
                        val tempban = info["tempban"] as String?

                        sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                        sender.sendMessage(text("Case").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(case).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("Type").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(type).color(TextColor.color(0x0055ff55))))
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
                        if(tempban != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                            text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                            text(tempban).color(TextColor.color(0x0055ff55))))
                        sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                    }

                    then("asUUID") {
                        executes {
                            val case = CaseManager.getLatest()
                            val info: JSONObject? = CaseManager.getCase(case)

                            if(info == null) {
                                sender.sendMessage(mod.append(text(Language[SendersLang[sender, isPlayer], "case.notfound"]).color(TextColor.color(0x00ff55555))))
                                return@executes
                            }

                            val moder = info["moder"] as String
                            val target = info["target"] as String
                            val reason = info["reason"] as String
                            val date = info["time"] as String
                            val typeint = info["type"] as Long
                            val type = when(typeint) {
                                0L -> "kick"
                                1L -> "ban"
                                2L -> "warn"
                                3L -> "mute"
                                4L -> "TempBan"
                                5L -> "unBan"
                                6L -> "unMute"
                                7L -> "unWarn"
                                else -> "unKnown Type"
                            }
                            val tempban = info["tempban"] as String?

                            sender.sendMessage(text("\n=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.color(0x0055ff55)))
                            sender.sendMessage(text("Case").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(case).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("Type").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(type).color(TextColor.color(0x0055ff55))))
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
                            if(tempban != null) sender.sendMessage(text("Period").color(TextColor.color(0x00ffff55)).append(
                                text(" :: ").color(TextColor.color(0x00aaaaaa))).append(
                                text(tempban).color(TextColor.color(0x0055ff55))))
                            sender.sendMessage(text("=-=-=-=-=-=-=-=-=-=-=-=-=\n").color(TextColor.color(0x0055ff55)))
                        }
                    }
                }
            }
        }
    }
}