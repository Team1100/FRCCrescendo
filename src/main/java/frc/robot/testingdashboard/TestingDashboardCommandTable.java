package frc.robot.testingdashboard;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.wpi.first.wpilibj2.command.Command;

public class TestingDashboardCommandTable {
  Hashtable<String, Hashtable<String, Command>> table;
  /**
   * 
   */
  public TestingDashboardCommandTable() {
    try {
      table = new Hashtable<String, Hashtable<String, Command>>();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param str
   * @param cmd
   */
  public void add(String str, Command cmd) {
    if (table.containsKey(str)) {
      Hashtable<String, Command> l = table.get(str);
      if (!l.containsKey(cmd.getName()))
        l.put(cmd.getName(), cmd);
    } else {
      Hashtable<String, Command> l = new Hashtable<String, Command>();
      l.put(cmd.getName(), cmd);
      table.put(str, l);
    }
  }

  public Enumeration<String> getCommandGroups() {
    return table.keys();
  }

  public Collection<Command> getCommandList(String cmdGrpName) {
    return table.get(cmdGrpName).values();
  }
}
