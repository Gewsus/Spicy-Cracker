package com;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class HawkServer2 implements Runnable {
	
	AxisCamera shooter;
	USBCamera ground;
	boolean isShooter = true;
	
	public HawkServer2(String groundName) {
		shooter = new AxisCamera("axis-camera.local");
		ground = new USBCamera(groundName);
	}

	@Override
	public void run() {
		ground.startCapture();
		Image frame;
		while(true) {
			frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
			if(isShooter) {
				shooter.getImage(frame);
			} else {
				ground.getImage(frame);
			}
			CameraServer.getInstance().setImage(frame);
		}
	}
}
