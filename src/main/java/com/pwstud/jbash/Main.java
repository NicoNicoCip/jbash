package com.pwstud.jbash;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.TerminalBuilder;

public class Main {
  public static void main(String[] args) {
    try {
      TerminalBASIC();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void TerminalBASIC() throws IOException {

    Logger jLineLogger = Logger.getLogger("org.jline");
    Logger rootLogger = Logger.getLogger("");

    // enable logging by setting this to Level.FINEST
    Level state = Level.OFF;
    rootLogger.setLevel(state);
    jLineLogger.setLevel(state);
    rootLogger.getHandlers()[0].setLevel(state);

    Path historyFile = Paths.get(System.getProperty("user.home"), ".jbashHistory");

    LineReader reader = LineReaderBuilder.builder()
        .terminal(TerminalBuilder.builder()
            .system(true)
            .build())
        .completer(new Completer() {

          @Override
          public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String partial = line.word();
            if (partial.startsWith("m")) {
              candidates.add(new Candidate("mega"));
              candidates.add(new Candidate("mockup"));
              candidates.add(new Candidate("mold"));
            }
            // Add other completion rules...
          }
        })
        .parser(new DefaultParser() {
          @Override
          public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
            if (line.endsWith("`") && !line.trim().equals("`")) {
              // Strip the backtick if present (for display) but keep continuation behavior
              throw new EOFError(-1, cursor, "Continue input");
          }
          return new ParsedLine() {
              @Override
              public String line() {
                  return line.replace("`", ""); // Remove all backticks from final input
              }
              @Override public String word() {return "";}; 
              @Override public int wordIndex() { return 0; }
              @Override public int wordCursor() { return 0; }
              @Override public int cursor() { return cursor; }
              @Override public List<String> words() { return Collections.singletonList(line()); }
          };
        }})
        .variable(LineReader.SECONDARY_PROMPT_PATTERN, "> ")
        .history(new DefaultHistory())
        .variable(LineReader.HISTORY_FILE, historyFile)
        .variable(LineReader.HISTORY_SIZE,1000)
        .build();

    
    String input = reader.readLine(">> ").replaceAll("`\n", "\n");

    System.out.println(input);
  }
}