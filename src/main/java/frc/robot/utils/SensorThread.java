// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

/** Add your docs here. */
public class SensorThread extends Thread 
{
    private NoteProximitySensor[] m_Sensors;
    public SensorThread(NoteProximitySensor... sensors) {
        m_Sensors = sensors;
    }

    public void run(){
        try {
            while(true) {
                for (NoteProximitySensor sensor : m_Sensors) {
                    sensor.update();
                }
                sleep(1);
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Got unexpected exception in Note sensor thread. Exiting thread, sensor will no longer be updated");
        } catch (InterruptedException ex) {
            System.out.println("Note Sensor Thread was interrupted. Exiting thread, sensor will no longer be updated");
        }
    }
}
