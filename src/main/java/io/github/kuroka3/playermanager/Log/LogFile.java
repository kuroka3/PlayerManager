package io.github.kuroka3.playermanager.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogFile extends File {
    private StringBuilder log;

    public LogFile(@NotNull String pathname) {
        super(pathname);
    }

    /**
     * 로그 파일에 로그를 추가하기 위한 코드
     * @param msg 로그
     * @param now 현재 시간
     */
    public void addLog(String msg, LocalDateTime now) {
        log.append("[" + now.toString() + "] " + msg + "\n");
    }

    /**StringBuilder를 기반으로 로그파일 저장*/
    public void saveLog() throws IOException {
        FileWriter writer = new FileWriter(this);
        writer.write(log.toString());
        writer.flush();
        writer.close();
    }

    /**로그 불러오기*/
    public StringBuilder getLog() {
        return log;
    }
}
