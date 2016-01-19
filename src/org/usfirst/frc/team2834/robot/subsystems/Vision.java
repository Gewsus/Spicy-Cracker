package org.usfirst.frc.team2834.robot.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 *
 */
public class Vision extends Subsystem implements Runnable {
	
	public boolean processImage = true;
	private boolean isGoal = false;
	double distance = 0.0;
	
	//Image fields
	Image frame;		//Frame that will hold the raw image from camera
	Image binaryFrame;	//Frame depicting possible targets
	AxisCamera cam;		//Object reference to an Axis Camera
	int session;		//Session id needed for a USB camera
	
	//NIVision fields
	NIVision.ParticleFilterCriteria2 criteria[];
	NIVision.ParticleFilterOptions2 options;
	NIVision.ParticleReport reports[];
	NIVision.ParticleReport best;
	
	//HSV Ranges
	NIVision.Range HRange;
	NIVision.Range SRange;
	NIVision.Range LRange;
	
	public Vision() {
		super("Vision");
		//Setup camera and images
		//cam = new AxisCamera("10.28.34.20");
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_HSL, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		
		//Set HSV bounds to that of the retro-reflective tape
		//The color threshold will filter out pixels outside of these ranges
    	HRange = new NIVision.Range(80, 100);
    	SRange = new NIVision.Range(230, 255);
    	LRange = new NIVision.Range(30, 100);
		
		//Set criteria to determine which particles to filter out
    	options = new NIVision.ParticleFilterOptions2(0, 1, 1, 1);
		criteria = new NIVision.ParticleFilterCriteria2[1];
    	criteria[0] = new NIVision.ParticleFilterCriteria2();
    	criteria[0].lower = 50;
    	criteria[0].parameter = NIVision.MeasurementType.MT_AREA;
    	
    	//Put data on dashboard that may need to be tested
    	//SmartDashboard.putNumber("Focal Length", 1000);
    	
    	//Identify camera session and begin receiving video.
    	//FRC crashes the program if the camera does not exist and you try to run this code
    	//so i made it easy to just disable the camera if it is disconnected.
		if(cam != null) {
			session = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
			//Run camera feed in separate thread so Scheduler does not interfere.
			new Thread(this).start();
		}
	}
    
    private void calculate() {
    	
    	//Filter out pixels and particles that do not meet the criteria
    	NIVision.imaqColorThreshold(binaryFrame, frame, 1, ColorMode.HSL, HRange, SRange, LRange);
    	NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, options, null);
    	
    	//Complete any partial shapes
    	NIVision.imaqConvexHull(binaryFrame, binaryFrame, 1);
    	
    	//Generate report for area and sort
    	int particles = NIVision.imaqCountParticles(binaryFrame, 1);
    	isGoal = particles > 0;
    	if(isGoal) {
			reports = new NIVision.ParticleReport[particles];
			best = new NIVision.ParticleReport();
			best.area = 0;
			for (int p = 0; p < particles; p++) {
				reports[p].area = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
						NIVision.MeasurementType.MT_AREA);
				reports[p].boundingBox.left = (int) NIVision.imaqMeasureParticle(binaryFrame, particles, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				reports[p].boundingBox.top = (int) NIVision.imaqMeasureParticle(binaryFrame, particles, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				reports[p].boundingBox.width = (int) NIVision.imaqMeasureParticle(binaryFrame, particles, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
				reports[p].boundingBox.height = (int) NIVision.imaqMeasureParticle(binaryFrame, particles, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
				if (reports[p].area > best.area) {
					best = reports[p];
				}
			}
			NIVision.imaqDrawShapeOnImage(frame, frame, best.boundingBox, NIVision.DrawMode.DRAW_VALUE, NIVision.ShapeMode.SHAPE_RECT, 0);
			SmartDashboard.putNumber("Best Particle Box Width", best.boundingBox.width);
	    	SmartDashboard.putNumber("Best Particle Box Height", best.boundingBox.height);
    	}
    	SmartDashboard.putBoolean("Is Goal", isGoal);
    }
    
    public void run() {
    	//Continuously grab images, possible process, and feed them to the dashboard.
    	while(true) {
			NIVision.IMAQdxGrab(session, frame, 1);
			//cam.getImage(frame);
			if(processImage) {
				calculate();
			}
			CameraServer.getInstance().setImage(frame);
    	}
    }
    
    public void getDistance() {
    	if(isGoal) {
    		double focalLength = SmartDashboard.getNumber("Focal Length", 1000);	//Coefficient for the relation between a camera image and actual dimensions
			double aHeight = 14;		//Actual height of target
			double z = 90;				//Actual vertical distance from the center of the target to the camera
			double alpha = Math.asin((2 * z * best.boundingBox.height) / (focalLength * aHeight)) / 2.0;
			distance = z / Math.tan(alpha);
    	}
    	distance = 0.0;
    }

    public void initDefaultCommand() {
    }
}

