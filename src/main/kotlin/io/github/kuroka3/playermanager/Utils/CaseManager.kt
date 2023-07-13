package io.github.kuroka3.playermanager.Utils

import io.github.kuroka3.playermanager.PlayerManager
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.time.LocalDateTime
import java.util.*

object CaseManager {
    /**
     * @param type 0: kick, 1: ban, 2: warn, 3: mute, 4: TempBan, 5: unBan, 6: unMute, 7: unWarn
     */
    fun addCase(type: Int, moder: String, target: UUID, reason: String?, time: LocalDateTime, tempBan: String? = null) {
        try {
            val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/data/case.json")
            if (!file.isFile) {
                file.createNewFile()
            }
            if (file.isEmpty) {
                val temp = JSONObject()
                val jsonArray = JSONArray()
                temp["case"] = jsonArray
                file.saveJSONFile(temp)
            }
            val `object` = file.jsonObject
            val array = `object`!!["case"] as JSONArray
            val caseInt = array.size + 1
            val caseobj = JSONObject()
            caseobj["case"] = caseInt
            caseobj["type"] = type
            caseobj["moder"] = moder
            caseobj["target"] = target.toString()
            caseobj["reason"] = reason
            caseobj["time"] = time.toString()
            if (tempBan != null) caseobj["tempban"] = tempBan
            array.add(caseobj)
            `object`["case"] = array
            file.saveJSONFile(`object`)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCase(caseInt: Int): JSONObject? {
        return try {
            val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/data/case.json")
            (file.jsonObject!!["case"] as JSONArray)[caseInt - 1] as JSONObject?
        } catch (e: Exception) {
            null
        }
    }
    fun getLatest(): Int {
        return try {
            val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/data/case.json")
            (file.jsonObject!!["case"] as JSONArray).size
        } catch (e: Exception) {
            0
        }
    }

    fun giveReason(caseInt: Int, reason: String) {
        val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/data/case.json")
        val fullobj: JSONObject
        try {
            fullobj = file.jsonObject!!
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("해당 Case 번호를 찾을 수 없습니다")
        }

        val fullarray = fullobj["case"] as JSONArray
        var obj: JSONObject
        try {
            obj = fullarray[caseInt - 1] as JSONObject
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("해당 Case 번호를 찾을 수 없습니다")
        }

        if(obj["reason"] != "No Reason Given") {
            throw IllegalAccessException("지정한 Case에 등록된 Reason이 [ No Reason Given ] 일 때만 Reason부여가 가능합니다.")
        }

        obj["reason"] = reason
        obj["isUnWarned"] = true
        fullarray[caseInt - 1] = obj
        fullobj["case"] = fullarray
        file.saveJSONFile(fullobj)
    }
}
