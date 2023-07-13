package io.github.kuroka3.playermanager.Class

import io.github.kuroka3.playermanager.Utils.JSONFile
import org.bukkit.OfflinePlayer
import org.json.simple.JSONObject
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ManagedPlayer(pP: OfflinePlayer, jsonFileP: JSONFile) {
    var p: OfflinePlayer
    private var jsonFile: JSONFile
    var warns: Int
    var ban: Boolean
    var banid: String
    var banre: String
    var mute: Boolean
    var temp: Boolean
    var tounban: String
    var jobj: JSONObject

    init {
        this.p = pP
        this.jsonFile = jsonFileP

        if (!jsonFile.isFile || jsonFile.isEmpty) {
            jsonFile.createNewFile()
            jsonFile.saveJSONFile(JSONObject())
        }

        val tmobj: JSONObject = jsonFile.jsonObject!!

        this.jobj = tmobj[p.uniqueId.toString()] as JSONObject

        this.warns = (jobj["warns"] as Long).toInt()
        this.ban = jobj["ban"] as Boolean
        this.banid = jobj["banid"] as String
        this.banre = jobj["banre"] as String
        this.mute = jobj["mute"] as Boolean
        this.temp = jobj["temp"] as Boolean
        this.tounban = jobj["tounban"] as String
    }

    fun warn() {
        warns++
        jobj["warns"] = warns
        save()
    }

    fun setWarn(i: Int) {
        if(i < 0) throw IllegalArgumentException("The number of warn cannot be negative")

        warns = i
        jobj["warns"] = warns
        save()
    }

    fun ban(re: String, id: String) {
        if(ban) throw IllegalAccessException("Targeted player is already banned")

        ban = true
        banre = re
        banid = id

        jobj["ban"] = ban
        jobj["banre"] = banre
        jobj["banid"] = banid

        save()
    }

    fun unban(isTemp: Boolean = false) {
        if (!ban) throw IllegalAccessException("Targeted player is not banned")

        if(isTemp) {
            temp = false
            tounban = "-1"
            jobj["temp"] = temp
            jobj["tounban"] = tounban
        }

        ban = false
        jobj["ban"] = ban
        save()
    }

    fun mute() {
        if(mute) throw IllegalAccessException("Targeted player is already muted")

        mute = true
        jobj["mute"] = mute
        save()
    }

    fun unmute() {
        if(!mute) throw IllegalAccessException("Targeted player is not muted")

        mute = false
        jobj["mute"] = mute
        save()
    }

    fun unwarn() {
        if(warns < 1) throw IllegalAccessException("Targeted player is not warned")

        warns--
        jobj["warns"] = warns
        save()
    }

    fun isToUnban(now: LocalDateTime): Boolean {
        return if(tounban == "-1") false
        else now.isAfter(LocalDateTime.parse(tounban))
    }

    fun tempban(re: String, id: String, toban: LocalDateTime) {
        if (ban) throw IllegalAccessException("Targeted player is already banned")

        ban = true
        temp = true

        tounban = toban.toString()
        banre = re
        banid = id

        jobj["ban"] = ban
        jobj["banre"] = banre
        jobj["banid"] = banid
        jobj["temp"] = temp
        jobj["tounban"] = tounban

        save()
    }

    fun getNokori(now: LocalDateTime, format: String): String {
        var a: String = format

        if (tounban == "-1") {
            a = a.replace("d", "0")
            a = a.replace("h", "0")
            a = a.replace("m", "0")
            a = a.replace("s", "0")

            return a
        } else {
            val tounbanLocalDateTime: LocalDateTime = LocalDateTime.parse(tounban)

            val array: Array<Int> = arrayOf(
                ChronoUnit.DAYS.between(now, tounbanLocalDateTime).toInt(),
                ChronoUnit.HOURS.between(now, tounbanLocalDateTime).toInt()%24,
                ChronoUnit.MINUTES.between(now, tounbanLocalDateTime).toInt()%60,
                ChronoUnit.SECONDS.between(now, tounbanLocalDateTime).toInt()%60
            )

            a = a.replace("d", array[0].toString())
            a = a.replace("h", array[1].toString())
            a = a.replace("m", array[2].toString())
            a = a.replace("s", array[3].toString())

            return a
        }
    }

    fun save() {

        val tmobj: JSONObject = jsonFile.jsonObject!!

        tmobj[p.uniqueId.toString()] = jobj

        jsonFile.saveJSONFile(tmobj)
    }

}