package com.pwstud.jbash.utils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;

import com.pwstud.jbash.debug.Debug;
import java.util.concurrent.*;

/**
 * Utility class used in the creation and management of files.
 * 
 * @author Cirnicianu Ciprian Nicolae
 * @version 1.3
 * @since 1.0
 */
public abstract class FileManager {
  private static final ConcurrentHashMap<File, StringBuilder> fileBuffers = new ConcurrentHashMap<>();
  private static final ConcurrentLinkedQueue<File> flushQueue = new ConcurrentLinkedQueue<>();
  private static volatile boolean appending = false;
  static {
    Thread flusherThread = new Thread(() -> {
      while (true) {
        try {
          File file = flushQueue.poll();
          if (file != null) {
            flushBufferToFile(file, appending);
            appending = false;
            Thread.sleep(1000); // Wait one second AFTER writing is done
          } else {
            Thread.sleep(50); // Avoid tight loop
          }
        } catch (Exception e) {
          Debug.logError(e);
        }
      }
    });
    flusherThread.setDaemon(true);
    flusherThread.start();
  }

  /**
   * Gets the absolute path of the project's root directory.
   */
  public static String getProjectPath() {
    return Paths.get("").toAbsolutePath().toString().replace("\\", "/") + "/";
  }

  /**
   * Creates a new file with metadata stored in the first line.
   */
  public static File createFile(String filePath) {
    try {
      File file = new File(getProjectPath() + filePath);

      if (file.getParentFile() != null) {
        file.getParentFile().mkdirs();
      }

      if (!file.exists()) {
        file.createNewFile();
        updateFile(file, "", false); // Initialize with metadata
      }
      return file;
    } catch (Exception e) {
      Debug.logError(e);
      return null;
    }
  }

  /**
   * Writes data to the file, either appending or overwriting it.
   */
  public static boolean updateFile(File file, String text, boolean append) {
    try {
      fileBuffers.compute(file, (f, buf) -> {
        if (buf == null) buf = new StringBuilder();
        synchronized (buf) {
          buf.append(text);
        }
        return buf;
      });
  
      if (!flushQueue.contains(file)) {
        flushQueue.add(file);
      }

      appending = Boolean.valueOf(append);
  
      return true;
    } catch (Exception e) {
      Debug.logError(e);
      return false;
    }
  }
  

  /**
   * Reads the full contents of a file as a string.
   */
  public static String readFile(String filePath) {
    return readFile(new File(getProjectPath(), filePath));
  }

  /**
   * Reads the full contents of a file as a string.
   */
  public static String readFile(File file) {
    try (Scanner scanner = new Scanner(file)) {
      StringBuilder output = new StringBuilder();
      while (scanner.hasNextLine()) {
        output.append(scanner.nextLine());
      }
      String fileData = output.toString();

      StringBuilder buffer = fileBuffers.get(file);
      if (buffer != null) {
        synchronized (buffer) {
          fileData += buffer.toString(); // Combine with unwritten buffer
        }
      }

      return fileData;
    } catch (Exception e) {
      Debug.logError(e);
      return null;
    }
  }

  /**
   * Flushes the contents of the buffer to a file. this is helpfull for deferring
   * file manipulation, keeping a seperate thread on the side, that manages just that.
   * @param file
   */
  private static void flushBufferToFile(File file, boolean append) {
    try {
      StringBuilder buffer = fileBuffers.get(file);
      if (buffer == null) return;
  
      String toWrite;
      synchronized (buffer) {
        if (buffer.length() == 0) return; // Nothing to flush
        toWrite = buffer.toString();
        buffer.setLength(0); // Clear buffer
      }
  
      try (FileWriter writer = new FileWriter(file, append)) {
        writer.write(toWrite);
      }
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
  
  /**
   * Reads a file and splits its content into an array based on a separator.
   */
  public static String[] readLines(File file, String separator) {
    return readFile(file).split(separator);
  }

  /**
   * Deletes a file.
   */
  public static boolean deleteFile(File file) {
    return file.delete();
  }

  /**
   * Deletes a file by path.
   */
  public static boolean deleteFile(String filePath) {
    return new File(getProjectPath(), filePath).delete();
  }

  /**
   * Checks if a file exists.
   */
  public static boolean fileExists(File file) {
    return file.exists();
  }

  /**
   * Checks if a file exists by path.
   */
  public static boolean fileExists(String filePath) {
    return new File(getProjectPath(), filePath).exists();
  }

  /**
   * Retrieves a File object for a given path.
   */
  public static File getFile(String filePath) {
    return new File(getProjectPath(), filePath);
  }
}
