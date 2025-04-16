package com.pwstud.jbash.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.pwstud.jbash.debug.Debug;

/**
 * Manages log files by redirecting System.out and System.err to a timestamped
 * file.
 * Also provides functionality to delete all logs except the latest one.
 * 
 * @author GPT
 * @version 1.0
 */
public class LogsManager {
  private static File currentLogFile;
  private static PrintStream logStream;
  private static PrintStream originalOut = System.out;
  private static PrintStream originalErr = System.err;

  /**
   * Initializes log file and redirects System.out and System.err to it.
   */
  public static void initLogRedirection() {
    try {
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String logPath = "/src/main/resources/logs/log_" + timestamp + ".log";
      currentLogFile = FileManager.createFile(logPath);

      OutputStream logOutput = new OutputStream() {
        @Override
        public void write(int b) {
          String s = Character.toString((char) b);
          FileManager.updateFile(currentLogFile, s, true);
        }

        @Override
        public void write(byte[] b, int off, int len) {
          String s = new String(b, off, len);
          FileManager.updateFile(currentLogFile, s, true);
        }
      };

      // TeeOutputStream to write to both the terminal and log file
      OutputStream teeOut = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          originalOut.write(b);
          logOutput.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
          originalOut.write(b, off, len);
          logOutput.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
          originalOut.flush();
          logOutput.flush();
        }

        @Override
        public void close() throws IOException {
          originalOut.close();
          logOutput.close();
        }
      };

      OutputStream teeErr = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          originalErr.write(b);
          logOutput.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
          originalErr.write(b, off, len);
          logOutput.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
          originalErr.flush();
          logOutput.flush();
        }

        @Override
        public void close() throws IOException {
          originalErr.close();
          logOutput.close();
        }
      };

      logStream = new PrintStream(teeOut, true);
      System.setOut(logStream);
      System.setErr(new PrintStream(teeErr, true));

    } catch (Exception e) {
      Debug.logError(e);
    }
  }

  /**
   * Deletes all log files in resources/log except the latest one.
   */
  public static void deleteAllLogsExceptLatest() {
    try {
      File logDir = FileManager.getFile("resources/log");

      if (!logDir.exists() || !logDir.isDirectory())
        return;

      File[] logFiles = logDir.listFiles((dir, name) -> name.matches("log_\\d{8}_\\d{6}\\.log"));
      if (logFiles == null || logFiles.length <= 1)
        return;

      List<File> sortedLogs = Arrays.stream(logFiles)
          .sorted(Comparator.comparing(File::getName))
          .collect(Collectors.toList());

      for (int i = 0; i < sortedLogs.size() - 1; i++) {
        FileManager.deleteFile(sortedLogs.get(i));
      }
    } catch (Exception e) {
      Debug.logError(e);
    }
  }

  /**
   * Appends a message to the current log file only.
   */
  public static void out(String message) {
    if (currentLogFile != null) {
      FileManager.updateFile(currentLogFile, message + System.lineSeparator(), true);
    }
  }

  /**
   * Returns the currently active log file.
   */
  public static File getCurrentLogFile() {
    return currentLogFile;
  }

  /**
   * Logs to file and also prints to terminal.
   */
  public static void outWithEcho(String message) {
    out(message); // reuse logging method
    if (originalOut != null) {
      originalOut.print(message);
    }
  }

  /**
   * Prints to terminal only, does NOT log.
   */
  public static void terminalOnly(String message) {
    if (originalOut != null) {
      originalOut.print(message);
    }
  }
}
