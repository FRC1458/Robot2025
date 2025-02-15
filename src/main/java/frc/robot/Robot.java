// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import au.grapplerobotics.CanBridge;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.Loops.Looper;
//import frc.robot.controlboard.ControlBoard;
//import frc.robot.controlboard.DriverControls;
import frc.robot.subsystems.DummySubsystem;
import frc.robot.subsystems.Laser;
import frc.robot.subsystems.SubsystemManager;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */


 
public class Robot extends TimedRobot {
  
  private RobotContainer25 m_robotContainer;


    public  DigitalInput level0 = new DigitalInput(0);
    public  DigitalInput level1 = new DigitalInput(1);
    public  DigitalInput level2 = new DigitalInput(2);
    public  DigitalInput level3 = new DigitalInput(3);
  
  @Override
  public void robotInit() {
    //m_robotContainer = new RobotContainer25();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("Sensor 1", level0.get());
    SmartDashboard.putBoolean("Sensor 2", level1.get());
    SmartDashboard.putBoolean("Sensor 3", level2.get());
    SmartDashboard.putBoolean("Sensor 4", level3.get());

    


  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
   
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    //m_robotContainer.initManualMode();

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //m_robotContainer.manualModePeriodic();
    SmartDashboard.putNumber("AAAAAAAAH", Laser.getMeasurementIntake());
    System.out.println(Laser.getMeasurementIntake());
  }

  @Override
  public void testInit() {
   
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
   
  }
}
