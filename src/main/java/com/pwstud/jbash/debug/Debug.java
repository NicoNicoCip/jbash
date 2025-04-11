package com.pwstud.jbash.debug;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utilitarian class used in printing and logging to the terminal in a controlled manner.
 * @see LogsManager
 * @author Cirnicianu Ciprian Nicolae
 * @version 1.2
 * @since 1.0
 */
public abstract class Debug {
  private static StringBuffer bulkBuffer = new StringBuffer();

  /**
   * Prints to the terminal and logs it to the log file.
   * @param args The arguments to be logged and printed.
   */
  public static final <T> void log(T ... args) {
    for (T arg : args) {
      LogsManager.log(arg.toString());
      System.out.print(arg);
    }
  }

  /**
   * Prints to the terminal and logs it to the log file, each argument on a new line.
   * @param args The arguments to be logged and printed to new lines.
   */
  public static final <T> void logn(T ... args) {
    for (T arg : args) {
      LogsManager.log(arg.toString());
      System.out.println(arg);
    }
  }

  /**
   * Prints to the terminal and logs it to the log file, using the specified format.
   * @param args The arguments to be logged and printed with the format.
   */
  public static final <T> void logf(String format, T ... args) {
    PrintStream stream = System.out.printf(format, args);
    LogsManager.log(stream.toString());
  }

  /**
   * Prints the error to the terminal and logs it.
   * @param <T> Takes in a object extending {@code Exception}
   * @param e The exception to be printed and logged.
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
   * @param args The arguments to be printed.
   */
  public static final <T> void out(T ... args) {
    System.out.print(args);
  }

  /**
   * Takes one or multiple arguments and puts them into a buffer to be printed later.
   * @param args The arguments to be buffered.
   */
  public static final <T> void bulkIn(T ... args) {
    for (T arg : args) {
      bulkBuffer.append(arg).append("\u001F");
    }
  }

  /**
   * Prints and clears the buffer, ready for the next bulk print.
   */
  public static final void bulkOut() {
    log(bulkBuffer.toString().replace("\u001F", ""));
    bulkBuffer.setLength(0);
  }

  /**
   * Prints and clear the buffer with a format, ready for the next bulk print. Note that the
   * format must match the ammount of givven arguments to the bulkIn, between the previous
   * bulkOut and this one.
   */
  public static final void bulkOut(String format) {
    logf(format, bulkBuffer.toString().split("\u001F"));
    bulkBuffer.setLength(0);
  }
}
