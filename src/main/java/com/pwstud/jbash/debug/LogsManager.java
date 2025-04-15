package com.pwstud.jbash.debug;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.pwstud.jbash.utils.FileManager;

public abstract class LogsManager {
  public static final String logFileCreationTime = getLogDateTime();
  public static final String logFileHead = "/src/main/resources/logs/log_" + logFileCreationTime + ".log";

  private static Logger jLineLogger = Logger.getLogger("org.jline");
  private static Logger rootLogger = Logger.getLogger("");

  // enable logging by setting this to Level.FINEST or Level.OFF to disable
  private static final Level state = Level.OFF;

  public static void createLogFile() {

    rootLogger.setUseParentHandlers(false);
    jLineLogger.setUseParentHandlers(false);

    Handler customHandler = new Handler() {
      @Override
      public void publish(LogRecord record) {
        if (record != null) {
          LogsManager.log(record.getLevel() + ": " + record.getMessage() + "\n");
        }
      }

      @Override
      public void flush() {
      }

      @Override
      public void close() throws SecurityException {
      }
    };

    for (Handler handler : rootLogger.getHandlers()) {
      rootLogger.removeHandler(handler);
    }

    rootLogger.addHandler(customHandler);
    jLineLogger.addHandler(customHandler);

    rootLogger.setLevel(state);
    jLineLogger.setLevel(state);
    rootLogger.getHandlers()[0].setLevel(state);

    File f = FileManager.createFile(logFileHead);
    FileManager.updateFile(f, "\n", true);
  }

  public static String getLogDateTime() {
    String time = LocalDateTime.now().toString().substring(11, 19).replace(":", "");
    String date = LocalDate.now().toString().replace("-", "");
    return date + "_" + time;
  }

  public static void deleteAllFiles() {
    String data = FileManager.readFile(logFileHead);
    for (File fileEntry : new File(FileManager.getProjectPath() + "/src/main/resources/logs/").listFiles()) {
      FileManager.deleteFile("/src/main/resources/logs/" + fileEntry.getName());
    }
    File f = FileManager.createFile(logFileHead);
    FileManager.updateFile(f, data, false);
    Debug.logn("Succesfuly deeleted old logs, and left out this one");
  }

  public static void log(String data) {
    try {
      if (data == null)
        return;

      FileWriter writer = new FileWriter(FileManager.getProjectPath() + logFileHead, true);
      writer.write(data);
      writer.close();
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
}
