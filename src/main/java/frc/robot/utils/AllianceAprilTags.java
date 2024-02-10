// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

/** Add your docs here. */
public class AllianceAprilTags {
    public int speakerMiddle;
    public int speakerSide;
    public int amp;
    public int sourceInner;
    public int sourceOuter;
    public int stageCenter;
    public int stageDown;
    public int stageUp;

    public AllianceAprilTags(
        int speakerMiddle,
        int speakerSide,
        int amp,
        int sourceInner,
        int sourceOuter,
        int stageCenter,
        int stageDown,
        int stageUp
    ){
        this.speakerMiddle = speakerMiddle;
        this.speakerSide = speakerSide;
        this.amp = amp;
        this.sourceInner = sourceInner;
        this.sourceOuter = sourceOuter;
        this.stageCenter = stageCenter;
        this.stageDown = stageDown;
        this.stageUp = stageUp;
    }
}
