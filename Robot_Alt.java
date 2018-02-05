/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5556.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot_Alt extends IterativeRobot {
	private DifferentialDrive m_robotDrive
			= new DifferentialDrive(new Spark(0), new Spark(1));
	private Timer m_timer = new Timer();
	// joysticks
	private Joystick driveStick = new Joystick(0); // 
	private Joystick fightStick = new Joystick(1);
	// motors & pnuematics. We're not sure we're using talons, may change later. Orientation is based standing behind the robot.
	private Talon liftRight = new Talon(2); // right lift motor
	private Talon liftLeft = new Talon(3); // left lift motor
	private Talon armRight = new Talon(4); // right arm wheels
	private Talon armLeft = new Talon(5); // left arm wheels
	private DoubleSolenoid armPiston = new DoubleSolenoid(1, 2); // pneumatics for opening and closing arms
	
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
	}
		
	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if(m_timer.get() < 2.0) {
			m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
		} else {
			m_robotDrive.stopMotor(); // stop robot
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		
	}
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		/**
		 * Drive controls
		 */
		// driving code
		m_robotDrive.arcadeDrive(driveStick.getY(), driveStick.getTwist());
		
		// DEFCON 12
		if(
				(driveStick.getThrottle() < 2) && 
				(driveStick.getTrigger() == true) && 
				(driveStick.getRawButton(12) == true) && 
				(driveStick.getRawButton(11) == true) &&
				(fightStick.getRawButton(4) == true) // CHANGE THIS BINDING ONCE WE HAVE CLIMBING
			
			) {
			m_robotDrive.arcadeDrive(0, 1);
		}
		/**
		 * One driver mode
		 */
		if(SmartDashboard.getBoolean("DB/Button 0",false) == true) {
			/**
			 * Lift controls
			 */
			// raise lift
			if(driveStick.getRawButton(8) == true) {
				liftRight.set(1);
				liftLeft.set(1);
			} 
			// lower lift
			if(driveStick.getRawButton(7) == true) {
				liftRight.set(-1);
				liftLeft.set(-1);
			}
			
			
			/**
			 * Arm controls
			 */
			// toggle arms. armPos false = arms open, armPos true = arms closed
			boolean armPos = false;
			if(driveStick.getTrigger() == true) {
				if(armPos == false){
					armPos = true;
					Timer.delay(0.5); // prevent jam
				}
				if(driveStick.getTrigger() == true) {
					if(armPos == true){
						armPos = false;
						Timer.delay(0.5); // prevent jam
					}
				}
			}
			if(armPos = false) { // arms open
				armPiston.set(DoubleSolenoid.Value.kReverse);
			}
			if(armPos = true) { // arms closed
				armPiston.set(DoubleSolenoid.Value.kForward);
			}
			// eject cube
			if(driveStick.getRawButton(5) == true) {
				armRight.set(1);
				armLeft.set(1);
			}
			// eat cube
			if(driveStick.getRawButton(3) == true) {
				armRight.set(-1);
				armLeft.set(-1);
			}
			
			
			/**
			 * Climb controls
			 */
			// deploy climb mechanism
			if(driveStick.getRawButton(11) == true) { // big red button
				// deploy climb mechanism
			}
			// climb
			if(driveStick.getRawButton(12) == true) { // small button to right of big red
				// utilize climb mechanism
			}
		}
		else {
			
			/**
			 * Lift controls
			 */
			// raise lift
			if(fightStick.getY() > 0) {
				liftRight.set(1);
				liftLeft.set(1);
			} 
			// lower lift
			if(fightStick.getY() < 0) {
				liftRight.set(-1);
				liftLeft.set(-1);
			}
			
			
			/**
			 * Arm controls
			 */
			// toggle arms. armPos false = arms open, armPos true = arms closed
			boolean armPos = false;
			if(fightStick.getRawButton(1) == true) {
				if(armPos == false){
					armPos = true;
					Timer.delay(0.5); // prevent jam
				}
				if(fightStick.getRawButton(1) == true) {
					if(armPos == true){
						armPos = false;
						Timer.delay(0.5); // prevent jam
					}
				}
			}
			if(armPos = false) { // arms open
				armPiston.set(DoubleSolenoid.Value.kReverse);
			}
			if(armPos = true) { // arms closed
				armPiston.set(DoubleSolenoid.Value.kForward);
			}
			// eject cube
			if(fightStick.getRawButton(2) == true) {
				armRight.set(1);
				armLeft.set(1);
			}
			// eat cube
			if(fightStick.getRawButton(3) == true) {
				armRight.set(-1);
				armLeft.set(-1);
			}
			
			
			/**
			 * Climb controls
			 */
			// deploy climb mechanism
			if(fightStick.getRawButton(4) == true) { // big red button
				// deploy climb mechanism
			}
			// climb
			if(fightStick.getRawButton(5) == true) { // small button to right of big red
				// utilize climb mechanism
			}
		}
	} 
		


	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
