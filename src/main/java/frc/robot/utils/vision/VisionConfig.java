// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.vision;

import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.math.geometry.Transform3d;

/** Add your docs here. */
public class VisionConfig 
{
    public String cameraName;
    public Transform3d cameraPosition;
    public PoseStrategy primaryStrategy;
    public PoseStrategy fallBackStrategy;

    public VisionConfig(String name, Transform3d camPosition, PoseStrategy primPoseStrategy, PoseStrategy fallbackPoseStrategy) {
        this.cameraName = name;
        this.cameraPosition = camPosition;
        this.primaryStrategy = primPoseStrategy;
        this.fallBackStrategy = fallbackPoseStrategy;
    }
}
