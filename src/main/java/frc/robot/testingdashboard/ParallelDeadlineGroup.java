// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

import com.pathplanner.lib.auto.NamedCommands;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ParallelDeadlineGroup extends edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup {

  public ParallelDeadlineGroup(SubsystemBase subsystem,
                               String groupName,
                               String name,
                               boolean registerNamedCommand,
                               edu.wpi.first.wpilibj2.command.Command deadline,
                               edu.wpi.first.wpilibj2.command.Command... otherCommands) {
    super(deadline, otherCommands);
    setName(name);
    TestingDashboard.getInstance().registerCommand(subsystem.getName(), groupName, this);
    if (registerNamedCommand) {
      NamedCommands.registerCommand(name, this);
    }
  }

  public ParallelDeadlineGroup(SubsystemBase subsystem, String groupName, String name, 
                               edu.wpi.first.wpilibj2.command.Command deadline,
                               edu.wpi.first.wpilibj2.command.Command... otherCommands) {
    this(subsystem, groupName, name, true, deadline, otherCommands);
  }
}