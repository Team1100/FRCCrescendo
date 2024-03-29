// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.vision;

import edu.wpi.first.math.geometry.Pose3d;

/** Add your docs here. */
public class VisionEstimationResult 
{
    public Pose3d estimatedPose;
    public double ambiguity;
    public double timestamp;

    public VisionEstimationResult(Pose3d poseEstimate, double timestamp, double ambiguity) {
        this.estimatedPose = poseEstimate;
        this.timestamp = timestamp;
        this.ambiguity = ambiguity;
    }
}
