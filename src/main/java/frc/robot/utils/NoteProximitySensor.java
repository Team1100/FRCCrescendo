// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.testingdashboard.TDString;

/** Add your docs here. */
public class NoteProximitySensor {
    private enum State {
        c_State_No_Note,
        c_State_First_Leg,
        c_State_Middle_Hole,
        c_State_Second_Leg
    };
    private DigitalInput m_sensorDIO;
    private State m_state;

    private TDString m_tdState;
    private TDNumber m_testingState;

    public NoteProximitySensor(int dioPort, SubsystemBase subsystem) {
        m_sensorDIO = new DigitalInput(dioPort);
        m_state = State.c_State_No_Note;
        String sensorName = subsystem.getName() + "NoteSensor";
        m_tdState = new TDString(subsystem, "Sensors", sensorName);
        m_testingState = new TDNumber(subsystem, "Sensors", "SensorValue", 0);
    }

    public void update(){
        boolean val = m_sensorDIO.get();
        if(RobotBase.isSimulation()){
            val = m_testingState.get()!=0;
        }
        State nextState = State.c_State_No_Note;

        switch (m_state) {
            case c_State_No_Note:
                if(val){
                    nextState = State.c_State_First_Leg;
                } else {
                    nextState = State.c_State_No_Note;
                }
                break;

            case c_State_First_Leg:
                if(val){
                    nextState = State.c_State_First_Leg;
                } else {
                    nextState = State.c_State_Middle_Hole;
                }
                break;

            case c_State_Middle_Hole:
                if(val){
                    nextState = State.c_State_Second_Leg;
                } else {
                    nextState = State.c_State_Middle_Hole;
                }
                break;

            case c_State_Second_Leg:
                if(val){
                    nextState = State.c_State_Second_Leg;
                } else {
                    nextState = State.c_State_No_Note;
                }
                break;

            default:
                System.out.println("Note Proximity Sensor has invalid state");
                nextState = State.c_State_No_Note;
                break;
        }

        m_state = nextState;
        updateTD();
    }

    public boolean hasNote() {
        return m_state != State.c_State_No_Note;
    }

    public void updateTD(){
        switch (m_state) {
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

    public boolean noteIsCentered() { return m_state == State.c_State_Middle_Hole; }
}
