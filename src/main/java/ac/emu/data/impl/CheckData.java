package ac.emu.data.impl;

import ac.emu.check.Check;
import ac.emu.check.impl.hitbox.HitboxA;
import ac.emu.check.impl.inventory.InventoryA;
import ac.emu.check.impl.killaura.KillauraA;
import ac.emu.check.impl.killaura.KillauraB;
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
import ac.emu.check.impl.strafe.StrafeA;
import ac.emu.check.impl.timer.TimerA;
import ac.emu.check.impl.timer.TimerB;
import ac.emu.check.impl.velocity.VelocityA;
import ac.emu.check.impl.velocity.VelocityB;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CheckData {

    @Getter
    private final List<Check> checks = new ArrayList<>();

    public CheckData(EmuPlayer profile) {
        this.register(profile,
            HitboxA::new,

            KillauraA::new,
            KillauraB::new,

            VelocityA::new,
            VelocityB::new,

            FlyA::new,
            FlyB::new,

            JumpA::new,
            JumpB::new,

            SpeedA::new,
            SpeedB::new,
            SpeedC::new,

            InventoryA::new,

            MoveA::new,
            MoveB::new,

            StrafeA::new,

            JesusA::new,
            JesusB::new,

            ClimbB::new,
            ClimbA::new,

            NoSlowA::new,

            StepA::new,
            StepB::new,

            ProtocolA::new,
            ProtocolB::new,
            ProtocolC::new,
            ProtocolD::new,
            ProtocolE::new,
            ProtocolF::new,
            ProtocolG::new,

            TimerA::new,
            TimerB::new,

            GroundA::new
        );

        checks.removeIf(c -> !c.isEnabled());
    }

    public void handle(Packet packet) {
        checks.forEach(c -> c.handle(packet));
    }

    @SafeVarargs
    private final void register(EmuPlayer profile, Function<EmuPlayer, Check>... functions) {
        Stream.of(functions).forEach(f -> this.checks.add(f.apply(profile)));
    }

}
