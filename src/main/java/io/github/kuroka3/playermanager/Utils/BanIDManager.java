package io.github.kuroka3.playermanager.Utils;

import io.github.kuroka3.playermanager.PlayerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class BanIDManager {

    private static final JSONParser parser = new JSONParser();

    public static void setBan(String id, UUID target, String moder, String reason, LocalDateTime time) {

        JSONFile file = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/banid.json");

        JSONObject fullJson = loadJSON(file);

        JSONObject banInfo = new JSONObject();

        banInfo.put("target", target.toString());
        banInfo.put("moder", moder);
        banInfo.put("reason", reason);
        banInfo.put("time", time.toString());

        fullJson.put(id, banInfo);

        saveJSON(fullJson, file);
    }

    public static @Nullable JSONObject getBanInfo(String id) {

        JSONFile file = new JSONFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/banid.json");

        JSONObject jsonObject = loadJSON(file);

        return (JSONObject) jsonObject.get(id);

    }

    private static @NotNull JSONObject loadJSON(JSONFile jsonFile) {
        try {
            if (!jsonFile.isFile()) {
                if(!jsonFile.createNewFile()) {
                    PlayerManager.getPlugin(PlayerManager.class).getDataFolder().mkdir();
                    jsonFile.createNewFile();
                }
                return new JSONObject();
            }

            return jsonFile.getJSONObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private static void saveJSON(JSONObject jsonObject, JSONFile jsonFile) {
        try {
            jsonFile.saveJSONFile(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
