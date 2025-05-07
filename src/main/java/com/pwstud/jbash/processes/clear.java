package com.pwstud.jbash.processes;

import javax.swing.text.BadLocationException;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.process.JBashProcess;

public class clear extends JBashProcess{
  @Override
  public String stdout(String[] stdin) {
    try {
      Debug.getTerminal().doc.remove(0, Debug.getTerminal().doc.getLength());
      return "1";
    } catch (BadLocationException e) {
      Debug.logError(e);
      return "0";
    }
    
  }
}
