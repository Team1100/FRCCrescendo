// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;

public class FieldUtils{
    private static FieldUtils m_fieldUtils;

    public static final AllianceAprilTags RedTags = new AllianceAprilTags(4, 3, 5, 10, 9, 13, 11, 12);
    public static final AllianceAprilTags BlueTags = new AllianceAprilTags(7, 8, 6, 1, 2, 14, 16, 15);

    public static FieldUtils getInstance(){
        if(m_fieldUtils == null){
            m_fieldUtils = new FieldUtils();
        }
        return m_fieldUtils;
    }

    private FieldUtils(){}

    public AllianceAprilTags getAllianceAprilTags(){
        AllianceAprilTags tags = BlueTags;
        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        if(alliance.isPresent()){
            if(alliance.get() == DriverStation.Alliance.Red){
                tags = RedTags;
            } else if(alliance.get() == DriverStation.Alliance.Blue) {
                tags = BlueTags;
            }
        }
        return tags;
    }

    public Pose3d getSpeakerPose(){
        var aprilTags = getAllianceAprilTags();
        if(aprilTags == null){
            aprilTags = BlueTags;
        }

        return Constants.kTagLayout.getTagPose(aprilTags.speakerMiddle).get();
    }

    public Pose3d getAmpPose(){
        var aprilTags = getAllianceAprilTags();
        if(aprilTags == null){
            aprilTags = BlueTags;
        }

        return Constants.kTagLayout.getTagPose(aprilTags.amp).get();
    }

    public Rotation2d getRotationOffset() {
        Rotation2d offset = new Rotation2d();//returns no offset by default
        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        if(alliance.isPresent() && 
            alliance.get() == DriverStation.Alliance.Red){
                offset = new Rotation2d(Math.PI);
        }
        return offset;
    }

    public Rotation2d getAngleToPose(Pose2d currentPose, Pose2d targetPose) {
        Translation2d curTrans = currentPose.getTranslation();
        Translation2d targetTrans = targetPose.getTranslation();
        Translation2d toTarget = targetTrans.minus(curTrans);
        return toTarget.getAngle();
    }

    //Robot poses on field for driving
    public Pose2d getSource1Pose() {
        if(DriverStation.getAlliance().isPresent() &&
         (DriverStation.getAlliance().get() == DriverStation.Alliance.Red)) {
            return Constants.kSource1RedPose;
         } else {
            return Constants.kSource1BluePose;
         }
    }

    public Pose2d getSource3Pose() {
        if(DriverStation.getAlliance().isPresent() &&
         (DriverStation.getAlliance().get() == DriverStation.Alliance.Red)) {
            return Constants.kSource3RedPose;
         } else {
            return Constants.kSource3BluePose;
         }
    }

    public Pose2d getAmpScorePose() {
        if(DriverStation.getAlliance().isPresent() &&
          (DriverStation.getAlliance().get() == DriverStation.Alliance.Red)) {
            return Constants.kAmpScoreRedPose;
        } else {
            return Constants.kAmpScoreBluePose;
        }
    }

    public Pose2d getSpeakerScorePose() {
        if(DriverStation.getAlliance().isPresent() &&
          (DriverStation.getAlliance().get() == DriverStation.Alliance.Red)) {
            return Constants.kSpeakerScoreRedPose;
        } else {
            return Constants.kSpeakerScoreBluePose;
        }
    }
}