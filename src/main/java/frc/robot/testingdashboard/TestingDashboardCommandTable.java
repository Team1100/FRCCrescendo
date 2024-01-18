package frc.robot.testingdashboard;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.wpi.first.wpilibj2.command.Command;

public class TestingDashboardCommandTable {
  Hashtable<String, ArrayList<Command>> table;
  /**
   * 
   */
  public TestingDashboardCommandTable() {
    try {
      table = new Hashtable<String, ArrayList<Command>>();
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
      ArrayList<Command> l = table.get(str);
      l.add(cmd);
    } else {
      ArrayList<Command> l = new ArrayList<Command>();
      l.add(cmd);
      table.put(str, l);
    }
  }

  public Enumeration<String> getCommandGroups() {
    return table.keys();
  }

  public ArrayList<Command> getCommandList(String cmdGrgName) {
    return table.get(cmdGrgName);
  }
}
