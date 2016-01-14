package org.usfirst.frc.team2834.robot.subsystems;

import java.lang.Math;
import java.util.Comparator;
import java.util.Vector;

import org.usfirst.frc.team2834.robot.commands.VisionStandard;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Example of finding yellow totes based on retroreflective target.
 * This example utilizes an image file, which you need to copy to the roboRIO
 * To use a camera you will have to integrate the appropriate camera details with this example.
 * To use a USB camera instead, see the SimpelVision and AdvancedVision examples for details
 * on using the USB camera. To use an Axis Camera, see the AxisCamera example for details on
 * using an Axis Camera.
 *
 * Sample images can found here: http://wp.wpi.edu/wpilib/2015/01/16/sample-images-for-vision-projects/
 * 
 * Modified to work as a subsystem in a robot.
 */
public class ExampleVision extends PIDSubsystem {
		//A structure to hold measurements of a particle
		public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
			double PercentAreaToImageArea;
			double Area;
			double BoundingRectLeft;
			double BoundingRectTop;
			double BoundingRectRight;
			double BoundingRectBottom;
			
			public int compareTo(ParticleReport r)
			{
				return (int)(r.Area - this.Area);
			}
			
			public int compare(ParticleReport r1, ParticleReport r2)
			{
				return (int)(r1.Area - r2.Area);
			}
		};

		//Structure to represent the scores for the various tests used for target identification
		public class Scores {
			double Area;
			double Aspect;
		};

		//Added fields
		AxisCamera camera;
		//int session;
		double displacementFromCenter = 0.0;
		boolean isGoal;
		double motorComp = 0.0;
		
		//Images
		Image frame;
		Image binaryFrame;
		int imaqError;

		//Constants
		NIVision.Range HUE_RANGE = new NIVision.Range(101, 64);	//Default hue range for yellow tote
		NIVision.Range SAT_RANGE = new NIVision.Range(88, 255);	//Default saturation range for yellow tote
		NIVision.Range VAL_RANGE = new NIVision.Range(134, 255);	//Default value range for yellow tote
		double AREA_MINIMUM = 0.5; //Default Area minimum for particle as a percentage of total image area
		//double LONG_RATIO = 2.22; //Tote long side = 26.9 / Tote height = 12.1 = 2.22
		//double SHORT_RATIO = 1.4; //Tote short side = 16.9 / Tote height = 12.1 = 1.4
		double SCORE_MIN = 75.0;  //Minimum score to be considered a tote
		double VIEW_ANGLE = 49.4; //View angle fo camera, set to Axis m1011 by default, 64 for m1013, 51.7 for 206, 52 for HD3000 square, 60 for HD3000 640x480
		NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
		NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
		Scores scores = new Scores();

		public ExampleVision() {
			//Setup PID Controller
			super(0.001, 0, 0);
			this.setOutputRange(-1.0, 1.0);
			
		    // create images
			frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
			binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
			criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);

			//Setup camera for acquisition
			//session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
	        //NIVision.IMAQdxConfigureGrab(session);
	        //NIVision.IMAQdxStartAcquisition(session);
			camera = new AxisCamera("10.0.0.1");
			
			//Put default values to SmartDashboard so fields will appear
			SmartDashboard.putNumber("Hue min", HUE_RANGE.minValue);
			SmartDashboard.putNumber("Hue max", HUE_RANGE.maxValue);
			SmartDashboard.putNumber("Sat min", SAT_RANGE.minValue);
			SmartDashboard.putNumber("Sat max", SAT_RANGE.maxValue);
			SmartDashboard.putNumber("Val min", VAL_RANGE.minValue);
			SmartDashboard.putNumber("Val max", VAL_RANGE.maxValue);
			SmartDashboard.putNumber("Area min %", AREA_MINIMUM);
		}

		public void calculate() {
			//read file in from disk. For this example to run you need to copy image.jpg from the SampleImages folder to the
			//directory shown below using FTP or SFTP: http://wpilib.screenstepslive.com/s/4485/m/24166/l/282299-roborio-ftp
			//NIVision.imaqReadFile(frame, "/home/lvuser/SampleImages/image.jpg");//This is for a test image
			//NIVision.IMAQdxStartAcquisition(session);//This is for a USB camer (webcam)
			camera.getImage(frame);//This is for an axis camera

			//Update threshold values from SmartDashboard. For performance reasons it is recommended to remove this after calibration is finished.
			HUE_RANGE.minValue = (int)SmartDashboard.getNumber("Hue min", HUE_RANGE.minValue);
			HUE_RANGE.maxValue = (int)SmartDashboard.getNumber("Hue max", HUE_RANGE.maxValue);
			SAT_RANGE.minValue = (int)SmartDashboard.getNumber("Sat min", SAT_RANGE.minValue);
			SAT_RANGE.maxValue = (int)SmartDashboard.getNumber("Sat max", SAT_RANGE.maxValue);
			VAL_RANGE.minValue = (int)SmartDashboard.getNumber("Val min", VAL_RANGE.minValue);
			VAL_RANGE.maxValue = (int)SmartDashboard.getNumber("Val max", VAL_RANGE.maxValue);

			//Threshold the image looking for yellow (tote color)
			NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.HSV, HUE_RANGE, SAT_RANGE, VAL_RANGE);

			//Send particle count to dashboard
			int numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			SmartDashboard.putNumber("Masked particles", numParticles);

			

			//filter out small particles
			float areaMin = (float)SmartDashboard.getNumber("Area min %", AREA_MINIMUM);
			criteria[0].lower = areaMin;
			imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);

			//Send particle count after filtering to dashboard
			numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			SmartDashboard.putNumber("Filtered particles", numParticles);

			if(numParticles > 0)
			{
				//Measure particles and sort by particle size
				Vector<ParticleReport> particles = new Vector<ParticleReport>();
				for(int particleIndex = 0; particleIndex < numParticles; particleIndex++)
				{
					ParticleReport par = new ParticleReport();
					par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
					par.Area = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
					par.BoundingRectTop = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					par.BoundingRectLeft = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					par.BoundingRectBottom = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
					par.BoundingRectRight = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
					particles.add(par);
				}
				particles.sort(null);

				//This example only scores the largest particle. Extending to score all particles and choosing the desired one is left as an exercise
				//for the reader. Note that this scores and reports information about a single particle (single L shaped target). To get accurate information 
				//about the location of the tote (not just the distance) you will need to correlate two adjacent targets in order to find the true center of the tote.
				scores.Aspect = AspectScore(particles.elementAt(0));
				SmartDashboard.putNumber("Aspect", scores.Aspect);
				scores.Area = AreaScore(particles.elementAt(0));
				SmartDashboard.putNumber("Area", scores.Area);
				isGoal = scores.Aspect > SCORE_MIN && scores.Area > SCORE_MIN;
				if(isGoal) {
					ParticleReport p = particles.elementAt(0);
					Rect rect = new Rect((int)p.BoundingRectLeft, (int)p.BoundingRectTop, (int)(p.BoundingRectRight - p.BoundingRectLeft), (int)(p.BoundingRectTop - p.BoundingRectBottom));
					NIVision.imaqDrawShapeOnImage(binaryFrame, binaryFrame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
				}
				
				//Send masked image to dashboard to assist in tweaking mask.
				CameraServer.getInstance().setImage(binaryFrame);

				//Send distance and tote status to dashboard. The bounding rect, particularly the horizontal center (left - right) may be useful for rotating/driving towards a tote
				SmartDashboard.putBoolean("IsGoal", isGoal);
				SmartDashboard.putNumber("Distance", computeDistance(binaryFrame, particles.elementAt(0)));
				
				double centerPosition = (particles.elementAt(0).BoundingRectRight - particles.elementAt(0).BoundingRectLeft) / 2.0;
				double frameCenter = NIVision.imaqGetImageSize(binaryFrame).width / 2.0;
				displacementFromCenter = frameCenter - centerPosition;
				SmartDashboard.putNumber("Delta from Center", displacementFromCenter);
				SmartDashboard.putNumber("Distance 2", computeDistance2(binaryFrame, particles.elementAt(0)));
			} else {
				SmartDashboard.putBoolean("IsGoal", false);
			}

			Timer.delay(0.005);				// wait for a motor update time
		}

		//Comparator function for sorting particles. Returns true if particle 1 is larger
		static boolean CompareParticleSizes(ParticleReport particle1, ParticleReport particle2)
		{
			//we want descending sort order
			return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
		}

		/**
		 * Converts a ratio with ideal value of 1 to a score. The resulting function is piecewise
		 * linear going from (0,0) to (1,100) to (2,0) and is 0 for all inputs outside the range 0-2
		 */
		double ratioToScore(double ratio)
		{
			return (Math.max(0, Math.min(100*(1-Math.abs(1-ratio)), 100)));
		}

		double AreaScore(ParticleReport report)
		{
			double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop) * (report.BoundingRectRight - report.BoundingRectLeft);
			//Tape is 7" edge so 49" bounding rect. With 2" wide tape it covers 24" of the rect.
			return ratioToScore((49/24)*report.Area/boundingArea);
		}

		/**
		 * Method to score if the aspect ratio of the particle appears to match the retro-reflective target. Target is 7"x7" so aspect should be 1
		 */
		double AspectScore(ParticleReport report)
		{
			return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop)));
		}

		/**
		 * Computes the estimated distance to a target using the width of the particle in the image. For more information and graphics
		 * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
		 *
		 * @param image The image to use for measuring the particle estimated rectangle
		 * @param report The Particle Analysis Report for the particle
		 * @param isLong Boolean indicating if the target is believed to be the long side of a tote
		 * @return The estimated distance to the target in feet.
		 */
		double computeDistance (Image image, ParticleReport report) {
			double normalizedWidth, targetWidth;
			NIVision.GetImageSizeResult size;

			size = NIVision.imaqGetImageSize(image);
			normalizedWidth = 2*(report.BoundingRectRight - report.BoundingRectLeft)/size.width;
			targetWidth = 20;

			return  targetWidth/(normalizedWidth/*12*/*Math.tan(VIEW_ANGLE*Math.PI/(180/*2*/)));
		}
		
		double computeDistance2(Image image, ParticleReport report) {
			double focalLength = 0.3;
			//double iWidth = report.BoundingRectRight - report.BoundingRectLeft;
			//double iHeight = report.BoundingRectTop - report.BoundingRectBottom;
			double aHeight = 14;
			//double aWidth = 20;
			double z = 90;
			double alpha = Math.asin((2 * z * (report.BoundingRectTop - report.BoundingRectBottom)) / (focalLength * aHeight)) / 2.0;
			return z / Math.tan(alpha);
		}

		protected void initDefaultCommand() {
			setDefaultCommand(new VisionStandard());
		}

		@Override
		protected double returnPIDInput() {
			return isGoal ? displacementFromCenter : 0.0;
		}

		@Override
		protected void usePIDOutput(double output) {
			motorComp = output;
		}
		
		public double getMotorComp() {
			return motorComp;
		}
}
