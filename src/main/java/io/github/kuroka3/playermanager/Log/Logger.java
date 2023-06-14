package io.github.kuroka3.playermanager.Log;

import io.github.kuroka3.playermanager.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static LogFile logFile;
    public static void log(@NotNull String m) {
        logFile.addLog(m, LocalDateTime.now());
    }

    public static void saveLog() {
        try {
            logFile.saveLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createLogFile() {
        try {
            logFile = new LogFile(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + ".txt");


            if (!logFile.isFile()) {
                logFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
