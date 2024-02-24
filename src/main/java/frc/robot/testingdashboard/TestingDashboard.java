/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.testingdashboard;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class sets up a testing dashboard using
 * WPILib's ShuffleBoard. The testing dashboard
 * contains a single tab for every tabName
 * containing the tabName status and all
 * commands associated with that tabName.
 * 
 * There is also a debug tab that contains sensor
 * variables and debug values.
 * 
 * This class is a singleton that should be
 * instantiated in the robotInit method
 * of the Robot class.
 */
public class TestingDashboard {
  private static TestingDashboard testingDashboard;
  private HashMap<String, TestingDashboardTab> testingTabs;
  boolean initialized = false;
    
  private TestingDashboard() {
    testingTabs = new HashMap<String, TestingDashboardTab>();
    initialized = false;
  }

  public static TestingDashboard getInstance() {
    if (testingDashboard == null) {
      testingDashboard = new TestingDashboard();
    }
    return testingDashboard;
  }

  private boolean hasTab(String tabName) {
    return testingTabs.containsKey(tabName);
  }

  private TestingDashboardTab getTab(String tabName) {
    return testingTabs.get(tabName);
  }

  /*
   * This function registers a tab with
   * the testing dashboard.
   */
  void registerTab(String tabName) {
    if (hasTab(tabName)) {
      // Subsystem has already been registered
      return;
    }
    TestingDashboardTab tdt = new TestingDashboardTab(tabName);
    testingTabs.put(tabName, tdt);
    System.out.println("Subsystem " + tabName + " registered with TestingDashboard");
  }
    
  /*
   * This function registers a command with a tabName
   * and a command group in the command table on the testing
   * dashboard.
   */
  void registerCommand(String tabName, String cmdGrpName, Command command) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for command does not exist!");
      return;
    }
    System.out.println("Adding command " + command.toString());
    tab.commandTable.add(cmdGrpName, command);
  }

  void registerNumber(String tabName, String dataGrpName, String dataName, double defaultValue) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding data " + dataName);
    if (tab.dataTable.addName(dataGrpName, dataName))
      tab.dataTable.addDefaultNumberValue(dataName, defaultValue);
  }

  void registerBoolean(String tabName, String dataGrpName, String dataName, boolean defaultValue) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding data " + dataName);
    if (tab.dataTable.addName(dataGrpName, dataName))
      tab.dataTable.addDefaultBooleanValue(dataName, defaultValue);
  }

  void registerString(String tabName, String dataGrpName, String dataName, String defaultValue) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding String data " + dataName);
    if (tab.dataTable.addName(dataGrpName, dataName))
      tab.dataTable.addDefaultStringValue(dataName, defaultValue);
  }

  void registerSendable(String tabName, String dataGrpName, String dataName, Sendable sendable) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding Sendable data " + dataName);
    if (tab.dataTable.addName(dataGrpName, dataName))
      tab.dataTable.addDefaultSendableValue(dataName, sendable);
  }

   void updateNumber(String tabName, String dataName, double value) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    tab.dataTable.getEntry(dataName).setDouble(value);
  }

  void updateBoolean(String tabName, String dataName, boolean value) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    tab.dataTable.getEntry(dataName).setBoolean(value);
  }

  void updateString(String tabName, String dataName, String value) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    tab.dataTable.getEntry(dataName).setString(value);
  }

   double getNumber(String tabName, String dataName) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return 0;
    }
    if (initialized) {
      return tab.dataTable.getEntry(dataName).getDouble(0.0);
    } else {
      return tab.dataTable.getDefaultNumberValue(dataName);
    }
  }

  boolean getBoolean(String tabName, String dataName) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return false;
    }
    if (initialized) {
      return tab.dataTable.getEntry(dataName).getBoolean(false);
    } else {
      return tab.dataTable.getDefaultBooleanValue(dataName);
    }
  }

  String getString(String tabName, String dataName) {
    TestingDashboardTab tab = getTab(tabName);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return "";
    }
    if (initialized) {
      return tab.dataTable.getEntry(dataName).getString("");
    } else {
      return tab.dataTable.getDefaultStringValue(dataName);
    }
  }

  /**
   * 
   */
  public void createTestingDashboard() {
    System.out.println("Creating Testing Dashboard");
    for (String tabName : testingTabs.keySet()) {
      // Create Shuffleboard Tab
      TestingDashboardTab tdt = testingTabs.get(tabName);
      tdt.tab = Shuffleboard.getTab(tdt.tabName);
      // Add Command Groups and Commands
      Enumeration<String> cmdGrpNames = tdt.commandTable.getCommandGroups();
      Iterator<String> it = cmdGrpNames.asIterator();
      System.out.println("Created tab for " + tdt.tabName + " tabName");
      int colpos = 0; // columns in shuffleboard tab
      while (it.hasNext()) {
        String cmdGrpName = it.next();
        System.out.println("Creating \"" + cmdGrpName + "\" command group");
        ShuffleboardLayout layout = tdt.tab.getLayout(cmdGrpName, BuiltInLayouts.kList);
        layout.withPosition(colpos,0);
        layout.withSize(1, tdt.commandTable.getCommandList(cmdGrpName).size());
        for (Command cmd : tdt.commandTable.getCommandList(cmdGrpName)) {
          layout.add(cmd);
        }
        colpos++;
      }

      // Add Data Entries
      Enumeration<String> dataGrpNames = tdt.dataTable.getDataGroups();
      Iterator<String> itd = dataGrpNames.asIterator();
      while (itd.hasNext()) {
        String dataGrpName = itd.next();
        System.out.println("Creating \"" + dataGrpName + "\" data group");
        ArrayList<String> dataList = tdt.dataTable.getDataList(dataGrpName);
        Collections.sort(dataList);
        ShuffleboardLayout layout = tdt.tab.getLayout(dataGrpName, BuiltInLayouts.kList);
        layout.withPosition(colpos,0);
        layout.withSize(8,dataList.size());
        for (int j = 0; j < dataList.size(); j++) {
          String entryName = dataList.get(j);
          double defaultNumberValue = 0;
          String defaultStringValue = "";
          boolean defaultBooleanValue = false;
          Sendable sendable;
          GenericEntry entry;
          int type = tdt.dataTable.getType(entryName);
          switch (type) {
            case TestingDashboardDataTable.TYPE_NUMBER:
              defaultNumberValue = tdt.dataTable.getDefaultNumberValue(entryName);
              entry = layout.add(entryName, defaultNumberValue).getEntry();
              tdt.dataTable.addEntry(entryName, entry);
              break;
            case TestingDashboardDataTable.TYPE_STRING:
              defaultStringValue = tdt.dataTable.getDefaultStringValue(entryName);
              entry = layout.add(entryName, defaultStringValue).getEntry();
              tdt.dataTable.addEntry(entryName, entry);
              break;
            case TestingDashboardDataTable.TYPE_SENDABLE:
              sendable = tdt.dataTable.getDefaultSendableValue(entryName);
              layout.add(entryName, sendable);
              break;
            case TestingDashboardDataTable.TYPE_BOOLEAN:
              defaultBooleanValue = tdt.dataTable.getDefaultBooleanValue(entryName);
              entry = layout.add(entryName, defaultBooleanValue).getEntry();
              tdt.dataTable.addEntry(entryName, entry);
              break;
            default:
              System.out.println("ERROR: Type is " + type + " for data item \"" + entryName + "\"");
              break;
          }
        }
        colpos++;
      }

    }
    createDebugTab();
    initialized = true;
  }

  public void createDebugTab() {

  }
 
  public void updateDebugTab() {
  }
}
