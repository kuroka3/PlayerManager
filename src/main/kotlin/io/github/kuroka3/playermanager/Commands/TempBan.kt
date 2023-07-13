package io.github.kuroka3.playermanager.Commands

import io.github.kuroka3.playermanager.Class.ManagedPlayer
import io.github.kuroka3.playermanager.PlayerManager
import io.github.kuroka3.playermanager.Utils.BanIDManager
import io.github.kuroka3.playermanager.Utils.CaseManager
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

object TempBan {
    fun registerKommand() {

        val PlayerManager = PlayerManager.instance

        val mod: TextComponent = text("[").color(color(0x00aaaaaa)).append(
            text("!").color(color(0x00ff55ff))
        ).append(
            text("] ").color(color(0x00aaaaaa))
        )

        PlayerManager.kommand {
            register("tempban") {
                requires { (isPlayer && hasPermission("playermanager.ban")) || isConsole }

                val onlinePlayer = string().apply {
                    suggests {
                        Bukkit.getOnlinePlayers().forEach {
                            suggest(it.name)
                        }
                    }
                }

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

                val periodSuggest = long().apply {
                    suggests {
                        suggest(
                            listOf(
                                "1",
                                "5",
                                "10",
                                "30",
                                "90",
                                "365"
                            )
                        )
                    }
                }

                val unitSuggest = string().apply {
                    suggests {
                        suggest(
                            listOf(
                                "days", "hours", "minutes", "seconds"
                            )
                        )
                    }
                }

                then("target" to onlinePlayer) {
                    then("period" to periodSuggest) {
                        then("unit" to unitSuggest) {
                            then("reason" to reasonString) {
                                executes {
                                    try {
                                        val target: String by it
                                        val reason: String by it
                                        val period: Long by it
                                        val unit: String by it
                                        val id: String =
                                            (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSS"))
                                                .toLong() * 5).toString()

                                        var moder: String

                                        if(isConsole) moder = "Server"
                                        else moder = (sender as Player).uniqueId.toString()

                                        val targetPlayer = Bukkit.getOfflinePlayer(target)

                                        if (!targetPlayer.hasPlayedBefore()) throw IllegalArgumentException("Targeted player not found")

                                        val managedPlayer = ManagedPlayer(
                                            Bukkit.getOfflinePlayer(target),
                                            PlayerManager.playerJSONFile
                                        )

                                        var toban = LocalDateTime.now()

                                        when(unit) {
                                            "days" -> toban = toban.plusDays(period)
                                            "hours" -> toban = toban.plusHours(period)
                                            "minutes" -> toban = toban.plusMinutes(period)
                                            "seconds" -> toban = toban.plusSeconds(period)
                                            else -> throw IllegalArgumentException("Unit Mismatched")
                                        }

                                        managedPlayer.tempban(reason, id, toban)

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
                                                    text("당신은 이 서버에서 차단되었습니다\n\n${managedPlayer.getNokori(LocalDateTime.now(), "d일 h시간 m분 s초 남음")}\n\n").color(color(0x00ff5555)).append(
                                                        text("Reason: ").color(color(0x00aaaaaa))).append(
                                                        text(managedPlayer.banre).color(color(0x00ffffff))).append(
                                                        text("\n\nBan ID: ").color(color(0x00aaaaaa))).append(
                                                        text(managedPlayer.banid).color(color(0x00ffffff)))
                                                )
                                            }

                                            Bukkit.getScheduler().runTaskLater(
                                                io.github.kuroka3.playermanager.PlayerManager.instance,
                                                kick,
                                                100L
                                            )
                                        }

                                        sender.sendMessage(
                                            mod.append(
                                                text("${managedPlayer.p.name}").color(color(0x00ffff55))
                                            ).append(
                                                text("님을 서버에서 차단했습니다: ").color(color(0x00ff5555))
                                            ).append(
                                                text(reason).color(color(0x00ffaa00))
                                            )
                                        )

                                        BanIDManager.setBan(id, managedPlayer.p.uniqueId, moder, reason, LocalDateTime.now(), true, "$period$unit")
                                        CaseManager.addCase(4, moder, managedPlayer.p.uniqueId, reason, LocalDateTime.now(), "$period$unit")
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
    }
}