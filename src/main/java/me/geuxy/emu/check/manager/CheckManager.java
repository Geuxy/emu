package me.geuxy.emu.check.manager;

import me.geuxy.emu.check.impl.combat.killaura.KillAuraA;
import me.geuxy.emu.check.impl.combat.velocity.VelocityA;
import me.geuxy.emu.check.impl.move.fly.*;
import me.geuxy.emu.check.impl.move.speed.*;
import me.geuxy.emu.check.impl.move.step.*;
import me.geuxy.emu.check.impl.packet.badpackets.*;
import me.geuxy.emu.check.impl.packet.timer.*;
import me.geuxy.emu.check.impl.player.post.*;
import me.geuxy.emu.check.impl.player.ground.*;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.PlayerData;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckManager {

    private final List<Class<?>> CHECKS = new ArrayList<>();

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public void onInit() {
        this.addChecks(
            KillAuraA.class,

            //VelocityA.class,

            FlyA.class,
            FlyB.class,
            FlyC.class,
            FlyD.class,

            SpeedA.class,
            SpeedB.class,
            SpeedC.class,
            SpeedD.class,
            SpeedE.class,
            SpeedF.class,
            SpeedG.class,

            StepA.class,
            StepB.class,

            BadPacketsA.class,

            TimerA.class,
            TimerB.class,

            PostA.class,
            GroundA.class
        );

        for(Class<?> clazz : CHECKS) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<AbstractCheck> loadChecks(PlayerData data) {
        List<AbstractCheck> checkList = new ArrayList<>();

        for(Constructor<?> constructor : CONSTRUCTORS) {
            try {
                AbstractCheck check = (AbstractCheck) constructor.newInstance(data);

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
