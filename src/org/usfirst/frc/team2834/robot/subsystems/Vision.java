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
	private boolean processImage = true; //Processing the image takes a lot of processing power and a lot of time, sometimes it may be best to leave it off
	private boolean isGoal = false; //Is there a particle that meets the criteria
	private double distance = 0.0; //EXPERIMENTAL: Gives the distance to a possible target
	private double alpha = 0.0; //EXPERIMENTAL: Vertical angle to the target
	private double beta = 0.0;  //EXPERIMENTAL: Horizontal angle to the target
	private double gamma = 0.0;
	private double delta = 0.0;
	private double zeta = 0.0;
	private final int FRAME_HEIGHT;
	private int FRAME_WIDTH;
	//private final double TARGET_WIDTH = 20.0;
	private final double TARGET_HEIGHT = 12.0;
	private final double TARGET_VERTICAL_DISTANCE = 82.0;
	//private final double DIST_TO_ROTATION_CENTER = 10;
	private double FOCAL_LENGTH = 606.0; //Coefficient for the relation between a camera image and actual dimensions
	private final int SAMPLES_TO_AVERAGE = 1;
	private final String SHOOTER_CAMERA = "cam1";
	private final String GROUND_CAMERA = "cam0";
	
	//Image fields
	//private USBCamera ground;
	//private AxisCamera shooter;
	private Image frame;		//Frame that will hold the raw image from camera
	private Image binaryFrame;	//Frame depicting possible targets
	private int shooterSession;		//Session id for the processing camera
	//private int groundSession; //Session for the ground/view camera
	
	//NIVision fields
	private NIVision.ParticleFilterCriteria2 criteria[];
	private NIVision.ParticleFilterOptions2 options;
	private NIVision.ParticleReport reports[];
	
	//HSV Ranges
	private NIVision.Range HRange, SRange, LRange;
	
	public Vision() {
		super("Vision");
		
		//Setup camera and images
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_HSL, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		FRAME_HEIGHT = NIVision.imaqGetImageSize(binaryFrame).height;
		
		//Set HSV bounds to that of the retro-reflective tape
		//The color threshold will filter out pixels outside of these ranges
    	HRange = new NIVision.Range(80, 255);
    	SRange = new NIVision.Range(230, 255);
    	LRange = new NIVision.Range(60, 255);
		
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
    	criteria[1].upper = 360;
    	//Run camera feed in separate thread so Scheduler does not interfere.
		new Thread(this, "Vision Processing Thread").start();
	}
    
	public void run() {
		initializeCamera();
		//Continuously grab images, possible process, and feed them to the dashboard.
		//Create another thread to process the image so that the camera feed is not interrupted by the
		//vision processing
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
		try {
			shooterSession = NIVision.IMAQdxOpenCamera(SHOOTER_CAMERA, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(shooterSession);
			/*groundSession = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(groundSession);*/
			//ground = new USBCamera("cam1");
			//shooter = new AxisCamera("10.28.34.89");
			//NIVision.IMAQdxStartAcquisition(groundSession);
			CameraServer.getInstance().startAutomaticCapture(GROUND_CAMERA);
			NIVision.IMAQdxStartAcquisition(shooterSession);
		} catch (VisionException e) {
			System.out.println("====Problem Starting Camera, trying agin in 5s====");
			e.printStackTrace();
			Timer.delay(5);
			initializeCamera();
		}
	}
	
    public void calculate() {
    	double[] alphas = new double[SAMPLES_TO_AVERAGE];
    	boolean isGoal = false;
    	NIVision.ParticleReport best = new NIVision.ParticleReport();
    	for (int i = 0; i < SAMPLES_TO_AVERAGE; i++) {
    		NIVision.IMAQdxGrab(shooterSession, frame, 1);
			//Filter out pixels and particles that do not meet the criteria
			NIVision.imaqColorThreshold(binaryFrame, frame, 1, ColorMode.HSL, HRange, SRange, LRange);
			FRAME_WIDTH = NIVision.imaqGetImageSize(binaryFrame).width;
			//Complete any partial shapes
			NIVision.imaqConvexHull(binaryFrame, binaryFrame, 1);
			NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, options, null);
			//Generate report for area and sort
			int particles = NIVision.imaqCountParticles(binaryFrame, 1);
			//Use the first sample to determine if there is a goal in view
			if (particles > 0) {
				isGoal = true;
				reports = new NIVision.ParticleReport[particles];
				best = new NIVision.ParticleReport();
				best.area = Integer.MIN_VALUE;
				//Add measurements of each particle to a report
				for (int p = 0; p < particles; p++) {
					reports[p] = new NIVision.ParticleReport();
					reports[p].area = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_AREA);
					reports[p].boundingBox.left = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					reports[p].boundingBox.width = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
					reports[p].boundingBox.top = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					reports[p].projectionX = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_AVERAGE_HORIZ_SEGMENT_LENGTH);
					reports[p].projectionY = (int) NIVision.imaqMeasureParticle(binaryFrame, p, 0,
							NIVision.MeasurementType.MT_AVERAGE_VERT_SEGMENT_LENGTH);
					//Find the best particle by area
					if (reports[p].area > best.area) {
						best = reports[p];
					}
				}
				alphas[i] = Math.asin((2.0 * TARGET_VERTICAL_DISTANCE * best.projectionY) / (FOCAL_LENGTH * TARGET_HEIGHT)) / 2.0;
			} 
		}
    	this.isGoal = isGoal;
    	if (isGoal) {
    		Arrays.sort(alphas);
    		alpha = alphas[SAMPLES_TO_AVERAGE / 2];
    		//double h = best.projectionY;
        	//double w = best.projectionX;
    		FOCAL_LENGTH = SmartDashboard.getNumber("Focal Length", 606);
    		//alpha = Math.asin((2 * TARGET_VERTICAL_DISTANCE * h) / (FOCAL_LENGTH * TARGET_HEIGHT)) / 2.0;
    		distance = TARGET_VERTICAL_DISTANCE / Math.tan(alpha);
    		//beta = Math.asin(Math.sqrt(1.0 - ((distance * w) / (FOCAL_LENGTH * TARGET_WIDTH))) / Math.cos(alpha));
    		gamma = 2.0 * Math.atan(((best.boundingBox.left + (best.boundingBox.width / 2.0)) - (FRAME_WIDTH / 2.0)) / FOCAL_LENGTH);
    		/*delta = Math.asin((DIST_TO_ROTATION_CENTER * Math.sin(gamma)) /
    				Math.sqrt(Math.pow(DIST_TO_ROTATION_CENTER, 2) + Math.pow(distance, 2) - 2 * distance * DIST_TO_ROTATION_CENTER * Math.cos(gamma)));*/
    		zeta = Math.atan((FRAME_HEIGHT / 2.0) - best.boundingBox.top);
		}
    }
    
    public void useShooterView() {
    	shooterView = true;
    }
    
    public void useGroundView() {
    	shooterView = false;
    }
    
    public void initDefaultCommand() {
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
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putBoolean("Is Goal", isGoal);
		SmartDashboard.putNumber("Estimated Distance", getDistance());
    	SmartDashboard.putNumber("Alpha", getAlpha());
    	SmartDashboard.putNumber("Beta", getBeta());
    	SmartDashboard.putNumber("Gamma", getGamma());
    	SmartDashboard.putNumber("Delta", getDelta());
    	SmartDashboard.putBoolean("Camera", shooterView);
    	if (isGoal) {
			//SmartDashboard.putNumber("Height", best.projectionY);
			//SmartDashboard.putNumber("Width", best.projectionX);
		}
	}
}

