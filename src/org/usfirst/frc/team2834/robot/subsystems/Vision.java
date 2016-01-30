package org.usfirst.frc.team2834.robot.subsystems;

import com.DashboardSender;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * Subsystems that processes images based on the retroreflective tape around the goals
 */
public class Vision extends Subsystem implements Runnable, DashboardSender {
	
	private boolean shooterView = false; //Which camera (Shooter or ground) should be sent to the dashboard
	private boolean processImage = true; //Processing the image takes a lot of processing power and a lot of time, sometimes it may be best to leave it off
	private boolean isGoal = false; //Is there a particle that meets the criteria
	double distance = 0.0; //EXPERIMENTAL: Gives the distance to a possible target
	double autoRotateToTarget = 0.0; //EXPERIMENTAL: Factor to rotate the robot so it is centered on a possible target
	
	//Image fields
	Image frame;		//Frame that will hold the raw image from camera
	Image binaryFrame;	//Frame depicting possible targets
	AxisCamera cam;		//Object reference to an Axis Camera
	int processSession;		//Session id for the processing camera
	int viewSession; //Session if for the ground/view camera
	
	//NIVision fields
	NIVision.ParticleFilterCriteria2 criteria[];
	NIVision.ParticleFilterOptions2 options;
	NIVision.ParticleReport reports[];
	NIVision.ParticleReport best;
	
	//HSV Ranges
	NIVision.Range HRange, SRange, LRange;
	
	public Vision() {
		super("Vision");
		//setOutputRange(-1.0, 1.0);
		
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
			processSession = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(processSession);
			NIVision.IMAQdxStartAcquisition(processSession);
			viewSession = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(viewSession);
			NIVision.IMAQdxStartAcquisition(viewSession);
			//Run camera feed in separate thread so Scheduler does not interfere.
			new Thread(this, "Vision Processing Thread").start();
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
    	}
    }
    
    public void run() {
    	//Continuously grab images, possible process, and feed them to the dashboard.
    	while(true) {
    		int session = shooterView ? processSession : viewSession;
			NIVision.IMAQdxGrab(session, frame, 1);
			//cam.getImage(frame);
			if(processImage) {
				calculate();
			}
			CameraServer.getInstance().setImage(frame);
    	}
    }
    
    public synchronized void calcDistance() {
    	if(isGoal) {
    		double focalLength = SmartDashboard.getNumber("Focal Length", 1000);	//Coefficient for the relation between a camera image and actual dimensions
			double aHeight = 14.0;		//Actual height of target
			double z = 90.0;				//Actual vertical distance from the center of the target to the camera
			double alpha = Math.asin((2 * z * best.boundingBox.height) / (focalLength * aHeight)) / 2.0;
			distance = z / Math.tan(alpha);
    	} else {
    		distance = 0.0;
    	}
    }
    
    public synchronized void calcDistance2() {
    	if(isGoal) {
    		double fov = 26.9277;
    		double aWidth = 20.0;
    		distance = aWidth * NIVision.imaqGetImageSize(binaryFrame).width /
    				(2.0 * best.boundingBox.width * Math.tan(fov));
    	} else {
    		distance = 0.0;
    	}
    }

    public void initDefaultCommand() {
    }

	/*@Override
	protected double returnPIDInput() {
		//return the center of the best particle relative to the center of the image
		return (best.boundingBox.width / 2.0 + best.boundingBox.left) - (NIVision.imaqGetImageSize(binaryFrame).width / 2.0);
	}

	@Override
	protected void usePIDOutput(double output) {
		autoRotateToTarget = output;
	}*/
	
	public double getAutoRotate() {
		return autoRotateToTarget;
	}

	public boolean isShooterView() {
		return shooterView;
	}

	public void setShooterView(boolean shooterView) {
		this.shooterView = shooterView;
	}

	public boolean isProcessImage() {
		return processImage;
	}

	public void setProcessImage(boolean processImage) {
		this.processImage = processImage;
	}

	public boolean isGoal() {
		return isGoal;
	}

	@Override
	public void dashboardInit() {
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putBoolean("Is Goal", isGoal);
		if(isGoal) {
			SmartDashboard.putNumber("Best Particle Box Width", best.boundingBox.width);
	    	SmartDashboard.putNumber("Best Particle Box Height", best.boundingBox.height);
		}
	}
}

