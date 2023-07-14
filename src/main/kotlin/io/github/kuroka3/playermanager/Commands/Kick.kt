package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.CaseManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SendersLang
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.lang.Exception
import java.time.LocalDateTime

object Kick {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = text("[").color(color(0x00aaaaaa)).append(
            text("!").color(color(0x00ff55ff))
        ).append(
            text("] ").color(color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("managerkick") {
                requires { (isPlayer && hasPermission("playermanager.kick")) || isConsole }

                val reasonString = string(StringType.GREEDY_PHRASE).apply {
                    suggests {
                        suggest(
                            listOf(
                                "No Reason Given",
                                "Reason"
                            )
                        )
                    }
                }

                then("targetname" to player()) {
                    then("reason" to reasonString) {
                        executes {
                            try {
                                val target: Player by it
                                val reason: String by it

                                var moder: String

                                if(isConsole) moder = "Server"
                                else moder = (sender as Player).uniqueId.toString()

                                target.kick(
                                    text("${Language[ManagedPlayer(target as OfflinePlayer, PlayerManager.playerJSONFile).lang, "player.kicked"]}\n\n").color(color(0x00ff5555)).append(
                                        text("Reason: ").color(color(0x00aaaaaa))
                                    ).append(
                                        text(reason).color(color(0x00ffffff))
                                    ))

                                sender.sendMessage(
                                    mod.append(
                                        text(target.name).color(color(0x00ffff55))
                                    ).append(
                                        text("${Language[SendersLang[sender, isPlayer], "sender.kick"]}: ").color(color(0x00ff5555))
                                    ).append(
                                        text(reason).color(color(0x00ffaa00))
                                    )
                                )



                                CaseManager.addCase(0, moder, target.uniqueId, reason, LocalDateTime.now())
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