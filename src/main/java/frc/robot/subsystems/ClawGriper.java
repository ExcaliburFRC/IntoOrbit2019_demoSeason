package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.ClawGriper.*;

public class ClawGriper extends SubsystemBase {
    private final CANSparkMax clawMotor = new CANSparkMax(CLAW_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax RollerGripperMotor = new CANSparkMax(LEADER_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rollerGripperFollower = new CANSparkMax(FOLLOWER_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DigitalInput diskButton = new DigitalInput(DISK_BUTTON_PORT);
    private final DigitalInput ballBeambreak = new DigitalInput(BALL_BEAMBREAK_PORT);

    public Trigger diskDetectedTrigger = new Trigger(diskButton::get);
    public Trigger ballDetectedTrigger = new Trigger(ballBeambreak::get);

    public ClawGriper() {
        rollerGripperFollower.setInverted(true);
        rollerGripperFollower.follow(RollerGripperMotor);
        clawMotor.getEncoder();
    }

    public Command setRollersSpeed(double Speed) {
        return Commands.run(() -> RollerGripperMotor.set(Speed));
    }

    public Command intake() {
        return setRollersSpeed(0.2);
    }

    public Command outtake() {
        return setRollersSpeed(-0.2);
    }

    public Command openClaw() {
        return Commands.run(
                        () -> clawMotor.set(0.2))
                .until(() -> (clawMotor.getEncoder().getPosition()) > 110)
                .andThen(Commands.run(() -> clawMotor.set(0)));

    }

    public Command closeClaw() {
        return Commands.run(() -> clawMotor.set(-0.2))
                .until(() -> (clawMotor.getEncoder().getPosition()) < 25)
                .andThen(Commands.run(() -> clawMotor.set(0)));
    }
}
