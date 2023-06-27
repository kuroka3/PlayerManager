package io.github.kuroka3.playermanager.Utils;

import io.github.kuroka3.playermanager.PlayerManager;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CaseManager {

    /**
     * @param type 0: kick, 1: ban, 2: warn, 3: mute, 4: TempBan, 5: unBan, 6: unMute
     */
    public static void addCase(int type, String moder, UUID target, String reason, @Nullable String tempBan) {
        try {
            JSONFile file = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/Case.json");

            if(!file.isFile()) {
                file.createNewFile();
            }

            if(file.isEmpty()) {
                JSONObject temp = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                temp.put("case", jsonArray);
                file.saveJSONFile(temp);
            }

            JSONObject object = file.getJSONObject();

            JSONArray array = (JSONArray) object.get("case");

            int caseInt = array.size() + 1;

            JSONObject caseobj = new JSONObject();

            caseobj.put("case", caseInt);
            caseobj.put("type", type);
            caseobj.put("moder", moder.toString());
            caseobj.put("target", target.toString());
            caseobj.put("reason", reason);
            if (tempBan != null) caseobj.put("tempban", tempBan);

            array.add(caseobj);

            object.put("case", array);

            file.saveJSONFile(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getCase(int caseInt) {
        try {
            JSONFile file = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/Case.json");

            return (JSONObject) ((JSONArray) file.getJSONObject().get("case")).get(caseInt - 1);
        } catch (Exception e) {
            return null;
        }
    }
}
