// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.FieldUtils;


public class TurnToTarget extends TurnToRotation {
  private Pose2d m_target;

  TDNumber m_targetX;
  TDNumber m_targetY;

  public TurnToTarget(Pose2d target) {
    super(()->new Rotation2d(), "Auto Commands", "TurnToTarget");
    m_target = target;

    m_targetX = new TDNumber(m_drive, "Test Inputs", "TargetPoseX");
    m_targetY = new TDNumber(m_drive, "Test Inputs", "TargetPoseY");

    m_targetSupplier = ()->{
      Pose2d currentPose = m_drive.getPose();
      m_targetX.set(m_target.getX());
      m_targetY.set(m_target.getY());
      return FieldUtils.getInstance().getAngleToPose(currentPose, m_target);
    };
  }
}
