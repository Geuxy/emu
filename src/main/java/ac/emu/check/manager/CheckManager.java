package ac.emu.check.manager;

import ac.emu.check.Check;
import ac.emu.check.impl.hitbox.HitboxA;
import ac.emu.check.impl.protocol.*;
import ac.emu.check.impl.climb.ClimbA;
import ac.emu.check.impl.climb.ClimbB;
import ac.emu.check.impl.fly.FlyA;
import ac.emu.check.impl.fly.FlyB;
import ac.emu.check.impl.ground.GroundA;
import ac.emu.check.impl.jesus.JesusA;
import ac.emu.check.impl.jesus.JesusB;
import ac.emu.check.impl.jump.JumpA;
import ac.emu.check.impl.jump.JumpB;
import ac.emu.check.impl.move.MoveA;
import ac.emu.check.impl.move.MoveB;
import ac.emu.check.impl.noslow.NoSlowA;
import ac.emu.check.impl.speed.SpeedA;
import ac.emu.check.impl.speed.SpeedB;
import ac.emu.check.impl.speed.SpeedC;
import ac.emu.check.impl.step.StepA;
import ac.emu.check.impl.step.StepB;
import ac.emu.check.impl.step.StepC;
import ac.emu.check.impl.strafe.StrafeA;
import ac.emu.check.impl.timer.TimerA;
import ac.emu.check.impl.velocity.VelocityA;
import ac.emu.check.impl.velocity.VelocityB;
import ac.emu.user.EmuPlayer;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckManager {

    private final List<Class<?>> CHECKS = new ArrayList<>();

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public void onInit() {
        this.addChecks(
            //HitboxA.class,

            VelocityA.class,
            VelocityB.class,

            FlyA.class,
            FlyB.class,

            JumpA.class,
            JumpB.class,

            SpeedA.class,
            SpeedB.class,
            SpeedC.class,

            MoveA.class,
            MoveB.class,

            StrafeA.class,

            JesusA.class,
            JesusB.class,

            ClimbB.class,
            ClimbA.class,

            NoSlowA.class,

            StepA.class,
            StepB.class,
            StepC.class,

            ProtocolA.class,
            ProtocolB.class,
            ProtocolC.class,
            ProtocolD.class,
            ProtocolE.class,
            ProtocolF.class,
            //ProtocolG.class,
            ProtocolH.class,
            //ProtocolI.class,

            TimerA.class,

            GroundA.class
        );

        for(Class<?> clazz : CHECKS) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(EmuPlayer.class));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Check> loadChecks(EmuPlayer data) {
        List<Check> checkList = new ArrayList<>();

        for(Constructor<?> constructor : CONSTRUCTORS) {
            try {
                Check check = (Check) constructor.newInstance(data);

                if(check.isEnabled()) {
                    checkList.add(check);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return checkList;
    }

    private void addChecks(Class<?>... classes) {
        this.CHECKS.addAll(Arrays.asList(classes));
    }

}
