package com.pwstud.jbash.shell.input;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class JBashTerminal {
  // UI Components
  public JFrame frame = new JFrame("JBash Terminal");
  public JTextPane terminalPane = new JTextPane();
  public JScrollPane scrollPane = new JScrollPane(terminalPane);
  public StyledDocument doc = terminalPane.getStyledDocument();
  public Style style = terminalPane.addStyle("TerminalStyle", null);
  public Font font = new Font("Courier New", Font.PLAIN, 14);
  public FontMetrics fontMetrics;

  // Dimensions
  public int termColumns = 95;
  public int termRows = 60;
  public int pixelWidth, pixelHeight;
  public int posX = 64, posY = 64;
  public boolean visible = true;

  // State and Utilities
  public EventBus eventBus = new EventBus();
  private final List<String> commandHistory = new ArrayList<>();
  private final AutoCompleter autoCompleter = new AutoCompleter();
  private StringBuilder currentCommand = new StringBuilder();
  private int historyIndex = -1;
  private int promptOffset = 0;
  public boolean deferedCommand = false;

  private List<String> currentCompletions = Collections.emptyList();
  private int completionIndex = -1;

  public JBashTerminal() {
    initializeTerminalUI();
  }

  /*** UI Initialization ***/
  private void initializeTerminalUI() {
    updateFontMetrics();
    setupFrame();
    setupListeners();
    terminalPane.requestFocusInWindow();
  }

  public void updateFontMetrics() {
    terminalPane.setFont(font);
    fontMetrics = terminalPane.getFontMetrics(font);
    int charWidth = fontMetrics.charWidth('M');
    pixelWidth = termColumns * charWidth;
    pixelHeight = termRows * fontMetrics.getHeight();
  }

  public void setupFrame() {
    terminalPane.setEditable(false);
    terminalPane.setFocusTraversalKeysEnabled(false);

    frame.setContentPane(scrollPane);
    frame.setSize(pixelWidth, pixelHeight);
    frame.setLocation(posX, posY);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(visible);
  }

  /*** Listeners ***/
  private void setupListeners() {
    setupKeyboardInput();
    setupMouseInput();
    setupResizeListener();
    setupCaretListener();
  }

  private void setupKeyboardInput() {
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
          case KeyEvent.VK_ENTER -> executeCommand();
          case KeyEvent.VK_UP -> navigateHistory(-1);
          case KeyEvent.VK_DOWN -> navigateHistory(1);
          case KeyEvent.VK_TAB -> {
            e.consume();
            handleAutocomplete();
          }
          case KeyEvent.VK_SPACE -> resetAutocomplete();
        }
      }
    });
  }

  private void setupCaretListener() {
    terminalPane.addCaretListener(e -> {
      if (terminalPane.getCaretPosition() < promptOffset) {
        SwingUtilities.invokeLater(() -> terminalPane.setCaretPosition(doc.getLength()));
      }
    });
  }

  private void setupMouseInput() {
    terminalPane.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int col = e.getX() / fontMetrics.charWidth('M');
        int row = e.getY() / fontMetrics.getHeight();
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
        int col = e.getX() / fontMetrics.charWidth('M');
        int row = e.getY() / fontMetrics.getHeight();
        consumer.accept(col, row, e.getButton());
      }

      @FunctionalInterface
      interface MouseEventConsumer {
        void accept(int col, int row, int button);
      }
    });
  }

  private void setupResizeListener() {
    frame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        termColumns = terminalPane.getWidth() / fontMetrics.charWidth('M');
        termRows = terminalPane.getHeight() / fontMetrics.getHeight();
        eventBus.fireResize(termColumns, termRows);
      }
    });
  }

  /*** Command Handling ***/
  public void printPrompt() {
    print(">> ");
    promptOffset = doc.getLength();
  }

  private void executeCommand() {
    print("\n");
    String command = currentCommand.toString().trim();
    if (!command.isEmpty()
        && (commandHistory.isEmpty() || !command.equals(commandHistory.get(commandHistory.size() - 1)))) {
      commandHistory.add(command);
    }

    historyIndex = commandHistory.size();

    if(deferedCommand) {
      SwingUtilities.invokeLater(() -> new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() {
          eventBus.fireCommand(command);
          return null;
        }
      }.execute());
      deferedCommand = false;
    } else {
      eventBus.fireCommand(command);
    }

    currentCommand.setLength(0);
    resetAutocomplete();
    printPrompt();
  }

  private void navigateHistory(int direction) {
    int newIndex = historyIndex + direction;
    if (newIndex < 0 || newIndex >= commandHistory.size())
      return;

    historyIndex = newIndex;
    currentCommand.setLength(0);
    currentCommand.append(commandHistory.get(historyIndex));

    try {
      removeText(promptOffset, doc.getLength() - promptOffset);
      insertText(currentCommand.toString());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  private void insertChar(char c) {
    int caret = terminalPane.getCaretPosition();
    if (caret < promptOffset)
      return;

    int relPos = caret - promptOffset;
    try {
      doc.insertString(caret, String.valueOf(c), style);
      terminalPane.setCaretPosition(caret + 1);
      currentCommand.insert(relPos, c);
      resetAutocomplete();
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  private void handleBackspace() {
    int caret = terminalPane.getCaretPosition();
    if (caret <= promptOffset || currentCommand.length() == 0)
      return;

    int relPos = caret - promptOffset - 1;
    try {
      removeText(caret - 1, 1);
      terminalPane.setCaretPosition(caret - 1);
      currentCommand.deleteCharAt(relPos);
      resetAutocomplete();
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  private void handleAutocomplete() {
    int caret = terminalPane.getCaretPosition();
    if (caret < promptOffset)
      return;

    int relCaret = caret - promptOffset;
    String text = currentCommand.toString();
    int wordStart = text.lastIndexOf(' ', relCaret - 1) + 1;
    String prefix = text.substring(wordStart, relCaret);

    if (prefix.isEmpty())
      return;

    if (currentCompletions.isEmpty()) {
      currentCompletions = autoCompleter.completeAll(prefix);
      completionIndex = 0;
    } else {
      completionIndex = (completionIndex + 1) % currentCompletions.size();
    }

    if (!currentCompletions.isEmpty()) {
      String full = currentCompletions.get(completionIndex);
      try {
        int docStart = promptOffset + wordStart;
        int docEnd = promptOffset + relCaret;
        doc.remove(docStart, docEnd - docStart);
        doc.insertString(docStart, full, style);
        currentCommand.replace(wordStart, relCaret, full);
        terminalPane.setCaretPosition(promptOffset + wordStart + full.length());
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
  }

  private void resetAutocomplete() {
    currentCompletions = Collections.emptyList();
    completionIndex = -1;
  }

  /*** Utility Output Methods ***/
  public void print(String text) {
    try {
      boolean atEnd = terminalPane.getCaretPosition() == doc.getLength();
      doc.insertString(doc.getLength(), text, style);
      if (atEnd)
        terminalPane.setCaretPosition(doc.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  private void insertText(String text) throws BadLocationException {
    doc.insertString(promptOffset, text, style);
    terminalPane.setCaretPosition(doc.getLength());
  }

  private void removeText(int offset, int length) throws BadLocationException {
    doc.remove(offset, length);
  }

  public AutoCompleter getAutoCompleter() {
    return autoCompleter;
  }
}
