package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.CaseManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SendersLang
import io.github.monun.kommand.KommandArgument
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.LocalDateTime

object UnBan {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = text("[").color(color(0x00aaaaaa)).append(
            text("!").color(color(0x00ff55ff))
        ).append(
            text("] ").color(color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("unban") {
                requires { (isPlayer && hasPermission("playermanager.ban")) || isConsole }

                val onlinePlayer = KommandArgument.string().apply {
                    suggests {
                        Bukkit.getOnlinePlayers().forEach {
                            suggest(it.name)
                        }
                    }
                }

                val reasonString = KommandArgument.string(StringType.GREEDY_PHRASE).apply {
                    suggests {
                        suggest(
                            listOf(
                                "No Reason Given",
                                "Reason"
                            )
                        )
                    }
                }

                then("target" to onlinePlayer) {
                    then("reason" to reasonString) {
                        executes {
                            try {
                                val target: String by it
                                val reason: String by it

                                val targetPlayer = Bukkit.getOfflinePlayer(target)

                                if (!targetPlayer.hasPlayedBefore()) throw IllegalArgumentException("Targeted player not found")

                                var moder: String

                                if(isConsole) moder = "Server"
                                else moder = (sender as Player).uniqueId.toString()

                                val managedPlayer: ManagedPlayer = ManagedPlayer(
                                    targetPlayer,
                                    PlayerManager.playerJSONFile
                                )

                                managedPlayer.unban(managedPlayer.temp)

                                sender.sendMessage(
                                    mod.append(
                                        text("${managedPlayer.p.name}").color(color(0x00ffff55))
                                    ).append(
                                        text("${Language[SendersLang[sender, isPlayer], "sender.unban"]}: ").color(color(0x0055ff55))
                                    ).append(
                                        text(reason).color(color(0x00ffaa00))
                                    )
                                )



                                CaseManager.addCase(5, moder, managedPlayer.p.uniqueId, reason, LocalDateTime.now())
                            } catch (e: Exception) {
                                when (e) {

                                    is IllegalArgumentException, is IllegalAccessException -> sender.sendMessage(
                                        mod.append(
                                            text("${e.message}").color(color(0x00ff55555))
                                        )
                                    )

                                    else -> {
                                        e.printStackTrace()
                                        sender.sendMessage(
                                            mod.append(
                                                text("Check Console: An Exception occured")
                                                    .color(color(0x00ff55555))
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}