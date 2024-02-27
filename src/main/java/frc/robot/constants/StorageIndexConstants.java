package frc.robot.constants;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.IdleMode;

public class StorageIndexConstants {
    public static final int indexMotorID = 7;
    public static final int indexBeamBreak = 0; // no clue what we're using, but this is what windup's doing
    public static final NeutralModeValue indexNeutralMode = NeutralModeValue.Brake;

    public static final int stallLimit = 25;

    public static final double intakeSpeed = 1;
    public static final double ejectShootSpeed = 1;
    public static final double ejectAmpFrontSpeed = 1;
    public static final double ejectAmpBackSpeed = 1;
    public static final double ejectTrapSpeed = 1;

    public static final double ejectShootTimeout = 1;
    public static final double ejectAmpFrontTimeout = 1;
    public static final double ejectAmpBackTimeout = 1;

    public static final IdleMode idleMode = IdleMode.kBrake;
}
