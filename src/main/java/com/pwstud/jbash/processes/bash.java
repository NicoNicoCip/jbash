package com.pwstud.jbash.processes;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.shell.process.JBashProcess;
import com.pwstud.jbash.shell.process.ProcessManager;
import com.pwstud.jbash.utils.StringUtils;

public class bash extends JBashProcess {
  Input input = new Input();

  @Override
  public String stdout(String[] stdin) {

    String in = Input.readBlocking();
    if(in.equals("")) return "10"; // exit without closing

    String[] command = StringUtils.breakApart(in);
    JBashProcess proc = ProcessManager.getProcessByID(command[0].toLowerCase());

    if (proc == null) {
      Debug.log("The command \"", command[0], "\" not found.", "\n");
      return "10"; // exit without closing
    }

    return proc.stdout(command);
  }
}
