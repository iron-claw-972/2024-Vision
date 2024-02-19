// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.controls;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.constants.Constants;
import frc.robot.subsystems.gpm.Intake;
import frc.robot.subsystems.gpm.Intake.Mode;
import frc.robot.util.MathUtils;
import lib.controllers.GameController;
import lib.controllers.GameController.Axis;
import lib.controllers.GameController.Button;

/** Add your docs here. */
public class Operater{
    
    private final GameController kDriver = new GameController(Constants.OPERATOR_JOY);
    
    Intake intake;
    
    public Operater(Intake intake){
        this.intake = intake;
    }

    public void configureControls(){
        kDriver.get(Button.X).onTrue(new InstantCommand(()->intake.setMode(Mode.INTAKE)));
        kDriver.get(Button.X).onFalse(new InstantCommand(()->intake.setMode(Mode.DISABLED)));
        kDriver.get(Button.B).onTrue(new InstantCommand(()->intake.setMode(Mode.REVERSE)));
        kDriver.get(Button.B).onFalse(new InstantCommand(()->intake.setMode(Mode.DISABLED)));
    }
  
  public double getRawForwardTranslation() {
    return kDriver.get(Axis.LEFT_Y);
  }

  public double getRawSideTranslation() {
    return kDriver.get(Axis.LEFT_X);
  }

  public double getRawRotation() {
    return kDriver.get(Axis.RIGHT_X);
  }

  public double getRawHeadingAngle() {
    return Math.atan2(kDriver.get(Axis.RIGHT_X), -kDriver.get(Axis.RIGHT_Y)) - Math.PI / 2;
  }

  public double getRawHeadingMagnitude() {
    return MathUtils.calculateHypotenuse(kDriver.get(Axis.RIGHT_X), kDriver.get(Axis.RIGHT_Y));
  }

  public boolean getIsSlowMode() {
    return kDriver.RIGHT_TRIGGER_BUTTON.getAsBoolean();
  }

  public boolean getIsAlign() {
    return kDriver.LEFT_TRIGGER_BUTTON.getAsBoolean();
  }
}
