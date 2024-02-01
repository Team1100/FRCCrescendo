// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;

public class TurnToRotation extends Command {
  protected Drive m_drive;
  protected Rotation2d m_targetRot;
  private ProfiledPIDController m_thetaController;

  public TurnToRotation(Rotation2d target) {
    this(target, "Auto Commands", "TurnToTarget");
  }

  //Used for subclasses so they can set they're own testing dashboard name
  protected TurnToRotation(Rotation2d target, String groupName, String name){
    super(Drive.getInstance(), groupName, name);
    m_drive = Drive.getInstance();
    m_targetRot = target;


    m_thetaController = new ProfiledPIDController(
        Constants.kPThetaController, 0, Constants.kDThetaController, Constants.kThetaControllerConstraints);
    m_thetaController.enableContinuousInput(-Math.PI, Math.PI);

    m_thetaController.setTolerance(0.1);
 
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Pose2d currentPose = m_drive.getPose();

    double rotation = m_thetaController.calculate(currentPose.getRotation().getRadians(), m_targetRot.getRadians());
    m_drive.drive(0,0, rotation, true, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_thetaController.atGoal() && (m_drive.getMeasuredSpeeds().omegaRadiansPerSecond < 0.001);
  }
}
