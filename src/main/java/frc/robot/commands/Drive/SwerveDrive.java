// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;
import frc.robot.utils.SwerveDriveInputs;

public class SwerveDrive extends Command {
  private SwerveDriveInputs m_DriveInputs;
  Drive m_drive;

  /** Creates a new SwerveDrive. */
  public SwerveDrive(SwerveDriveInputs driveInputs) {
    super(Drive.getInstance(), "Basic", "SwerveDrive");
    m_drive = Drive.getInstance();
    m_DriveInputs = driveInputs;

    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.drive(
      -MathUtil.applyDeadband(m_DriveInputs.getX(), Constants.kDriveDeadband),
      -MathUtil.applyDeadband(m_DriveInputs.getY(), Constants.kDriveDeadband),
      -MathUtil.applyDeadband(m_DriveInputs.getRotation(), Constants.kDriveDeadband),
      true, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
