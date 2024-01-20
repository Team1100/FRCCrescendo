// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class RobotMap {

    //[D]rive
    public static final int D_FRONT_LEFT_DRIVE = RoboRioMap.CAN_1;
    public static final int D_BACK_LEFT_DRIVE = RoboRioMap.CAN_5;
    public static final int D_FRONT_RIGHT_DRIVE = RoboRioMap.CAN_3;
    public static final int D_BACK_RIGHT_DRIVE = RoboRioMap.CAN_7;

    public static final int D_FRONT_LEFT_TURNING = RoboRioMap.CAN_2;
    public static final int D_BACK_LEFT_TURNING = RoboRioMap.CAN_6;
    public static final int D_FRONT_RIGHT_TURNING = RoboRioMap.CAN_4;
    public static final int D_BACK_RIGHT_TURNING = RoboRioMap.CAN_8;

    //[I]ntake
    public static final int I_MOTOR_LEFT = RoboRioMap.CAN_10;
    public static final int I_MOTOR_RIGHT = RoboRioMap.CAN_9;
    public static final double I_INTAKE_SPEED = 0.3;

    //[U]ser Input
	public static final int U_DRIVER_XBOX_CONTROLLER = 0;
	public static final int U_OPERATOR_XBOX_CONTROLLER = 1;
}
