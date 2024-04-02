// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;
import java.lang.reflect.Field;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Add your docs here. */
public class RobotMap {

  public static void init()
  {
    try {
      String home = java.lang.System.getenv("HOME");
      if (home == null || home.isEmpty()) {
        home = "/home/lvuser";
      }
      else {
        System.out.println("$HOME is " + home);
      }
      File f = new File(home + "/1100_Config");
      if (f.exists())
      {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(f);  
        Node root = doc.getFirstChild(); 
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); ++i)
        {
          String varName = "";
          String varValue = "";
          String varType = "";
          Node n = nodes.item(i);
          if (n.getNodeName() == "RobotMap")
          {
            NamedNodeMap nnm = n.getAttributes();
            for (int j = 0; j < nnm.getLength(); ++j)
            {
              Node config = nnm.item(j);
              if (config.getNodeType() != Node.ATTRIBUTE_NODE)
              {
                continue;
              }
              String attrName = config.getNodeName();
              String attrVal = config.getNodeValue();
              if (attrName.equals("VarName"))
              {
                varName = attrVal;
              }
              else if (attrName.equals("Value"))
              {
                varValue = attrVal;
              }
              else if (attrName.equals("Type"))
              {
                varType = attrVal;
              }
            }
          }
          if (!varName.isEmpty() && !varValue.isEmpty() && !varType.isEmpty())
          {
            Field field = RobotMap.class.getField(varName);
            if (varType.equals("double"))
            {
              System.out.println("Configuring " + varName + " to (double)" + varValue);
              field.setDouble(null, Double.parseDouble(varValue));
            }
            else if (varType.equals("int"))
            {
              System.out.println("Configuring " + varName + " to (int)" + varValue);
              field.setInt(null, Integer.parseInt(varValue));
            }
            else if (varType.equals("boolean"))
            {
              System.out.println("Configuring " + varName + " to (boolean)" + varValue);
              field.setBoolean(null, Boolean.parseBoolean(varValue));             
            }
          }
        }
      }  
      else
      {
        System.out.println("No configuration present, using defaults");
      }          
    } catch (Exception e) {
      System.err.println("exception from RobotMap.init():" + e.toString());
      // can't read the config. Carry on.
    }
  }
  // [R]obot geometry
  public static double R_TRACK_WIDTH_INCHES = 22.5;
  public static double R_WHEEL_BASE_INCHES  = 22.5;
  public static double R_BASE_RADIUS_INCHES = 11.25 * Math.sqrt(2);
  
  // [A]mp Add-On
  public static boolean A_ENABLED = true;
  public static boolean A_PIVOT_ENABLED = true;
  public static int A_MOTOR = RoboRioMap.CAN_17;
  public static int A_PIVOT_MOTOR = RoboRioMap.CAN_16;
  public static int A_NOTE_SENSOR = RoboRioMap.DIO_3;

  // [B]arrel 
  public static boolean B_ENABLED = true;
  public static int B_MOTOR = RoboRioMap.CAN_15;
  public static int B_NOTE_SENSOR = RoboRioMap.DIO_1;

  // [B]arrel [P]ivot
  public static boolean BP_ENABLED = true;
  public static int BP_MOTOR_LEFT = RoboRioMap.CAN_13;
  public static int BP_MOTOR_RIGHT = RoboRioMap.CAN_14;

  //[D]rive
  public static int D_FRONT_LEFT_DRIVE = RoboRioMap.CAN_18;
  public static int D_BACK_LEFT_DRIVE = RoboRioMap.CAN_5;
  public static int D_FRONT_RIGHT_DRIVE = RoboRioMap.CAN_3;
  public static int D_BACK_RIGHT_DRIVE = RoboRioMap.CAN_7;

  public static int D_FRONT_LEFT_TURNING = RoboRioMap.CAN_2;
  public static int D_BACK_LEFT_TURNING = RoboRioMap.CAN_6;
  public static int D_FRONT_RIGHT_TURNING = RoboRioMap.CAN_4;
  public static int D_BACK_RIGHT_TURNING = RoboRioMap.CAN_8;

  //[E]levator
  // TODO: determine CAN IDs
  public static boolean E_ELEVATOR_ENABLED = false;
  public static int E_MOTOR_LEFT = RoboRioMap.CAN_20;
  public static int E_MOTOR_RIGHT = RoboRioMap.CAN_19;

  //[I]ntake
  public static boolean I_INTAKE_ENABLED = true;
  public static int I_MOTOR_LEFT = RoboRioMap.CAN_10;
  public static int I_MOTOR_RIGHT = RoboRioMap.CAN_9;
  public static int I_NOTE_SENSOR = RoboRioMap.DIO_0;

  // [L]ights
  public static int L_LEDS = RoboRioMap.PWM_0;

  // [S]hooter
  public static boolean S_SHOOTER_ENABLED = true;
  public static int S_MOTOR_LEFT = RoboRioMap.CAN_11;
  public static int S_MOTOR_RIGHT = RoboRioMap.CAN_12;
  public static int S_NOTE_SENSOR = RoboRioMap.DIO_2;

  //[U]ser Input
	public static int U_DRIVER_XBOX_CONTROLLER = 0;
	public static int U_OPERATOR_XBOX_CONTROLLER = 1;

  //[V]ision
  public static boolean V_ENABLED = true;
}
