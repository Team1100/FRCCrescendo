// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.FieldUtils;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.Drive;

public class TargetDrive extends Command {
    private XboxController m_driverController;
    private Drive m_drive;
    private Supplier<Pose2d> m_tgtSupplier;
    private ProfiledPIDController m_thetaController;

    TDNumber TDTargetAngle;

  /** Creates a new TargetDrive. */
  public TargetDrive(Supplier<Pose2d> targetSupplier) {
    super(Drive.getInstance(), "", "TargetDrive");
    m_driverController = OI.getInstance().getDriverXboxController();
    m_drive = Drive.getInstance();
    m_tgtSupplier = targetSupplier;

      m_thetaController = new ProfiledPIDController(
        Constants.kPThetaController, 0, Constants.kDThetaController, Constants.kThetaControllerConstraints);
    m_thetaController.enableContinuousInput(-Math.PI, Math.PI);

    m_thetaController.setTolerance(0.1);

    TDTargetAngle = new TDNumber(m_drive, "Test Outputs", "OutputAngle");

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Pose2d targetPose = m_tgtSupplier.get();
    Pose2d currentPose = m_drive.getPose();

    Rotation2d targetRot = FieldUtils.getInstance().getAngleToPose(currentPose, targetPose);

    TDTargetAngle.set(targetRot.getDegrees());

    double rotation = m_thetaController.calculate(
        MathUtil.angleModulus(currentPose.getRotation().getRadians()),
        MathUtil.angleModulus(targetRot.getRadians()));

    double xSpeed =  -MathUtil.applyDeadband(m_driverController.getLeftY(), Constants.kDriveDeadband) * Constants.kMaxSpeedMetersPerSecond;
    double ySpeed =  -MathUtil.applyDeadband(m_driverController.getLeftX(), Constants.kDriveDeadband) * Constants.kMaxSpeedMetersPerSecond;

    ChassisSpeeds outputSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotation, currentPose.getRotation());
    m_drive.drive(outputSpeeds);
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
