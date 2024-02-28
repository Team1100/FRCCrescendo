// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDString;

/** Add your docs here. */
public class NoteProximitySensor extends Thread {
    private enum State {
        c_State_No_Note,
        c_State_First_Leg,
        c_State_Middle_Hole,
        c_State_Second_Leg
    };
    private DebouncedDioSensor m_sensor;
    private State m_state;

    private TDString m_tdState;

    public NoteProximitySensor(int dioPort, SubsystemBase subsystem) {
        m_sensor = new DebouncedDioSensor(dioPort, subsystem);
        m_state = State.c_State_No_Note;
        String sensorName = subsystem.getName() + "NoteSensor";
        m_tdState = new TDString(subsystem, "Sensors", sensorName);
    }

    public void run() {
        try {
            while(true) {
                sleep(1);
                update();
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Got unexpected exception in Note sensor thread. Exiting thread, sensor will no longer be updated");
        } catch (InterruptedException ex) {
            System.out.println("Note Sensor Thread was interrupted. Exiting thread, sensor will no longer be updated");
        }
    }

    public void update(){
        m_sensor.update();
        boolean val = m_sensor.get();
        State nextState = State.c_State_No_Note;
        synchronized(this) {
            switch (m_state) {
                case c_State_No_Note:
                    nextState = State.c_State_No_Note;
                    if(val){
                        nextState = State.c_State_First_Leg;
                    }
                    break;

                case c_State_First_Leg:
                    nextState = State.c_State_First_Leg;
                    if(!val){
                        nextState = State.c_State_Middle_Hole;
                    }
                    break;

                case c_State_Middle_Hole:
                    nextState = State.c_State_Middle_Hole;
                    if(val){
                        nextState = State.c_State_Second_Leg;
                    }
                    break;

                case c_State_Second_Leg:
                    nextState = State.c_State_Second_Leg;
                    if(!val){
                        nextState = State.c_State_No_Note;
                    }
                    break;

                default:
                    System.out.println("Note Proximity Sensor has invalid state");
                    nextState = State.c_State_No_Note;
                    break;
            }

            m_state = nextState;
        }
        updateTD();
    }

    public boolean hasNote() {
        return getSensorState() != State.c_State_No_Note;
    }

    public boolean noteIsCentered() { return getSensorState() == State.c_State_Middle_Hole; }

    public void reset() { setSensorState(State.c_State_No_Note); }

    private State getSensorState() {
        synchronized(this){return m_state;}
    }
    private void setSensorState(State newState) {
        synchronized(this){m_state = newState;}
    } 

    public void updateTD(){
        switch (getSensorState()) {
            case c_State_No_Note:
                m_tdState.set("No Note Detected");
                break;
            case c_State_First_Leg:
                m_tdState.set("HasNote:FirstLeg");
                break;
            case c_State_Middle_Hole:
                m_tdState.set("HasNote:CenterHole");
                break;
            case c_State_Second_Leg:
                m_tdState.set("HasNote:SecondLeg");
                break;
            default:
                m_tdState.set("Unknown State");
                break;
        }
    }
}
