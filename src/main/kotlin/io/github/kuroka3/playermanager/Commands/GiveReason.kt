package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.CaseManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SendersLang
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor

object GiveReason {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = Component.text("[").color(TextColor.color(0x00aaaaaa)).append(
            Component.text("!").color(TextColor.color(0x00ff55ff))
        ).append(
            Component.text("] ").color(TextColor.color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("givereason") {
                requires { (isPlayer && hasPermission("playermanager.managecase")) || isConsole }
                then("case" to int()) {
                    then("reason" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            val case: Int by it
                            val reason: String by it

                            try {

                                CaseManager.giveReason(case, reason)

                                sender.sendMessage(mod.append(text(case.toString()).color(TextColor.color(0x00ffff55))).append(
                                    text("${Language[SendersLang[sender, isPlayer], "casegive.success"]}: ").color(TextColor.color(0x0055ff55))).append(
                                    text(reason).color(TextColor.color(0x00ffaa00))
                                ))

                            } catch (e: Exception) {
                                when (e) {

                                    is IllegalArgumentException, is IllegalAccessException -> sender.sendMessage(
                                        mod.append(
                                            text("${e.message}").color(TextColor.color(0x00ff55555))
                                        )
                                    )

                                    else -> {
                                        e.printStackTrace()
                                        sender.sendMessage(
                                            mod.append(
                                                text("Check Console: An Exception occured")
                                                    .color(TextColor.color(0x00ff55555))
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