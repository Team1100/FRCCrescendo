/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.testingdashboard;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;

/**
 * TestingDashboardDataTable is a map of data groups to
 * sensor/testing values that can be mapped to a shuffleboard
 * tab.
 */
public class TestingDashboardDataTable {
  public static final int TYPE_NUMBER = 0;
  public static final int TYPE_STRING = 1;
  public static final int TYPE_SENDABLE = 2;
  public static final int TYPE_BOOLEAN = 3;
  Hashtable<String, ArrayList<String>> table;
  ArrayList<String> names;
  Hashtable<String, Integer> type;
  Hashtable<String, GenericEntry> entries;
  Hashtable<String, String> defaultString;
  Hashtable<String, Double> defaultDouble;
  Hashtable<String, Boolean> defaultBoolean;
  Hashtable<String, Object> defaultSendable;
  public TestingDashboardDataTable() {
    table = new Hashtable<String, ArrayList<String>>();
    names = new ArrayList<String>();
    entries = new Hashtable<String, GenericEntry>();
    type = new Hashtable<String, Integer>();
    defaultString = new Hashtable<String, String>();
    defaultDouble = new Hashtable<String, Double>();
    defaultBoolean = new Hashtable<String, Boolean>();
    defaultSendable = new Hashtable<String, Object>();
  }

  public boolean addName(String grp, String name) {
    if (table.containsKey(grp)) {
      ArrayList<String> l = table.get(grp);
      if (!l.contains(name)) {
        l.add(name);
      }
      else {
        return false;
      }
    } else {
      ArrayList<String> l = new ArrayList<String>();
      l.add(name);
      table.put(grp, l);
    }
    names.add(name);
    return true;
  }
  
  public int getType(String name) {
      return type.get(name).intValue();
  }

  public void addDefaultStringValue(String name, String value ) {
    if (names.contains(name)) {
        type.put(name,TYPE_STRING);
        defaultString.put(name,value);
    }
  }

  public void addDefaultNumberValue(String name, double value) {
    if (names.contains(name)) {
        type.put(name,Integer.valueOf(TYPE_NUMBER));
        defaultDouble.put(name,Double.valueOf(value));
    }
  }

  public void addDefaultBooleanValue(String name, boolean value) {
    if (names.contains(name)) {
        type.put(name,Integer.valueOf(TYPE_BOOLEAN));
        defaultBoolean.put(name,Boolean.valueOf(value));
    }
  }
  
  public void addDefaultSendableValue(String name, Sendable sendable) {
    if (names.contains(name)) {
        type.put(name,TYPE_SENDABLE);
        defaultSendable.put(name, sendable);
    }
  }

  public String getDefaultStringValue(String name) {
    return defaultString.get(name);
  }

  public double getDefaultNumberValue(String name) {
    return defaultDouble.get(name).doubleValue();
  }

  public boolean getDefaultBooleanValue(String name) {
    return defaultBoolean.get(name).booleanValue();
  }

  public Sendable getDefaultSendableValue(String name) {
    return (Sendable) defaultSendable.get(name);
  }

  /*
   *  This function adds the GenericEntry for a given
   *  named data item.
   */
  public void addEntry(String name, GenericEntry entry) {
      if (names.contains(name)) {
          entries.put(name,entry);
      }
  }

  public GenericEntry getEntry(String str) {
      return entries.get(str);
  }

  public Enumeration<String> getDataGroups() {
    return table.keys();
  }

  public ArrayList<String> getDataList(String dataGrgName) {
    return table.get(dataGrgName);
  }
}
