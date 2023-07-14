package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.Language
import io.github.kuroka3.playermanager.Utils.SettingsManager
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.OfflinePlayer

object FundamentalCommand {
    fun registerKommand() {
        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = Component.text("[").color(TextColor.color(0x00aaaaaa)).append(
            Component.text("!").color(TextColor.color(0x00ff55ff))
        ).append(
            Component.text("] ").color(TextColor.color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("playermanager") {
                requires { isConsole || isPlayer }
                then("version") {
                    executes {
                        try {
                            sender.sendMessage(
                                text("PlayerManager version \"${PlayerManager.description.version}\"").color(
                                    TextColor.color(0x0055ff55)
                                )
                            )
                        } catch (e: Exception) {
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
                then("reload") {
                    executes {
                        try {
                            SettingsManager.load()
                            Language.load()
                            sender.sendMessage(
                                text("Settings, Language reload complete!").color(
                                    TextColor.color(
                                        0x0055ff55
                                    )
                                )
                            )
                        } catch (e: Exception) {
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
                then("setlanguage") {

                    requires { isPlayer }

                    val langs = string().apply {
                        suggests {
                            Language.langlist.forEach {
                                suggest(it as String)
                            }
                        }
                    }

                    then("lang" to langs) {
                        executes {
                            try {
                                val managedPlayer = ManagedPlayer(sender as OfflinePlayer, PlayerManager.playerJSONFile)

                                val lang: String by it

                                if(!Language.langlist.contains(lang)) {
                                    sender.sendMessage(mod.append(text(Language[managedPlayer.lang, "lang.notfound"]).color(
                                        TextColor.color(0x00ff5555))))
                                    return@executes
                                }

                                managedPlayer.toLang(lang)
                                sender.sendMessage(text(Language[managedPlayer.lang, "lang.success"]).color(TextColor.color(0x0055ff55)))
                            } catch (e: Exception) {
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