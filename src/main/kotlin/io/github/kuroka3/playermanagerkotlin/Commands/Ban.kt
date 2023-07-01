package io.github.kuroka3.playermanagerkotlin.Commands

import io.github.kuroka3.playermanagerkotlin.Class.ManagedPlayer
import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import io.github.kuroka3.playermanagerkotlin.Utils.JSONFile
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor.color
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Ban {
    fun registerCommand() {

        val PlayerManagerKotlin = PlayerManagerKotlin.instance

        val mod: TextComponent = text("[").color(color(0x00aaaaaa)).append {
            text("!").color(color(0x00ff55ff))
            text("] ").color(color(0x00aaaaaa))
        }

        PlayerManagerKotlin.kommand {
            register("kotlinban") {

                requires { (isPlayer && sender.hasPermission("playermanager.ban")) || isConsole }

                then("target" to string()) {
                    then("reason" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            try {
                                val target: String by it
                                val reason: String by it
                                val id: String =
                                    (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSS"))
                                        .toLong() * 5).toString()

                                val managedPlayer: ManagedPlayer = ManagedPlayer(
                                    Bukkit.getOfflinePlayer(target),
                                    JSONFile("${PlayerManagerKotlin.dataFolder}/players.json")
                                )

                                managedPlayer.ban(reason, id)

                                if (managedPlayer.p.isOnline) {
                                    val onp: Player = Bukkit.getPlayer(managedPlayer.p.uniqueId)!!

                                    onp.showTitle(
                                        Title.title(
                                            text("당신은 이 서버에서 차단되었습니다")
                                                .color(color(0x00ff5555)),
                                            text(managedPlayer.banre)
                                                .color(color(0x00ffaa00))
                                        )
                                    )

                                    val kick: () -> Unit = {
                                        onp.kick(
                                            text("당신은 이 서버에서 차단되었습니다\n\n").color(color(0x00ff5555)).append(
                                                text("Reason: ").color(color(0x00aaaaaa))
                                            ).append(
                                                text(managedPlayer.banre).color(color(0x00ffffff))
                                            ).append(
                                                text("\n\nBan ID: ").color(color(0x00aaaaaa))
                                            ).append(
                                                text(managedPlayer.banid).color(color(0x00ffffff))
                                            )
                                        )
                                    }

                                    Bukkit.getScheduler().runTaskLater(
                                        io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin.instance,
                                        kick,
                                        100L
                                    )
                                }

                                sender.sendMessage(
                                    text("$mod").append(
                                        text("${managedPlayer.p.name}").color(color(0x00ffff55))
                                    ).append(
                                        text("님을 서버에서 차단했습니다: ").color(color(0x00ff5555))
                                    ).append(
                                        text(reason).color(color(0x00ffaa00))
                                    )
                                )
                            } catch (e: IllegalAccessException) {
                                sender.sendMessage(text("$mod").append(text("${e.message}").color(color(0x00ff55555))))
                            } catch (e: Exception) {
                                e.printStackTrace()
                                sender.sendMessage(text("$mod").append(text("Check Console: An Exception occured").color(color(0x00ff55555))))
                            }
                        }
                    }
                }
            }
        }
    }
}