// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import java.util.function.Supplier;
import com.pathplanner.lib.commands.PathfindHolonomic;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;

public class DriveToPose extends Command {

  Supplier<Pose2d> m_targetPose;
  edu.wpi.first.wpilibj2.command.Command m_currentPathCommand;
  Pose2d m_currentTarget;
  Drive m_Drive;

  /** Creates a new DriveToPose. */
  public DriveToPose(Supplier<Pose2d> poseSupplier) {
    super (Drive.getInstance(), "Drive", "Drive To Pose");
    m_Drive = Drive.getInstance();
    m_targetPose = poseSupplier;
  
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Drive.getInstance());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    var  targetPose = m_targetPose.get();
    if (m_currentPathCommand == null || m_currentTarget == null || !m_currentTarget.equals(targetPose))
    {
      m_currentPathCommand = new PathfindHolonomic(targetPose,
                                                   new PathConstraints(10.0, 10.0, 10.0, 10.0),
                                                   m_Drive::getPose,
                                                   m_Drive::getMeasuredSpeeds,
                                                   m_Drive::drive,
                                                   Constants.kPathFollowerConfig,
                                                   m_Drive);
      m_currentPathCommand.initialize();
      m_currentTarget = targetPose;
    }
    m_currentPathCommand.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_currentPathCommand != null) {
      m_currentPathCommand.end(interrupted);
      m_currentPathCommand = null;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_currentPathCommand != null) {
      return m_currentPathCommand.isFinished();
    }
    return true;
  }
}
