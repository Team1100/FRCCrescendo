/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.testingdashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * This class sets up a testing dashboard using
 * WPILib's ShuffleBoard. The testing dashboard
 * contains a single tab for every subsystem
 * containing the subsystem status and all
 * commands associated with that subsystem.
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
  private ArrayList<TestingDashboardTab> testingTabs;
  boolean initialized = false;
    
  private TestingDashboard() {
    testingTabs = new ArrayList<TestingDashboardTab>();
    initialized = false;
  }

  public static TestingDashboard getInstance() {
    if (testingDashboard == null) {
      testingDashboard = new TestingDashboard();
    }
    return testingDashboard;
  }

  private boolean hasSubsystem(SubsystemBase subsystem) {
    for (int i = 0; i < testingTabs.size(); i++) {
      TestingDashboardTab tab = testingTabs.get(i);
      if (tab.subsystem == subsystem) {
        return true;
      }
    }
    return false;
  }

  private TestingDashboardTab getSubsystemTab(SubsystemBase subsystem) {
    for (int i = 0; i < testingTabs.size(); i++) {
      TestingDashboardTab tab = testingTabs.get(i);
      if (tab.subsystem == subsystem) {
        return tab;
      }
    }
    return null;
  }

  /*
   * This function registers a subsystem with
   * the testing dashboard.
   */
  public void registerSubsystem(SubsystemBase subsystem, String name) {
    if (hasSubsystem(subsystem)) {
      // Subsystem has already been registered
      return;
    }
    TestingDashboardTab tdt = new TestingDashboardTab();
    tdt.subsystem = subsystem;
    tdt.subsystemName = name;
    tdt.commandTable = new TestingDashboardCommandTable();
    tdt.dataTable = new TestingDashboardDataTable();
    testingTabs.add(tdt);
    System.out.println("Subsystem " + name + " registered with TestingDashboard");
  }
    
  /*
   * This function registers a command with a subsystem
   * and a command group in the command table on the testing
   * dashboard.
   */
  public void registerCommand(SubsystemBase subsystem, String cmdGrpName, Command command) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for command does not exist!");
      return;
    }
    System.out.println("Adding command " + command.toString());
    tab.commandTable.add(cmdGrpName, command);
  }

  public void registerNumber(SubsystemBase subsystem, String dataGrpName, String dataName, double defaultValue) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding data " + dataName);
    tab.dataTable.addName(dataGrpName, dataName);
    tab.dataTable.addDefaultNumberValue(dataName, defaultValue);
  }

  public void registerString(SubsystemBase subsystem, String dataGrpName, String dataName, String defaultValue) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding String data " + dataName);
    tab.dataTable.addName(dataGrpName, dataName);
    tab.dataTable.addDefaultStringValue(dataName, defaultValue);
  }

  public void registerSendable(SubsystemBase subsystem, String dataGrpName, String dataName, Sendable sendable) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    System.out.println("Adding String data " + dataName);
    tab.dataTable.addName(dataGrpName, dataName);
    tab.dataTable.addDefaultSendableValue(dataName, sendable);
  }

  public void updateNumber(SubsystemBase subsystem, String dataName, double value) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    tab.dataTable.getEntry(dataName).setDouble(value);
  }

  public void updateString(SubsystemBase subsystem, String dataName, String value) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
    if (tab == null) {
      System.out.println("WARNING: Subsystem for data does not exist!");
      return;
    }
    tab.dataTable.getEntry(dataName).setString(value);
  }

  public double getNumber(SubsystemBase subsystem, String dataName) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
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

  public String getString(SubsystemBase subsystem, String dataName) {
    TestingDashboardTab tab = getSubsystemTab(subsystem);
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
    for (int i = 0; i < testingTabs.size(); i++) {
      // Create Shuffleboard Tab
      TestingDashboardTab tdt = testingTabs.get(i);
      tdt.tab = Shuffleboard.getTab(tdt.subsystemName);
      // Add Command Groups and Commands
      Enumeration<String> cmdGrpNames = tdt.commandTable.getCommandGroups();
      Iterator<String> it = cmdGrpNames.asIterator();
      System.out.println("Created tab for " + tdt.subsystemName + " subsystem");
      int colpos = 0; // columns in shuffleboard tab
      while (it.hasNext()) {
        String cmdGrpName = it.next();
        System.out.println("Creating \"" + cmdGrpName + "\" command group");
        ArrayList<Command> cmdList = tdt.commandTable.getCommandList(cmdGrpName);
        ShuffleboardLayout layout = tdt.tab.getLayout(cmdGrpName, BuiltInLayouts.kList);
        layout.withPosition(colpos,0);
        layout.withSize(1,cmdList.size());
        for (int j = 0; j < cmdList.size(); j++) {
          layout.add(cmdList.get(j));
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
            default:
              System.out.println("ERROR: Type is " + type + "for data item \"" + entryName);
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
