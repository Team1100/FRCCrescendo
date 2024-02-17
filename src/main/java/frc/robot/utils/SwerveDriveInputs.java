// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import java.util.function.Supplier;

/** Add your docs here. */
public class SwerveDriveInputs {
    Supplier<Double> m_xSupplier;
    Supplier<Double> m_ySupplier;
    Supplier<Double> m_rotSupplier;

    public SwerveDriveInputs(Supplier<Double> xSupplier, Supplier<Double> ySupplier, Supplier<Double> rotationSupplier){
        m_xSupplier = xSupplier;
        m_ySupplier = ySupplier;
        m_rotSupplier = rotationSupplier;
    }

    public double getX(){
        return m_xSupplier.get();
    }

    public double getY(){
        return m_ySupplier.get();
    }

    public double getRotation(){
        return m_rotSupplier.get();
    }
}
