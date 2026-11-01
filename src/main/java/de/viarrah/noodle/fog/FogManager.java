package de.viarrah.noodle.fog;


import de.viarrah.noodle.blockentity.FogBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public final class FogManager {

    private static final long TRANSITION_DURATION = 500_000_000L; // 0.5 seconds

    private static ClientLevel currentLevel;

    private static FogState transitionFrom = FogState.NONE;
    private static FogState transitionTo = FogState.NONE;
    private static FogState lastTarget;

    private static long transitionStart;

    private FogManager() {}

    public static void tick(ClientLevel level, LocalPlayer player) {

        long now = System.nanoTime();

        // Reset when changing dimensions/worlds.
        if (currentLevel != level) {
            reset();
            currentLevel = level;
        }

        FogState current = getCurrentState(now);
        FogState target = calculateTarget(level, player);

        // Only restart the transition if the target actually changed.
        if (lastTarget == null || !approximatelyEqual(target, lastTarget)) {

            transitionFrom = current;
            transitionTo = target;

            transitionStart = now;
            lastTarget = target;
        }
    }

    private static FogState calculateTarget(
            ClientLevel level,
            LocalPlayer player
    ) {

        double sumWeight = 0.0;

        double weightedStart = 0.0;
        double weightedEnd = 0.0;

        double weightedR = 0.0;
        double weightedG = 0.0;
        double weightedB = 0.0;

        double remainingInfluence = 1.0;

        for (FogSourceRegistry.FogSource source :
                FogSourceRegistry.getSources(level)) {

            float radius = source.influenceDistance();

            if (radius <= 0.0f) continue;

            BlockPos pos = source.pos();

            double dx = player.getX() - (pos.getX() + 0.5);
            double dy = player.getY() - (pos.getY() + 0.5);
            double dz = player.getZ() - (pos.getZ() + 0.5);

            double distanceSquared = dx * dx + dy * dy + dz * dz;

            if (distanceSquared > radius * radius) continue;

            float distance = (float) Math.sqrt(distanceSquared);

            float influence;

            if (source.influenceMode() ==
                    FogBlockEntity.InfluenceMode.FULL) {

                // Full influence inside the radius.
                influence = 1.0f;

            } else {

                // Spatial interpolation.
                float t = 1.0f - distance / radius;
                t = Mth.clamp(t, 0.0f, 1.0f);

                // Smoothstep.
                t = t * t * (3.0f - 2.0f * t);

                influence = Mth.lerp(
                        t,
                        source.startPercentage(),
                        source.endPercentage()
                );
            }

            influence = Mth.clamp(influence, 0.0f, 1.0f);

            if (influence <= 0.0f) continue;

            sumWeight += influence;

            remainingInfluence *= (1.0 - influence);

            weightedStart += source.fogStart() * influence;
            weightedEnd += source.fogEnd() * influence;

            int color = source.fogColor();

            float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;

            weightedR += r * influence;
            weightedG += g * influence;
            weightedB += b * influence;
        }

        if (sumWeight <= 0.000001) {
            return FogState.NONE;
        }

        float combinedInfluence =
                (float) (1.0 - remainingInfluence);

        // Normalize the weighted fog properties.
        float fogStart = (float) (weightedStart / sumWeight);
        float fogEnd = (float) (weightedEnd / sumWeight);

        float r = (float) (weightedR / sumWeight);
        float g = (float) (weightedG / sumWeight);
        float b = (float) (weightedB / sumWeight);

        return new FogState(
                combinedInfluence,
                fogStart,
                fogEnd,
                r,
                g,
                b
        );
    }

    // Returns the current temporally interpolated fog state.
    public static FogState getCurrentState() {
        return getCurrentState(System.nanoTime());
    }

    private static FogState getCurrentState(long now) {

        float t = Mth.clamp(
                (float) (now - transitionStart) / TRANSITION_DURATION,
                0.0f,
                1.0f
        );

        // Temporal smoothstep.
        t = t * t * (3.0f - 2.0f * t);

        return interpolate(transitionFrom, transitionTo, t);
    }

    private static FogState interpolate(
            FogState a,
            FogState b,
            float t
    ) {
        return new FogState(
                Mth.lerp(t, a.influence(), b.influence()),
                Mth.lerp(t, a.fogStart(), b.fogStart()),
                Mth.lerp(t, a.fogEnd(), b.fogEnd()),
                Mth.lerp(t, a.r(), b.r()),
                Mth.lerp(t, a.g(), b.g()),
                Mth.lerp(t, a.b(), b.b())
        );
    }

    private static boolean approximatelyEqual(FogState a, FogState b) {

        return Math.abs(a.influence() - b.influence()) < 0.0001f
                && Math.abs(a.fogStart() - b.fogStart()) < 0.001f
                && Math.abs(a.fogEnd() - b.fogEnd()) < 0.001f
                && Math.abs(a.r() - b.r()) < 0.0001f
                && Math.abs(a.g() - b.g()) < 0.0001f
                && Math.abs(a.b() - b.b()) < 0.0001f;
    }

    public static void reset() {

        currentLevel = null;

        transitionFrom = FogState.NONE;
        transitionTo = FogState.NONE;
        lastTarget = null;

        transitionStart = System.nanoTime();
    }

    public record FogState(
            float influence,
            float fogStart,
            float fogEnd,
            float r,
            float g,
            float b
    ) {
        private static final FogState NONE =
                new FogState(0.0f, 0.0f, 64.0f, 0.0f, 0.0f, 0.0f);
    }
}
