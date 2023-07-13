package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.CaseManager
import io.github.monun.kommand.KommandArgument
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.UUID

object UnWarn {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = text("[").color(color(0x00aaaaaa)).append(
            text("!").color(color(0x00ff55ff))
        ).append(
            text("] ").color(color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("unwarn") {
                requires { (isPlayer && hasPermission("playermanager.warn")) || isConsole }

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

                then("target" to int()) {
                    then("reason" to reasonString) {
                        executes {
                            try {
                                val target: Int by it
                                val reason: String by it

                                val targetobj = CaseManager.getCase(target)

                                if(targetobj == null) {
                                    sender.sendMessage(mod.append(text("해당 Case 번호를 찾을 수 없습니다").color(color(0x00ff55555))))
                                    return@executes
                                }

                                if((targetobj["type"] as Long) != 2L) {
                                    throw IllegalArgumentException("해당 Case의 Type이 Warn이 아닙니다.")
                                }

                                val targetuuid: UUID = UUID.fromString(targetobj["target"] as String)

                                val targetPlayer = Bukkit.getOfflinePlayer(targetuuid)

                                var moder: String

                                if(isConsole) moder = "Server"
                                else moder = (sender as Player).uniqueId.toString()

                                val managedPlayer = ManagedPlayer(
                                    targetPlayer,
                                    PlayerManager.playerJSONFile
                                )

                                managedPlayer.unwarn()

                                if (managedPlayer.p.isOnline) {
                                    val onp: Player = Bukkit.getPlayer(managedPlayer.p.uniqueId)!!
                                    onp.sendMessage(
                                        mod.append(
                                            text("경고가 1회 해제되었습니다: ").color(color(0x00ff5555))
                                        ).append(
                                            text(reason).color(color(0x00ffaa00))
                                        ).append(
                                            text("(총 ${managedPlayer.warns}회)")
                                                .color(color(0x00ffff55))
                                        )
                                    )
                                }

                                sender.sendMessage(
                                    mod.append(
                                        text("${managedPlayer.p.name}").color(color(0x00ffff55))
                                    ).append(
                                        text("님의 경고를 1회 취소하였습니다: ").color(color(0x00ff5555))
                                    ).append(
                                        text(reason).color(color(0x00ffaa00))
                                    ).append(
                                        text("(총 ${managedPlayer.warns}회)")
                                            .color(color(0x00ffff55))
                                    )
                                )

                                CaseManager.addCase(7, moder, managedPlayer.p.uniqueId, reason, LocalDateTime.now())
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