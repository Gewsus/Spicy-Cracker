package org.usfirst.frc.team2834.robot.subsystems;

import java.util.Arrays;

import com.DashboardSender;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystems that processes images based on the retroreflective tape around the goals
 */
public class Vision extends Subsystem implements Runnable, DashboardSender {
	
	private boolean shooterView = true; //Which camera (Shooter or ground) should be sent to the dashboard
	private boolean processImage = false; //Processing the image takes a lot of processing power and a lot of time, sometimes it may be best to leave it off
	private boolean isGoal = false; //Is there a particle that meets the criteria
	private double distance = 0.0; //EXPERIMENTAL: Gives the distance to a possible target
	private double alpha = 0.0; //EXPERIMENTAL: Vertical angle to the target
	private double beta = 0.0;  //EXPERIMENTAL: Horizontal angle to the target
	private double gamma = 0.0;
	private double delta = 0.0;
	private double zeta = 0.0;
	//private int FRAME_HEIGHT;
	private int FRAME_WIDTH;
	private final double TARGET_WIDTH = 20.0;
	private final double TARGET_HEIGHT = 12.0;
	private final int VERTICAL_CROSHAIR = 250;
	private final double TARGET_VERTICAL_DISTANCE = 82.0;
	private final double DIST_TO_ROTATION_CENTER = 10;
	private double FOCAL_LENGTH = 606.0; //Coefficient for the relation between a camera image and actual dimensions
	private double FOV = 0.65;
	private final int SAMPLES_TO_AVERAGE = 1;
	private final String SHOOTER_CAMERA = "cam1";
	private final String GROUND_CAMERA = "cam0";
	
	//Image fields
	private Image frame;		//Frame that will hold the raw image from camera
	private Image binaryFrame;	//Frame depicting possible targets
	private int shooterSession;		//Session id for the processing camera
	
	//NIVision fields
	private NIVision.ParticleFilterCriteria2 criteria[];
	private NIVision.ParticleFilterOptions2 options;
	private HawkReport reports[];
	
	//HSV Ranges
	private NIVision.Range HRange, SRange, LRange;
	
	public Vision() {
		super("Vision");
		
		//Setup camera and images
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_HSL, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		
		//Set HSV bounds to that of the retro-reflective tape
		//The color threshold will filter out pixels outside of these ranges
    	HRange = new NIVision.Range(80, 150);
    	SRange = new NIVision.Range(230, 255);
    	LRange = new NIVision.Range(60, 120);
		
		//Set criteria to determine which particles to filter out
    	options = new NIVision.ParticleFilterOptions2(0, 0, 1, 1);
    	//Filter out particles that are too small
		criteria = new NIVision.ParticleFilterCriteria2[2];
    	criteria[0] = new NIVision.ParticleFilterCriteria2();
    	criteria[0].parameter = NIVision.MeasurementType.MT_AREA;
    	criteria[0].lower = 2500;
    	//Filter for the particles that may appear due to the light shining on crossbars
    	criteria[1] = new NIVision.ParticleFilterCriteria2();
    	criteria[1].parameter = NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM;
    	criteria[1].lower = 0;
    	criteria[1].upper = 400;
    	//Run camera feed in separate thread so Scheduler does not interfere.
		new Thread(this, "Vision Processing Thread").start();
	}
    
	public void run() {
		initializeCamera();
		//Continuously grab images, possible process, and feed them to the dashboard.
		//Create another thread to process the image so that the camera feed is not interrupted by the
		//vision processing
		calculate();
		while(true) {
			if (processImage) {
				try {
					calculate();
				} catch (VisionException e) {
					System.out.println("===Vision Processing Error===");
					e.printStackTrace();
				}
			}
		}
	}

