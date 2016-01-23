package com;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;

public class AnalogAbsoluteEncoder implements Runnable, PIDSource {
	
	private AnalogInput ai;
	private double offset;
	private double realDistance;
	private double lastVoltage;
	private int cycles;
	private final double ANALOG_RANGE = 4.972;
	
	public AnalogAbsoluteEncoder(int channel) {
		ai = new AnalogInput(channel);
		ai.setAverageBits(4);
		zero();
		new Thread(this).start();
	}

	public void run() {
		while(true) {
			double offsetVoltage = ai.getAverageVoltage() - offset;
			if(offsetVoltage - lastVoltage < -2.0) cycles++;
			if(offsetVoltage - lastVoltage > 2.0) cycles--;
			realDistance = ANALOG_RANGE * cycles + offsetVoltage;
			lastVoltage = offsetVoltage;
			Timer.delay(0.01);
		}
	}
	
	public double getDistance() {
		return realDistance;
	}
	
	public void zero() {
		offset = ai.getAverageVoltage();
		realDistance = 0.0;
		lastVoltage = 0.0;
		cycles = 0;
	}

	//Implemented methods from PIDSource so this class can easily operate as a PIDSource
	public void setPIDSourceType(PIDSourceType pidSource) {
		//This will always be a displacement PIDSource
	}

	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	public double pidGet() {
		return getDistance();
	}
}
