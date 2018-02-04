package org.usfirst.frc.team5556.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import com.ctre.CANTalon;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot { // these classes mean something
	//RobotDrive m_robotDrive; // It's a command to drive the robot
	Joystick rotateStick, m_drivestick;//Left joystick moves robot around, right joystick rotates
	Button aButton, bButton; // a button (xbox) to launch gear
// bButton; // b button (xbox) to climb rope
	VictorSP frontLeft, frontRight, rearLeft, rearRight; // The wheels
	CANTalon gearLaunch, ropeClimb; // Speed Controller for rope climb and gear launch
	// Victor and Talon are the types of speed controllers, are within the libraries
	// And probably some sensors
	RobotDrive m_robotDrive;// = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
//	Joystick m_driveStick = new Joystick(1);
	AnalogGyro gyro;
		
	int station;
	int Counter;
	int buttonTimeOut; // I don't why we need this
	double Kp = 0.03;
	//set some variables used later in the code
	
	//boolean isReversed = true; I don't know what this does yet
	
	
	@Override
	public void robotInit() {
		//m_robotDrive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
		//Joystick m_driveStick = new Joystick(1);
		frontLeft = new VictorSP(0);
		frontRight = new VictorSP(3);
		rearLeft = new VictorSP(1);
		rearRight = new VictorSP(2); // the PWM outputs
		m_robotDrive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
		gearLaunch = new CANTalon(14);
		//myGear = new TalonLaunch(gearLaunch);
		ropeClimb = new CANTalon(15);
		//the talon stuff will probably change
		gyro = new AnalogGyro(1);
		m_robotDrive.setExpiration(0.1);
		
	}
		
	public class OI {
		Joystick rotateStick = new Joystick(0);
		Joystick m_driveStick = new Joystick(1);
		Button aButton = new JoystickButton(rotateStick, 1),
				bButton = new JoystickButton(rotateStick, 2);
	}	
	
	public void OI() {	
		aButton.whenPressed(launchGear());
		bButton.whenPressed(climbRope());
	}
	// For some reason the joystick and buttons on the controller need to be defined separate
	// from the motors. I don't know why, just some way the frc code was laid out probably.
	// I also believe it needs to be the special OI class, and one needs to be void and the
	// other needs to be class. 
	

	@Override
	public void autonomousInit() {
		//This function is run once during autonomous mode
		Counter = 0;
		station = DriverStation.getInstance().getLocation();
		//this reads what driver station you are
		gyro.reset();
	}

	@Override
	public void autonomousPeriodic() {
		//This function loops during autonomous
		Counter++; 
		if (station != 2 && station != 3) {
			position1();
		}
		if (station != 1 && station != 3) {
			position2();
		}
		if (station != 1 && station != 2) {
			position3();
		}
	}
	//This is supposed to check the driver station and then do a certain function based on the 
	//driver station value
	
	public void position1(){
		double angle = gyro.getAngle();
		if (Counter > 0 && Counter < 100) // these values might have to be tweaked
			m_robotDrive.mecanumDrive_Cartesian(0, 0.2, 0, -angle*Kp); //the numbers might have to be negative
		else if (Counter >= 100 && Counter < 200)
			m_robotDrive.mecanumDrive_Cartesian(0, 0, -0.4, 0); //I don't know if this will work
		else if (Counter >= 200 && Counter < 300)
			m_robotDrive.mecanumDrive_Cartesian(0, 0.2, 0, -angle*Kp);
		else if (Counter >= 300 && Counter < 350)
			ropeClimb.set(0.5); // this is supposed to launch the gear
		else if (Counter >= 350 && Counter < 400)
			ropeClimb.set(-0.5);
		else
			m_robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
		
	}
	public void position2(){
		double angle = gyro.getAngle();
		if (Counter > 0 && Counter < 150) // these values might have to be tweaked
			m_robotDrive.mecanumDrive_Cartesian(0, 0.2, 0, -angle*Kp); //the numbers might have to be negative
		else if (Counter >= 150 && Counter < 200)
			gearLaunch.set(1); // this is supposed to launch the gear
		else if (Counter >= 200 && Counter < 150)
			gearLaunch.set(-1);
		else
			m_robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
	}
	
	public void position3(){
		double angle = gyro.getAngle();
		if (Counter > 0 && Counter < 100) // these values might have to be tweaked
			m_robotDrive.mecanumDrive_Cartesian(0, 0.2, 0,-angle*Kp); //the numbers might have to be negative
		else if (Counter >= 100 && Counter < 200)
			m_robotDrive.mecanumDrive_Cartesian(0, 0, 0.2, -angle*Kp); // I don't know if this work
		else if (Counter >= 200 && Counter < 300)
			m_robotDrive.mecanumDrive_Cartesian(0.5, 0.2, 0, -angle*Kp);
		else if (Counter >= 300 && Counter < 350)
			gearLaunch.set(1); // this is supposed to launch the gear
		else if (Counter >= 350 && Counter < 400)
			gearLaunch.set(-1);
		else
			m_robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
		
	}

	@Override
	public void teleopInit() { 
		// This function is called each time the robot enter tele-operated mode
	}

	@Override
	public void teleopPeriodic() {
		gyro.reset();
		double angle = gyro.getAngle();
		//This function is called periodically during operator control
		m_robotDrive.mecanumDrive_Cartesian(m_drivestick.getX(), m_drivestick.getY(), m_drivestick.getTwist(), -angle*Kp);
		Timer.delay(0.02);		
		//This is special joystick driving with mecanum wheels
//		aButton.whenPressed(launchGear());
//		bButton.whenPressed(ropeClimb());
	}
	
	public Command launchGear() {
		gearLaunch.set(1);
		Timer.delay(1);
		gearLaunch.set(-1);
		Timer.delay(1);
		return null;
		//launches the gear
	}
	
	public Command climbRope() {
		
		ropeClimb.set(1);
		Timer.delay(1);
		ropeClimb.set(.6);
		Timer.delay(1);
		ropeClimb.set(.4);
		Timer.delay(1);
		ropeClimb.set(.1);
		Timer.delay(1);
		ropeClimb.set(0);
		return null;
		//climbs the rope
	}
	
	@Override
	public void testPeriodic() {
		//This function is called periodically during test mode
		LiveWindow.run();
	}
}