	private void initializeCamera() {
		//Identify camera session and begin receiving video.
		//FRC crashes the program if the camera does not exist and you try to run this code
		//so i made it easy to just disable the camera if it is disconnected.
		CameraServer.getInstance().startAutomaticCapture(GROUND_CAMERA);
		try {
			shooterSession = NIVision.IMAQdxOpenCamera(SHOOTER_CAMERA, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(shooterSession);
			NIVision.IMAQdxStartAcquisition(shooterSession);
		} catch (VisionException e) {
			System.out.println("===Problem Starting Camera, trying agin in 1s===");
			e.printStackTrace();
			Timer.delay(1);
			initializeCamera();
		}
	}
	
    public void calculate() {
    	double[] alphas = new double[SAMPLES_TO_AVERAGE];
    	boolean isGoal = false;
    	HawkReport best = new HawkReport();
    	for (int i = 0; i < SAMPLES_TO_AVERAGE; i++) {
    		//FRAME_HEIGHT = NIVision.imaqGetImageSize(binaryFrame).height;
    		FRAME_WIDTH = NIVision.imaqGetImageSize(binaryFrame).width;
    		NIVision.IMAQdxGrab(shooterSession, frame, 1);
			//Filter out pixels and particles that do not meet the criteria
			NIVision.imaqColorThreshold(binaryFrame, frame, 1, ColorMode.HSL, HRange, SRange, LRange);
			//Complete any partial shapes
			NIVision.imaqConvexHull(binaryFrame, binaryFrame, 1);
			NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, options, null);
			//Generate report for area and sort
			int particles = NIVision.imaqCountParticles(binaryFrame, 1);
			//Use the first sample to determine if there is a goal in view
			if (particles > 0) {
				int bestID = 0;
				isGoal = true;
				reports = new HawkReport[particles];
				best.area = Integer.MIN_VALUE;
				//Add measurements of each particle to a report
				for (int p = 0; p < particles; p++) {
					reports[p] = new HawkReport();
					reports[p].area = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0, NIVision.MeasurementType.MT_AREA);
					//Find the best particle by area
					if (reports[p].area > best.area) {
						best = reports[p];
						bestID = p;
					}
				}
				best.boundingBox.left = (int) NIVision.imaqMeasureParticle(binaryFrame, bestID, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
				best.boundingBox.width = (int) NIVision.imaqMeasureParticle(binaryFrame, bestID, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
				best.boundingBox.top = (int) NIVision.imaqMeasureParticle(binaryFrame, bestID, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				best.estimatedWidth = NIVision.imaqMeasureParticle(binaryFrame, bestID, 0, NIVision.MeasurementType.MT_EQUIVALENT_RECT_LONG_SIDE);
				best.estimatedHeight = NIVision.imaqMeasureParticle(binaryFrame, bestID, 0, NIVision.MeasurementType.MT_EQUIVALENT_RECT_SHORT_SIDE);
				alphas[i] = Math.asin((2.0 * TARGET_VERTICAL_DISTANCE * best.estimatedHeight) / (FOCAL_LENGTH * TARGET_HEIGHT)) / 2.0;
			} 
		}
    	this.isGoal = isGoal;
    	if (isGoal) {
    		Arrays.sort(alphas);
    		alpha = alphas[SAMPLES_TO_AVERAGE / 2];
    		double h = best.estimatedHeight;
        	double w = best.estimatedWidth;
    		//FOCAL_LENGTH = SmartDashboard.getNumber("Focal Length", 606);
    		//FOV = SmartDashboard.getNumber("VFOV", 0.65);
    		distance = TARGET_VERTICAL_DISTANCE / Math.tan(alpha);
    		//distance = TARGET_HEIGHT * FRAME_HEIGHT / (2 * h * FOV);
    		beta = Math.asin(Math.sqrt(1.0 - ((distance * w) / (FOCAL_LENGTH * TARGET_WIDTH))) / Math.cos(alpha));
    		gamma = 2.0 * Math.atan(((best.boundingBox.left + (best.boundingBox.width / 2.0)) - (FRAME_WIDTH / 2.0)) / (FOCAL_LENGTH + 30.0));
    		delta = Math.asin((DIST_TO_ROTATION_CENTER * Math.sin(gamma)) / Math.sqrt(Math.pow(DIST_TO_ROTATION_CENTER, 2) + Math.pow(distance, 2) - 2 * distance * DIST_TO_ROTATION_CENTER * Math.cos(gamma)));
    		zeta = (best.boundingBox.top + h - VERTICAL_CROSHAIR) / 135.0;
		}
    }
    
    private class HawkReport extends NIVision.ParticleReport {
    	public double estimatedWidth;
    	public double estimatedHeight;
    }
    
    public void useShooterView() {
    	shooterView = true;
    }
    
    public void useGroundView() {
    	shooterView = false;
    }
    
    public void initDefaultCommand() {
    	//setDefaultCommand(new ProcessImage());
    }
	
	public double getAlpha() {
		return alpha;
	}
	
	public double getBeta() {
		return beta;
	}
	
	public double getGamma() {
		return gamma;
	}
	
	public double getDelta() {
		return delta;
	}
	
	public double getZeta() {
		return zeta;
	}

	public double getDistance() {
		return distance;
	}

	public boolean isShooterView() {
		return shooterView;
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
		SmartDashboard.putNumber("Focal Length", FOCAL_LENGTH);
		SmartDashboard.putNumber("VFOV", FOV);
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putBoolean("Is Goal", isGoal);
		SmartDashboard.putNumber("Estimated Distance", getDistance());
		SmartDashboard.putNumber("Alpha", getAlpha());
		SmartDashboard.putNumber("Beta", getBeta());
		SmartDashboard.putNumber("Gamma", getGamma());
		SmartDashboard.putNumber("Delta", getDelta());
		SmartDashboard.putNumber("Zeta", getZeta());
		SmartDashboard.putBoolean("Camera", shooterView);
	}
}

