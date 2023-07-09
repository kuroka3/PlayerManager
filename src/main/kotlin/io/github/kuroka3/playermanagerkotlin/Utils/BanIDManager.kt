package io.github.kuroka3.playermanagerkotlin.Utils

import io.github.kuroka3.playermanagerkotlin.PlayerManagerKotlin
import org.json.simple.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

object BanIDManager {
    lateinit var banidJSONFile: JSONFile

    fun setInit() {
        banidJSONFile = JSONFile("${PlayerManagerKotlin.instance.dataFolder}/data/banid.json")
    }
    
    fun setBan(id: String?, target: UUID, moder: String?, reason: String?, time: LocalDateTime, istemp: Boolean?) {
        setBan(id, target, moder, reason, time, istemp, null)
    }

    fun setBan(
        id: String?,
        target: UUID,
        moder: String?,
        reason: String?,
        time: LocalDateTime,
        istemp: Boolean?,
        period: String?
    ) {
        val file = banidJSONFile
        val fullJson = loadJSON(file)
        val banInfo = JSONObject()
        banInfo["target"] = target.toString()
        banInfo["moder"] = moder
        banInfo["reason"] = reason
        banInfo["time"] = time.toString()
        banInfo["isTemp"] = istemp
        if (period != null) banInfo["period"] = period
        fullJson[id] = banInfo
        saveJSON(fullJson, file)
    }

    fun getBanInfo(id: String): JSONObject? {
        val file = banidJSONFile
        val jsonObject = loadJSON(file)
        return jsonObject[id] as JSONObject?
    }

    private fun loadJSON(jsonFile: JSONFile): JSONObject {
        return try {
            if (!jsonFile.isFile) {
                if (!jsonFile.createNewFile()) {
                    PlayerManagerKotlin.instance.dataFolder.mkdir()
                    jsonFile.createNewFile()
                }
                return JSONObject()
            }
            jsonFile.jsonObject!!
        } catch (e: Exception) {
            e.printStackTrace()
            JSONObject()
        }
    }

    private fun saveJSON(jsonObject: JSONObject, jsonFile: JSONFile) {
        try {
            jsonFile.saveJSONFile(jsonObject)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
