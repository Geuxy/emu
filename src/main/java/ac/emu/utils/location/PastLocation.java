package ac.emu.utils.location;

import ac.emu.utils.mcp.Vec3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor @AllArgsConstructor
public class PastLocation {

    private final double x, y, z;
    private float yaw, pitch;

    private final long timeStamp = System.currentTimeMillis();

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

}
