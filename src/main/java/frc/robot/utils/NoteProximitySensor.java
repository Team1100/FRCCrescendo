// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalInput;

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

    public NoteProximitySensor(int dioPort) {
        m_sensorDIO = new DigitalInput(dioPort);
        m_state = State.c_State_No_Note;
    }

    public void update(){
        boolean val = m_sensorDIO.get();
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

            case c_State_Second_Leg:
                if(val){
                    nextState = State.c_State_Second_Leg;
                } else {
                    nextState = State.c_State_No_Note;
                }

            default:
                System.out.println("Note Proximity Sensor has invalid state");
                nextState = State.c_State_No_Note;
                break;
        }

        m_state = nextState;
    }

    public boolean hasNote() {
        return m_state != State.c_State_No_Note;
    }
}
