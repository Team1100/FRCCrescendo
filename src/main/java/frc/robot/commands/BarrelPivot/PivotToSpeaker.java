// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.subsystems.Drive;
import frc.robot.testingdashboard.Command;
import frc.robot.utils.FieldUtils;

public class PivotToSpeaker extends Command {
  protected BarrelPivot m_barrelPivot;

  public PivotToSpeaker() {
    this("Auto Commands", "PivotToSpeaker");
  }

  //Used for subclasses so they can set they're own testing dashboard name
  protected PivotToSpeaker(String groupName, String name){
    super(BarrelPivot.getInstance(), "Auto Pivot", "PivotToSpeaker");
    m_barrelPivot = BarrelPivot.getInstance();
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // get speaker pose as topdown vector2 (x, y, z) > (x,y), get height as tag pose Z + constant offset - barrel pivot vertical offset
    Pose3d speakerPose = FieldUtils.getInstance().getSpeakerPose();
    Translation2d speakerTrans = new Translation2d(speakerPose.getX(), speakerPose.getY());
    double speakerHeight = speakerPose.getZ() + Constants.SPEAKER_HEIGHT_OFFSET - Constants.kRobotToBarrel.getZ();

    // get shooter pivot point as topdown vector2 (x, y) via offsetting drive pose by constant barrel pivot offset, rotated by drive rotation
    Pose2d botPose = Drive.getInstance().getPose();
    Translation2d botTrans = new Translation2d(botPose.getX(), botPose.getY());
    Translation2d pivotOffset = new Translation2d(Constants.kRobotToBarrel.getX(), Constants.kRobotToBarrel.getY());
    pivotOffset = pivotOffset.rotateBy(botPose.getRotation());
    Translation2d pivotTrans = botTrans.minus(pivotOffset);

    // get distance between speaker and pivot point
    double distToTag = speakerTrans.getDistance(pivotTrans);

    // get desired shooter rotation via tan of triangle 
    // equivalent to arctan( speakerHeight / distToTag )
    Rotation2d rotation = new Rotation2d(distToTag, speakerHeight);
    
    m_barrelPivot.setTargetAngle(rotation.getDegrees());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_barrelPivot.stopPivot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
