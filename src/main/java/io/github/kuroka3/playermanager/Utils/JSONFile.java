package io.github.kuroka3.playermanager.Utils;

import com.google.gson.JsonArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JSONFile extends File {

    private JSONObject jobj;
    private final JSONParser parser = new JSONParser();

    public JSONFile(@NotNull String pathname) {
        super(pathname);
    }

    public boolean isEmpty() throws IOException {
        JSONObject a = getJSONObject();
        return a == null;
    }

    public JSONObject getJSONObject() throws IOException {
        FileReader reader = new FileReader(this);
        try {
            jobj = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            return null;
        }

        return jobj;
    }

    public void updateJSONObject() throws IOException {
        FileReader reader = new FileReader(this);
        try {
            jobj = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            jobj = null;
        }
    }

    public void saveJSONFile(@Nullable JSONObject obj) throws IOException {
        FileWriter writer = new FileWriter(this);

        if (obj != null) {
            jobj = obj;
        }

        writer.write(obj.toJSONString());
        writer.flush();
        writer.close();
    }
}
