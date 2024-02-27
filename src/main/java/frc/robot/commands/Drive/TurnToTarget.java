// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;


import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.utils.FieldUtils;


public class TurnToTarget extends TurnToRotation {
  private Supplier<Pose2d> m_targetPoseSupplier;

  public TurnToTarget(Supplier<Pose2d> targetSupplier) {
    super(()->new Rotation2d(), "Auto Commands", "TurnToTarget");
    m_targetPoseSupplier = targetSupplier;

    m_targetSupplier = ()->{
      Pose2d currentPose = m_drive.getPose();
      Pose2d target = m_targetPoseSupplier.get();
      return FieldUtils.getInstance().getAngleToPose(currentPose, target);
    };
  }
}
