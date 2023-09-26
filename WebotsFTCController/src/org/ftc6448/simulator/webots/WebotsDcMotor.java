package org.ftc6448.simulator.webots;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;


/**
 * Empty motor implementation.
 *
 */
public class WebotsDcMotor implements DcMotorEx{
	
	protected final String name;
	
	public WebotsDcMotor(String name) {
		this.name=name;
		
	}

	public MotorConfigurationType getMotorType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMotorType(MotorConfigurationType motorType) {
		// TODO Auto-generated method stub
		
	}

	public DcMotorController getController() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPortNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
		// TODO Auto-generated method stub
		
	}

	public ZeroPowerBehavior getZeroPowerBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPowerFloat() {
		// TODO Auto-generated method stub
		
	}

	public boolean getPowerFloat() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTargetPosition(int position) {
		// TODO Auto-generated method stub
		
	}

	public int getTargetPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMode(RunMode mode) {
		// TODO Auto-generated method stub
		
	}

	public RunMode getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDirection(Direction direction) {
		// TODO Auto-generated method stub
		
	}

	public Direction getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPower(double power) {
		// TODO Auto-generated method stub
		
	}

	public double getPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Manufacturer getManufacturer() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConnectionInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void resetDeviceConfigurationForOpMode() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMotorEnable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMotorDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMotorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVelocity(double angularRate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVelocity(double angularRate, AngleUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getVelocity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getVelocity(AngleUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
		// TODO Auto-generated method stub
		
	}

	public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPositionPIDFCoefficients(double p) {
		// TODO Auto-generated method stub
		
	}

	public PIDCoefficients getPIDCoefficients(RunMode mode) {
		// TODO Auto-generated method stub
		return null;
	}

	public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTargetPositionTolerance(int tolerance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTargetPositionTolerance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrent(CurrentUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentAlert(CurrentUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCurrentAlert(double current, CurrentUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOverCurrent() {
		// TODO Auto-generated method stub
		return false;
	}

}
