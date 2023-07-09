package io.github.kuroka3.playermanagerkotlin.Utils

import io.github.kuroka3.playermanagerkotlin.PlayerManager
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.util.*

object CaseManager {
    /**
     * @param type 0: kick, 1: ban, 2: warn, 3: mute, 4: TempBan, 5: unBan, 6: unMute
     */
    fun addCase(type: Int, moder: String, target: UUID, reason: String?, tempBan: String?) {
        try {
            val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/Case.json")
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
            val file = JSONFile(PlayerManager.instance.dataFolder.toString() + "/Case.json")
            (file.jsonObject!!["case"] as JSONArray)[caseInt - 1] as JSONObject?
        } catch (e: Exception) {
            null
        }
    }
}
