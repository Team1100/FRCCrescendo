// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.vision;

import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import frc.robot.Constants;

/** Add your docs here. */
public class VisionSystem {

    private PhotonCamera m_camera;
    private PhotonPoseEstimator m_photonEstimator;
    private EstimatedRobotPose m_lastResult;
    private double m_lastEstTime;

    public VisionSystem(VisionConfig config) {
        m_camera = new PhotonCamera(config.cameraName);
        m_photonEstimator = new PhotonPoseEstimator(Constants.kTagLayout, config.primaryStrategy, m_camera, config.cameraPosition);
        if((config.primaryStrategy == PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR) ||
            (config.primaryStrategy == PoseStrategy.MULTI_TAG_PNP_ON_RIO)) {
            m_photonEstimator.setMultiTagFallbackStrategy(config.fallBackStrategy);
        }
    }

    public PhotonPipelineResult getLatestResult() {
        if(m_camera != null){
        return m_camera.getLatestResult();
        } else {
        return new PhotonPipelineResult();
        }
    }

    public Optional<VisionEstimationResult> getEstimatedPose() {
        if(m_photonEstimator != null && m_camera != null){
            var visionEst = m_photonEstimator.update();
            PhotonPipelineResult latestResult = m_camera.getLatestResult();

            if(visionEst.isPresent()) {
                EstimatedRobotPose est = visionEst.get();
                double ambiguity = getResultAmbiguity(est, latestResult);
                double latestTimestamp = latestResult.getTimestampSeconds();

                boolean valid = validateResult(est, ambiguity);
            
                boolean newResult = Math.abs(latestTimestamp - m_lastEstTime) > 1e-5;
                if (newResult) {
                    m_lastEstTime = latestTimestamp;
                    m_lastResult = est;
                }

                if(valid) {
                    Matrix<N3,N1> stdDevs = getEstimationStdDevs(est.estimatedPose.toPose2d());
                    return Optional.of(new VisionEstimationResult(est.estimatedPose, latestTimestamp, ambiguity, stdDevs));
                }
            }
        }
        return Optional.empty();
    }

    public Matrix<N3, N1> getEstimationStdDevs(Pose2d estimatedPose) {
        var estStdDevs = Constants.kSingleTagStdDevs;

        if(m_photonEstimator != null){
          var targets = getLatestResult().getTargets();
          int numTags = targets.size();
          double avgDist = 0;
          for (var tgt : targets) {
              var tagPose = m_photonEstimator.getFieldTags().getTagPose(tgt.getFiducialId());
              if (tagPose.isEmpty()) continue;
              numTags++;
              avgDist +=
                      tagPose.get().toPose2d().getTranslation().getDistance(estimatedPose.getTranslation());
          }
          if (numTags == 0) { return estStdDevs; }
          avgDist /= numTags;
          // Decrease std devs if multiple targets are visible
          if (numTags > 1) { estStdDevs = Constants.kMultiTagStdDevs; }
          // Increase std devs based on (average) distance
          if (numTags == 1 && avgDist > 4) {
              estStdDevs = VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
          } else {
            estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30));
          }
        }

        return estStdDevs;
    }

    // !! Currently only works for Multitag on Coproceesor, Update later to handle other stratgies !!
    public double getResultAmbiguity(EstimatedRobotPose estPose, PhotonPipelineResult latestResult) {
            double ambiguity = Double.MAX_VALUE;
            switch (estPose.strategy) {
                case MULTI_TAG_PNP_ON_COPROCESSOR:
                    ambiguity = latestResult.getMultiTagResult().estimatedPose.ambiguity;
                    break;

                case LOWEST_AMBIGUITY:
                    var targets = estPose.targetsUsed;
                    for(PhotonTrackedTarget target : targets) {
                        if(target.getPoseAmbiguity() < ambiguity) {
                            ambiguity = target.getPoseAmbiguity();
                        }
                    }
                    break;
            
                default:
                    System.out.println("Unexpected Strategy Used For Pose Estimation. Returning Max Value of Double as Ambiguity");
                    break;
            }
            return ambiguity;
    }

    private boolean validateResult(EstimatedRobotPose estPose, double ambiguity) {
        if(ambiguity > Constants.kMaxValidAmbiguity) {
            return false;
        }
        
        // double timeSinceLastEstimate = estPose.timestampSeconds - m_lastEstTime;
        // double maxPossibleMovement = timeSinceLastEstimate * Constants.kMaxSpeedMetersPerSecond;

        // double distanceFromLastEstimate = estPose.estimatedPose.getTranslation().getDistance(m_lastResult.estimatedPose.getTranslation());
        // if(distanceFromLastEstimate > maxPossibleMovement) {
        //     return false;
        // }

        return true;
    }

}
