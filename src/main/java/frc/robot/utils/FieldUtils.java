// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;

public class FieldUtils{
    private static FieldUtils m_fieldUtils;
    private DriverStation.Alliance m_Alliance;
    private AllianceAprilTags m_aprilTags;

    public static final AllianceAprilTags RedTags = new AllianceAprilTags(4, 3, 5, 10, 9, 13, 11, 12);
    public static final AllianceAprilTags BlueTags = new AllianceAprilTags(7, 8, 6, 1, 2, 14, 16, 15);

    public static FieldUtils getInstance(){
        if(m_fieldUtils == null){
            m_fieldUtils = new FieldUtils();
        }
        return m_fieldUtils;
    }

    private FieldUtils(){
        //Get alliance from driverstation
        DriverStation.getAlliance().ifPresent(
                alliance -> {
                    m_Alliance = alliance;
                    setAprilTags();
                }
        );
    }

    public AllianceAprilTags getAllianceAprilTags(){
        if(m_aprilTags == null && m_Alliance != null)
        {
            setAprilTags();
        }
        return m_aprilTags;
    }

    public void setAlliance(DriverStation.Alliance alliance){
        m_Alliance = alliance;
    }

    public void setAprilTags(){
        if(m_Alliance == DriverStation.Alliance.Red){
            m_aprilTags = RedTags;
        }
        else if(m_Alliance == DriverStation.Alliance.Blue){
            m_aprilTags = BlueTags;
        }
        else{
            System.out.println("Unrecognized Alliance Color");
        }
    }

    public DriverStation.Alliance getAlliance(){
        return m_Alliance;
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
        if(m_Alliance == DriverStation.Alliance.Red){
            return new Rotation2d(Math.PI);
        } else {
            return new Rotation2d();
        }
    }
}