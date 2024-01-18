// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The RoboRioMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RoboRioMap {

  //List of all PWM ports
	public final static int PWM_0 = 0;
	public final static int PWM_1 = 1;
	public final static int PWM_2 = 2;
	public final static int PWM_3 = 3;
	public final static int PWM_4 = 4;
	public final static int PWM_5 = 5;
	public final static int PWM_6 = 6;
	public final static int PWM_7 = 7;
	public final static int PWM_8 = 8;
	public final static int PWM_9 = 9;
	// see https://www.pdocs.kauailabs.com/navx-mxp/installation/io-expansion/
	public final static int PWM_MXP_0 = 10;
	public final static int PWM_MXP_1 = 11;
	public final static int PWM_MXP_2 = 12;
	public final static int PWM_MXP_3 = 13;
	public final static int PWM_MXP_4 = 14;
	public final static int PWM_MXP_5 = 15;
	public final static int PWM_MXP_6 = 16;
	public final static int PWM_MXP_7 = 17;
	public final static int PWM_MXP_8 = 18;
	public final static int PWM_MXP_9 = 19;

	// List of Talon SRX CAN IDs
	public final static int SRX_CAN_0 = 0;
	public final static int SRX_CAN_1 = 1;
	public final static int SRX_CAN_2 = 2;
	public final static int SRX_CAN_3 = 3;

	// List of CAN IDs
	public final static int CAN_0 = 0; 
	public final static int CAN_1 = 1; 
	public final static int CAN_2 = 2; 
	public final static int CAN_3 = 3; 
	public final static int CAN_4 = 4; 
	public final static int CAN_5 = 5;
	public final static int CAN_6 = 6; 
	public final static int CAN_7 = 7; 
	public final static int CAN_8 = 8;
	public final static int CAN_9 = 9; 
	public final static int CAN_10 = 10;
	public final static int CAN_11 = 11;
	public final static int CAN_12 = 12; 
	public final static int CAN_13 = 13; 
	public final static int CAN_14 = 14; 
	public final static int CAN_15 = 15; 
	public final static int CAN_16 = 16;
	public final static int CAN_17 = 17;
	public final static int CAN_18 = 18; 
	public final static int CAN_19 = 19; 
	public final static int CAN_20 = 20; 
	public final static int CAN_21 = 21;
	public final static int CAN_22 = 22;
	public final static int CAN_23 = 23;
	public final static int CAN_24 = 24; 
	public final static int CAN_25 = 25; 
	public final static int CAN_26 = 26; 
	public final static int CAN_27 = 27; 
	public final static int CAN_28 = 28; 
	public final static int CAN_29 = 29; 
	public final static int CAN_30 = 30; 
	public final static int CAN_31 = 31; 
	public final static int CAN_32 = 32; 
	public final static int CAN_33 = 33; 
	public final static int CAN_34 = 34; 
	public final static int CAN_35 = 35; 
	public final static int CAN_36 = 36; 
	public final static int CAN_37 = 37; 
	public final static int CAN_38 = 38; 
	public final static int CAN_39 = 39; 
	public final static int CAN_40 = 40; 
	public final static int CAN_41 = 41; 
	public final static int CAN_42 = 42; 
	public final static int CAN_43 = 43; 
	public final static int CAN_44 = 44; 
	public final static int CAN_45 = 45; 
	public final static int CAN_46 = 46; 
	public final static int CAN_47 = 47; 
	public final static int CAN_48 = 48; 
	public final static int CAN_49 = 49; 
	public final static int CAN_50 = 50; 
    
	// List of PCM CAN IDs
	public final static int PCM_CAN = CAN_2;
	public final static int PCM_CAN_2 = CAN_18;

	//List of all analog ports
	public final static int ANALOG_0 = 0;
	public final static int ANALOG_1 = 1;
	public final static int ANALOG_2 = 2;
	public final static int ANALOG_3 = 3;
	// see https://www.pdocs.kauailabs.com/navx-mxp/installation/io-expansion/
	public final static int ANALOG_MXP_0 = 4;
	public final static int ANALOG_MXP_1 = 5;
	public final static int ANALOG_MXP_2 = 6;
	public final static int ANALOG_MXP_3 = 7;

	//List of all relays
	public final static int RELAY_0 = 0;
	public final static int RELAY_1 = 1;
	public final static int RELAY_2 = 2;
	public final static int RELAY_3 = 3;

	//List of all DIO ports
	public final static int DIO_0 = 0;
	public final static int DIO_1 = 1;
	public final static int DIO_2 = 2;
	public final static int DIO_3 = 3;
	public final static int DIO_4 = 4;
	public final static int DIO_5 = 5;
	public final static int DIO_6 = 6;
	public final static int DIO_7 = 7;
	public final static int DIO_8 = 8;
	public final static int DIO_9 = 9;

	// see https://www.pdocs.kauailabs.com/navx-mxp/installation/io-expansion/
	public final static int DIO_MXP_0 = 10;
	public final static int DIO_MXP_1 = 11;
	public final static int DIO_MXP_2 = 12;
	public final static int DIO_MXP_3 = 13;
	public final static int DIO_MXP_4 = 18;
	public final static int DIO_MXP_5 = 19;
	public final static int DIO_MXP_6 = 20;
	public final static int DIO_MXP_7 = 21;
	public final static int DIO_MXP_8 = 22;
	public final static int DIO_MXP_9 = 23;

	//List of all USB ports
	public static final int USB_0 = 0;
	public static final int USB_1 = 1;
	public static final int USB_2 = 2;
	public static final int USB_3 = 3;

	// Pneumatic Control Module (PCM) ports
	public static final int PCM_0 = 0;
	public static final int PCM_1 = 1;
	public static final int PCM_2 = 2;
	public static final int PCM_3 = 3;
	public static final int PCM_4 = 4;
	public static final int PCM_5 = 5;
	public static final int PCM_6 = 6;
	public static final int PCM_7 = 7;

	//List of all PDP ports
	public static final int PDP_0 = 0;
	public static final int PDP_1 = 1;
	public static final int PDP_2 = 2;
	public static final int PDP_3 = 3;
	public static final int PDP_4 = 4;
	public static final int PDP_5 = 5;
	public static final int PDP_6 = 6;
	public static final int PDP_7 = 7;
	public static final int PDP_8 = 8;
	public static final int PDP_9 = 9;
	public static final int PDP_10 = 10;
	public static final int PDP_11 = 11;
	public static final int PDP_12 = 12;
	public static final int PDP_13 = 13;
	public static final int PDP_14 = 14;
	public static final int PDP_15 = 15;
	public static final int PDP_16 = 16;

}