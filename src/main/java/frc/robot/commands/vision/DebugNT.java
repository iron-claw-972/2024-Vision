package frc.robot.commands.vision;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.miscConstants.VisionConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.DetectedObject;
import frc.robot.util.Vision;

/**
 * Adds data from object detection vision to SmartDashboard
 */
public class DebugNT extends CommandBase{
  private final Drivetrain m_drive;
  private DoublePublisher xPub;
  private DoublePublisher yPub;
  private DoubleSubscriber driveAngleSub; 

  private double x; 
  private double y; 
  private double xSpeed;
  private double ySpeed;
  private double driveAngle; 

  /**
   * Adds data from object detection vision to Smartdashboard
   * @param vision The vision
   */
  public DebugNT(Vision vision, Drivetrain drive){
    m_drive = drive; 

    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    NetworkTable table = inst.getTable("RobotPosition");

    xPub = table.getDoubleTopic("x").publish();
    yPub = table.getDoubleTopic("y").publish();
    driveAngleSub = table.getDoubleTopic("moveAngle").subscribe(0);
  }
  
  /**
   * Adds the data to SmartDashboard
   */
  @Override
  public void execute() {
    x = m_drive.getPose().getX(); 
    y = m_drive.getPose().getY();

    System.out.println("x: "+x); 
    System.out.println("y: "+ (-y)); 
    
    xPub.set(x);
    yPub.set(-y);

    driveAngle = driveAngleSub.get();
    xSpeed = 1 * Math.cos(Math.toRadians(-driveAngle));    
    ySpeed = 1 * Math.sin(Math.toRadians(-driveAngle));    

    //m_drive.drive(xSpeed,ySpeed,0,true,false); 
  }

  /**
   * Does nothing
   * @param interrupted If the command is interrupted
   */
  @Override
  public void end(boolean interrupted) {

  }

  /**
   * Returns if the command is finished
   * @retrun Always false (command never finishes)
   */
  @Override
  public boolean isFinished() {
    return false; 
  }


}

