// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.testingdashboard.TDSendable;
import frc.robot.utils.SwerveDriveInputs;

public class SwerveDrive extends Command {
  private SwerveDriveInputs m_DriveInputs;
  private double m_headingDegrees;
  private PIDController m_headingController;
  private TDNumber m_TDheading;
  Drive m_drive;

  /** Creates a new SwerveDrive. */
  public SwerveDrive(SwerveDriveInputs driveInputs) {
    super(Drive.getInstance(), "Basic", "SwerveDrive");
    m_drive = Drive.getInstance();
    m_DriveInputs = driveInputs;
    m_headingController = new PIDController(0.01, 0.0, 0.0);
    m_headingController.enableContinuousInput(-180, 180);
    new TDSendable(m_drive, "Swerve Drive", "Heading Controller", m_headingController);
    m_TDheading = new TDNumber(m_drive, "Swerve Drive", "Target Heading", 0);
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    var rotationPower = -MathUtil.applyDeadband(m_DriveInputs.getRotation(), Constants.kDriveDeadband);
    if (rotationPower == 0) {
      rotationPower = m_headingController.calculate(m_drive.getHeading(), m_TDheading.get());
    }
    else {
      m_headingDegrees = m_drive.getHeading();
      m_TDheading.set(m_headingDegrees);
    }
    m_drive.drive(
      -MathUtil.applyDeadband(m_DriveInputs.getX(), Constants.kDriveDeadband),
      -MathUtil.applyDeadband(m_DriveInputs.getY(), Constants.kDriveDeadband),
      rotationPower,
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
