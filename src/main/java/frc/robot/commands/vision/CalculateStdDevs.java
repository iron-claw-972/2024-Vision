package frc.robot.commands.vision;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.Constants;
import frc.robot.util.LogManager;
import frc.robot.util.MathUtils;
import frc.robot.util.Vision;

/**
 * Calculates standard deviations for vision
 */
public class CalculateStdDevs extends CommandBase {
  private final Vision m_vision;
  private ArrayList<Pose2d> m_poses;
  private int m_arrayLength;
  private Timer m_endTimer;

  /**
   * Constructor for CalculateStdDevs
   * @param posesToUse the amount of poses to take the standard deviation of. More poses will take more time.
   * @param vision The vision
   */
  public CalculateStdDevs(int posesToUse, Vision vision) {
    m_vision = vision;
    m_arrayLength = posesToUse;
    m_endTimer = new Timer();
  }

  /**
   * Resets the pose array
   */
  @Override
  public void initialize() {
    // create the ArrayList of poses to store
    // an ArrayList prevents issues if the command ends early, and makes checking if the command has finished easy
    m_poses = new ArrayList<Pose2d>();
  }

  /**
   * Adds a pose to the array
   */
  @Override
  public void execute() {
    Pose2d pose = m_vision.getPose2d(true);
    // If the pose exists, add it to the first open spot in the array
    if (pose != null) {
      // if we see a pose, reset the timer (it will be started the next time it doesn't get a pose)
      m_endTimer.stop();
      m_endTimer.reset();
      // add the pose to our data
      m_poses.add(pose);
      System.out.printf("%.1f%% done\n", ((double)m_poses.size())/m_arrayLength * 100);
    } else {
      m_endTimer.start();
      // If kStdDevCommandEndTime seconds have passed since it saw an April tag, stop the command
      // Prevents it from running forever
      if (m_endTimer.hasElapsed(10)) {
        cancel();
      }
    }
  }

  /**
   * Calculates the standard deviation
   */
  @Override
  public void end(boolean interrupted) {
    // If the array is empty, don't try to calculate std devs
    if (m_poses.size() == 0) {
      System.out.println("There are no poses in the array\nTry again where the robot can see an April tag.");
      return;
    }
    
    // create arrays of the poses by X, Y, and Rotation for calculations
    double[] xArray = new double[m_poses.size()];
    double[] yArray = new double[m_poses.size()];
    double[] rotArray = new double[m_poses.size()];

    // copy the values into the arrays
    for (int i = 0; i < m_poses.size(); i++) {
      xArray[i] = m_poses.get(i).getX();
      yArray[i] = m_poses.get(i).getY();
      rotArray[i] = m_poses.get(i).getRotation().getRadians();
    }

    // Calculate the standard deviations
    double stdDevX = MathUtils.stdDev(xArray);
    double stdDevY = MathUtils.stdDev(yArray);
    double stdDevRot = MathUtils.stdDev(rotArray);
    
    // Find area
    double area = m_vision.getTargetAreaPercentage();
    
    // Print and log values
    System.out.printf("Standard deviation values:\nX: %.5f\nY: %.5f\nRotation: %.5f\nDistance: %.5f\n",
      stdDevX, stdDevY, stdDevRot, area);
    if (Constants.kLogging) {
      LogManager.addDouble("Vision/StdDevTest/StdDevX", stdDevX);
      LogManager.addDouble("Vision/StdDevTest/StdDevY", stdDevY);
      LogManager.addDouble("Vision/StdDevTest/StdDevRotation", stdDevRot);
      LogManager.addDouble("Vision/StdDevTest/TargetArea", area);
    }    
  }

  /**
   * Returns if the command is finished
   * @return If the array is full
   */
  @Override
  public boolean isFinished() {
    return m_poses.size() >= m_arrayLength;
  }
}