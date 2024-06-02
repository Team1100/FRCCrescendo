// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Optional;

import frc.robot.Constants;
import frc.robot.RobotMap;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDBoolean;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.vision.VisionConfig;
import frc.robot.utils.vision.VisionEstimationResult;
import frc.robot.utils.vision.VisionSystem;

public class Vision extends SubsystemBase {

  private static Vision m_vision;
  private ArrayList<VisionSystem> m_visionSystems;

  private TDNumber m_estX;
  private TDNumber m_estY;
  private TDNumber m_estRot;
  private TDBoolean m_poseUpdatesEnabled;

  /** Creates a new Vision. */
  private Vision() {
    super("Vision");
    m_visionSystems = new ArrayList<VisionSystem>();
    if(RobotMap.V_ENABLED){
      m_visionSystems.ensureCapacity(Constants.kVisionSystems.length);
      for(VisionConfig config : Constants.kVisionSystems) {
        VisionSystem system = new VisionSystem(config);
        m_visionSystems.add(system);
      }

      m_estX = new TDNumber(this, "Est Pose", "Est X");
      m_estY = new TDNumber(this, "Est Pose", "Est Y");
      m_estRot = new TDNumber(this, "Est Pose", "Est Rot");

      m_poseUpdatesEnabled = new TDBoolean(this, "", "Pose Updates Enabled");
    }
  }

  public static Vision getInstance(){
    if(m_vision == null){
      m_vision = new Vision();
    }
    return m_vision;
  }

  public void enablePoseUpdates() {
    m_poseUpdatesEnabled.set(true);
  }

  public void disablePoseUpdates() {
    m_poseUpdatesEnabled.set(false);
  }

  public boolean getPoseUpdatesEnabled() {
    return m_poseUpdatesEnabled.get();
  }

  @Override
  public void periodic() {
    if (RobotMap.V_ENABLED) {
      if(getPoseUpdatesEnabled()){
        Drive robotDrive = Drive.getInstance();

        var newest = getEstimatedGlobalPose();
        newest.ifPresent(
          est -> {
            Pose2d estPose = est.estimatedPose.toPose2d();

            robotDrive.addVisionMeasurement(estPose, est.timestamp, est.stdDevs);

            m_estX.set(estPose.getX());
            m_estY.set(estPose.getY());
            m_estRot.set(estPose.getRotation().getDegrees());
          }
        );
      }
      super.periodic();
    }
  }

  public Optional<VisionEstimationResult> getEstimatedGlobalPose() {
    Optional<VisionEstimationResult> estimate = Optional.empty();
    double lowestAmb = Double.MAX_VALUE;
    for(var system : m_visionSystems) {
      Optional<VisionEstimationResult> sysEst = system.getEstimatedPose();
      if(sysEst.isPresent() && (sysEst.get().ambiguity < lowestAmb)) {
        estimate = sysEst;
        lowestAmb = sysEst.get().ambiguity;
      }
    }
    return estimate;
  }
}