// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

import com.pathplanner.lib.auto.NamedCommands;

public class Command extends edu.wpi.first.wpilibj2.command.Command {
  /** Creates a new Command. */
  public Command(SubsystemBase subsystem, String groupName, String name)
  {
    this(subsystem, groupName, name, true);
  }
  
  public Command(SubsystemBase subsystem, String groupName, String name, boolean registerNamedCommand) {
    setName(name);
    TestingDashboard.getInstance().registerCommand(subsystem.getName(), groupName, this);
    if (registerNamedCommand) {
      NamedCommands.registerCommand(name, this);
    }
  }
}
