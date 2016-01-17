package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.commands.CameraFeedWithProcessing;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 *
 */
public class Vision extends Subsystem implements Runnable {
	
	public boolean processImage = true;
	private boolean isGoal = false;
	
	//Image fields
	Image frame;
	Image binaryFrame;
	AxisCamera cam;
	
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
		//Setup camera and images
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_HSL, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		cam = new AxisCamera("10.28.34.20");
		
		//Adjust criteria to determine which particles to filter out
		criteria = new NIVision.ParticleFilterCriteria2[1];
    	options = new NIVision.ParticleFilterOptions2(0, 1, 1, 1);
    	//Area parameter
    	criteria[0].lower = 50;
    	criteria[0].parameter = NIVision.MeasurementType.MT_AREA;
    	
    	new Thread(this).run();
	}
    
    public void calculate() {
    	
    	//Set HSV bounds to that of the retro-reflective tape
    	HRange = new NIVision.Range(55, 140);
    	SRange = new NIVision.Range(100, 255);
    	LRange = new NIVision.Range(100, 130);
    	
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
		}
		if (best.area != 0) {
			NIVision.imaqDrawShapeOnImage(frame, frame, best.boundingBox, NIVision.DrawMode.DRAW_VALUE, NIVision.ShapeMode.SHAPE_RECT, 0);
		}
    }
    
    public void run() {
    	cam.getImage(frame);
    	if(processImage) {
    		calculate();
    	}
    	CameraServer.getInstance().setImage(frame);
    }
    
    public void processImage(boolean processImage) {
    	this.processImage = processImage;
    }
    
    public double getDistance() {
    	if(isGoal) {
    		double focalLength = 0.3;
			double aHeight = 14;
			//double aWidth = 20;
			double z = 90;
			double alpha = Math.asin((2 * z * best.boundingBox.height) / (focalLength * aHeight)) / 2.0;
			return z / Math.tan(alpha);
    	}
    	return 0.0;
    }

    public void initDefaultCommand() {
        setDefaultCommand(new CameraFeedWithProcessing());
    }
}

