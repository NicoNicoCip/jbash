package com.pwstud.jbash.shell.input;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * The JBAsh custom terminal. Provides stiling, event reporting,
 * auto completes and custiomisation details.
 */
public class JBashTerminal {
  private final JTextPane terminalPane = new JTextPane();
  private final StyledDocument doc = terminalPane.getStyledDocument();
  private final Style style = terminalPane.addStyle("TerminalStyle", null);
  private final EventBus eventBus;

  private final List<String> commandHistory = new ArrayList<>();
  private final AutoCompleter autoCompleter = new AutoCompleter();

  private int historyIndex = -1;
  private StringBuilder currentCommand = new StringBuilder();
  private int promptOffset = 0;

  private List<String> currentCompletions = Collections.emptyList();
  private int completionIndex = -1;

  public JBashTerminal(JFrame frame, EventBus eventBus) {
    this.eventBus = eventBus;
    setupTerminalUI(frame);
    printPrompt();
  }

  private void setupTerminalUI(JFrame frame) {
    terminalPane.setFont(new Font("Courier New", Font.PLAIN, 14));
    terminalPane.setEditable(false);
    terminalPane.setFocusTraversalKeysEnabled(false);

    JScrollPane scrollPane = new JScrollPane(terminalPane);
    frame.setContentPane(scrollPane);
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    terminalPane.requestFocusInWindow();

    terminalPane.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!Character.isISOControl(c)) {
          insertChar(c);
          eventBus.fireKey(c);
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        eventBus.fireKeyPress(e);

        switch (e.getKeyCode()) {
          case KeyEvent.VK_BACK_SPACE -> handleBackspace();
          case KeyEvent.VK_UP -> navigateHistory(-1);
          case KeyEvent.VK_DOWN -> navigateHistory(1);
          case KeyEvent.VK_LEFT -> {
            e.consume();
            moveCaret(-1);
          }
          case KeyEvent.VK_RIGHT -> {
            e.consume();
            moveCaret(1);
          }
          case KeyEvent.VK_ENTER -> executeCommand();
          case KeyEvent.VK_SPACE -> resetAutocomplete();
          case KeyEvent.VK_TAB -> {
            e.consume(); // Prevent default tab space
            handleAutocomplete();
          }
        }
      }
    });

    terminalPane.addCaretListener(e -> {
      int pos = terminalPane.getCaretPosition();
      if (pos < promptOffset) {
        SwingUtilities.invokeLater(() -> terminalPane.setCaretPosition(doc.getLength()));
      }
    });

    terminalPane.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        FontMetrics fm = terminalPane.getFontMetrics(terminalPane.getFont());
        int charWidth = fm.charWidth('M');

        int col = e.getX() / charWidth;
        int row = e.getY() / fm.getHeight();
        eventBus.fireMouseMove(col, row);
      }
    });

    terminalPane.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        fireMouseEvent(e, eventBus::fireMousePress);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        fireMouseEvent(e, eventBus::fireMouseRelease);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        fireMouseEvent(e, eventBus::fireMouseClick);
      }

      private void fireMouseEvent(MouseEvent e, MouseEventConsumer consumer) {
        FontMetrics fm = terminalPane.getFontMetrics(terminalPane.getFont());
        int charWidth = fm.charWidth('M');

        int col = e.getX() / charWidth;
        int row = e.getY() / fm.getHeight();
        consumer.accept(col, row, e.getButton());
      }

      @FunctionalInterface
      interface MouseEventConsumer {
        void accept(int col, int row, int button);
      }
    });

    frame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        FontMetrics fm = terminalPane.getFontMetrics(terminalPane.getFont());
        int charWidth = fm.charWidth('M');

        int cols = terminalPane.getWidth() / charWidth;
        int rows = terminalPane.getHeight() / fm.getHeight();
        eventBus.fireResize(cols, rows);
      }
    });
  }

  private void printPrompt() {
    String prompt = "> ";
    print(prompt);
    promptOffset = doc.getLength();
  }

  public void print(String text) {
    try {
      int oldCaret = terminalPane.getCaretPosition();
      boolean atEnd = oldCaret == doc.getLength();
      doc.insertString(doc.getLength(), text, style);
      if (atEnd)
        terminalPane.setCaretPosition(doc.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  public void println(String text) {
    print(text + "\n");
  }

  private void newLine() {
    print("\n");
  }

  private void insertChar(char c) {
    int caret = terminalPane.getCaretPosition();
    if (caret < promptOffset)
      return;

    int relativePos = caret - promptOffset;
    try {
      doc.insertString(caret, String.valueOf(c), style);
      terminalPane.setCaretPosition(caret + 1);
      currentCommand.insert(relativePos, c);
      resetAutocomplete();
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  private void handleBackspace() {
    int caret = terminalPane.getCaretPosition();
    if (caret <= promptOffset || currentCommand.length() == 0)
      return;

    int relativePos = caret - promptOffset - 1;
    try {
      doc.remove(caret - 1, 1);
      terminalPane.setCaretPosition(caret - 1);
      currentCommand.deleteCharAt(relativePos);
      resetAutocomplete();
    } catch (BadLocationException ex) {
      ex.printStackTrace();
    }
  }

  private void moveCaret(int direction) {
    int caret = terminalPane.getCaretPosition();
    int newCaret = caret + direction;
    if (newCaret < promptOffset || newCaret > doc.getLength())
      return;

    terminalPane.setCaretPosition(newCaret);
  }

  private void navigateHistory(int direction) {
    int newIndex = historyIndex + direction;
    if (newIndex < 0 || newIndex >= commandHistory.size())
      return;

    // Erase current command from view
    try {
      doc.remove(promptOffset, doc.getLength() - promptOffset);
      doc.insertString(promptOffset, currentCommand.toString(), style);
      currentCommand.setLength(0);
      currentCommand.append(currentCommand.toString());
      terminalPane.setCaretPosition(doc.getLength());
    } catch (BadLocationException ex) {
      ex.printStackTrace();
    }

    historyIndex = newIndex;
    String previous = commandHistory.get(historyIndex);
    currentCommand.setLength(0);
    currentCommand.append(previous);
    print(previous);
  }

  private void executeCommand() {
    newLine();
    String command = currentCommand.toString().trim();

    if (!command.isEmpty() && (commandHistory.isEmpty() ||
        !command.equals(commandHistory.get(commandHistory.size() - 1)))) {
      commandHistory.add(command);
    }

    historyIndex = commandHistory.size();
    SwingUtilities.invokeLater(() -> {
      new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() {
          eventBus.fireCommand(command);
          return null;
        }
      }.execute();
    });
    currentCommand.setLength(0);
    resetAutocomplete();
    printPrompt();
  }

  public AutoCompleter getAutoCompleter() {
    return autoCompleter;
  }

  private void handleAutocomplete() {
    int caret = terminalPane.getCaretPosition();
    if (caret < promptOffset)
      return;

    int relativeCaret = caret - promptOffset;
    String text = currentCommand.toString();
    int lastSpace = text.lastIndexOf(' ', relativeCaret - 1);

    int wordStart = (lastSpace == -1) ? 0 : lastSpace + 1;
    String prefix = text.substring(wordStart, relativeCaret);

    if (prefix.isEmpty())
      return;

    if (currentCompletions.isEmpty()) {
      currentCompletions = autoCompleter.completeAll(prefix);
      completionIndex = 0;
    } else {
      completionIndex = (completionIndex + 1) % currentCompletions.size();
    }

    if (!currentCompletions.isEmpty()) {
      String fullCompletion = currentCompletions.get(completionIndex);

      // Only replace the current word
      try {
        int docStart = promptOffset + wordStart;
        int docEnd = promptOffset + relativeCaret;
        doc.remove(docStart, docEnd - docStart);
        doc.insertString(docStart, fullCompletion, style);

        // Update currentCommand accordingly
        currentCommand.replace(wordStart, relativeCaret, fullCompletion);
        terminalPane.setCaretPosition(promptOffset + wordStart + fullCompletion.length());
      } catch (BadLocationException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void resetAutocomplete() {
    currentCompletions = Collections.emptyList();
    completionIndex = -1;
  }
}
