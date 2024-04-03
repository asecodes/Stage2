package Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static Log instance;

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }


    public void logMessage(String message) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = timeStamp + " - " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            writer.println(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
