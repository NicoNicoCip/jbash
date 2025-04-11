package com.pwstud.jbash.debug;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pwstud.jbash.utils.FileManager;

public abstract class LogsManager {
  public static final String logFileCreationTime = getLogDateTime();
  public static final String logFileHead = "/src/main/resources/logs/log_" + logFileCreationTime + ".log";

  public static void createLogFile() {
    File f = FileManager.createFile(logFileHead);
    FileManager.updateFile(f,"\n", true);
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
      FileWriter writer = new FileWriter(FileManager.getProjectPath() + logFileHead, true);
      writer.write(data);
      writer.close();
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
}
