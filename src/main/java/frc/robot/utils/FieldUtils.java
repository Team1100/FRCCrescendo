// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
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
        AllianceAprilTags tags = null;
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
            throw new RuntimeException("Attempted to get alliance specific info without a valid alliance");
        }

        return Constants.kTagLayout.getTagPose(aprilTags.speakerMiddle).get();
    }

    public Pose3d getAmpPose(){
        var aprilTags = getAllianceAprilTags();
        if(aprilTags == null){
            throw new RuntimeException("Attempted to get alliance specific info without a valid alliance");
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
}