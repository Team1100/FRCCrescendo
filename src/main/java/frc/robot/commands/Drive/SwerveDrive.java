// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;

public class SwerveDrive extends Command {
  private static XboxController m_driverController;
  Drive m_drive;

  /** Creates a new SwerveDrive. */
  public SwerveDrive() {
    super(Drive.getInstance(), "Basic", "SwerveDrive");
    m_drive = Drive.getInstance();
    m_driverController = OI.getInstance().getDriverXboxController();

    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.drive(
      -MathUtil.applyDeadband(m_driverController.getLeftY(), Constants.kDriveDeadband),
      -MathUtil.applyDeadband(m_driverController.getLeftX(), Constants.kDriveDeadband),
      -MathUtil.applyDeadband(m_driverController.getRightX(), Constants.kDriveDeadband),
      true, true);
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
