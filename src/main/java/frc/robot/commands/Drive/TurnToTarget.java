// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;


public class TurnToTarget extends TurnToRotation {
  private Pose2d m_target;

  public TurnToTarget(Pose2d target) {
    super(new Rotation2d(), "Auto Commands", "TurnToTarget");//Target Rotation will be reset on init
    m_target = target;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Pose2d currentPose = m_drive.getPose();
    Transform2d trsfrm = m_target.minus(currentPose);
    m_targetRot = new Rotation2d(trsfrm.getX(), trsfrm.getY());
  }
}
