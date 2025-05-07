package com.pwstud.jbash.debug;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pwstud.jbash.shell.input.DefaultJBT;
import com.pwstud.jbash.shell.input.JBashTerminal;
import com.pwstud.jbash.utils.FileManager;

/**
 * Utilitarian class used in printing and logging to the terminal in a
 * controlled enviorment.
 * 
 * @see LogsManager
 * @author Cirnicianu Ciprian Nicolae
 * @version 1.3
 * @since 1.0
 */
public abstract class Debug {
  private static StringBuffer bulkBuffer = new StringBuffer();
  private static JBashTerminal terminal = DefaultJBT.getDefault();
  private static File currentLogFile = createLogFile();

  public static final void setTerminal(JBashTerminal term) {
    terminal = term;
  }

  public static final JBashTerminal getTerminal() {
    return terminal;
  }

  /**
   * Prints to the terminal and logs it to the log file.
   * 
   * @param args The arguments to be logged and printed.
   */
  public static final <T> void log(T... args) {
    if (args.length == 0) {
      terminal.print("");
    }

    for (T arg : args) {
      terminal.print(arg.toString());
    }

    try {
      FileManager.updateFile(currentLogFile, terminal.doc.getText(0, terminal.doc.getLength()), false);
    } catch (Exception e) {
      logError(e);
    }
  }

  /**
   * Prints to the terminal and logs it to the log file, each argument on a new
   * line.
   * 
   * @param args The arguments to be logged and printed to new lines.
   */
  public static final <T> void logn(T... args) {
    if (args.length == 0) {
      log("\n");
    }

    for (T arg : args) {
      log(arg, "\n");
    }
  }

  /**
   * Prints to the terminal and logs it to the log file, using the specified
   * format.
   * 
   * @param args The arguments to be logged and printed with the format.
   */
  public static final <T> void logf(String format, T... args) {
    PrintStream stream = System.out.printf(format, args);
    log(stream.toString());
  }

  /**
   * Prints the error to the terminal and logs it.
   * 
   * @param <T> Takes in a object extending {@code Exception}
   * @param e   The exception to be printed and logged.
   */
  public static final <T extends Exception> void logError(T e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String stackTrace = "\n" + sw.toString();
    log(stackTrace);
  }

  /**
   * Prints an argument without logging it.
   * 
   * @param args The arguments to be printed.
   */
  public static final <T> void out(T... args) {
    if (args.length == 0) {
      return;
    }

    for (T arg : args) {
      terminal.print(arg.toString());
    }
  }

  /**
   * Takes one or multiple arguments and puts them into a buffer to be printed
   * later.
   * 
   * @param args The arguments to be buffered.
   */
  public static final <T> void bulkIn(T... args) {
    for (T arg : args) {
      bulkBuffer.append(arg).append("\u001F");
    }
  }

  /**
   * Prints and clears the buffer, ready for the next bulk print.
   */
  public static final void bulkOut() {
    if (bulkBuffer.toString().equals(""))
      return;

    log(bulkBuffer.toString().replace("\u001F", ""));
    bulkBuffer.setLength(0);
  }

  /**
   * Prints and clear the buffer with a format, ready for the next bulk print.
   * Note that the
   * format must match the ammount of givven arguments to the bulkIn, between the
   * previous
   * bulkOut and this one.
   */
  public static final void bulkOut(String format) {
    if (bulkBuffer.toString().equals(""))
      return;

    logf(format, bulkBuffer.toString().split("\u001F"));
    bulkBuffer.setLength(0);
  }

  public static final void processCode(String code) {
    /**
     * 0 >> Exit program
     * 1 >> success
     * 10 >> Exit witchout closing program
     */

    System.out.print("#> " + code + ": ");
    switch (code) {
      case "0" -> {
        System.out.println("Exited program.");
        System.exit(0);
      }

      case "1" -> {
        System.out.println("Success.");
      }

      case "10" -> {
        System.out.println("Exited process");
      }

      default -> {
        System.out.println("Code not found.");
      }
    }
  }

  /**
   * Creates a log file wit formatted file name.
   * @return The newly created log file.
   */
  private static final File createLogFile() {
    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String logPath = "/src/main/resources/logs/log_" + timestamp + ".log";
    return FileManager.createFile(logPath);
  }
}
