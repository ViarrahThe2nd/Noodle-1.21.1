package de.viarrah.noodle.fog;


import de.viarrah.noodle.blockentity.FogBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class FogSourceRegistry {

    private static final Map<Level, Map<Long, FogSource>> SOURCES =
            new WeakHashMap<>();

    private FogSourceRegistry() {}

    public static void update(Level level, FogSource source) {
        SOURCES.computeIfAbsent(level, ignored -> new HashMap<>())
                .put(source.pos().asLong(), source);
    }

    public static void remove(Level level, BlockPos pos) {
        Map<Long, FogSource> sources = SOURCES.get(level);

        if (sources == null) return;

        sources.remove(pos.asLong());

        if (sources.isEmpty()) {
            SOURCES.remove(level);
        }
    }

    public static List<FogSource> getSources(Level level) {
        Map<Long, FogSource> sources = SOURCES.get(level);

        if (sources == null) return List.of();

        return List.copyOf(sources.values());
    }

    public static void prune(Level level, long gameTime) {
        Map<Long, FogSource> sources = SOURCES.get(level);

        if (sources == null) return;

        sources.values().removeIf(
                source -> gameTime - source.lastSeenGameTime() > 2
        );

        if (sources.isEmpty()) {
            SOURCES.remove(level);
        }
    }

    public record FogSource(
            BlockPos pos,
            float influenceDistance,
            float fogStart,
            float fogEnd,
            int fogColor,
            FogBlockEntity.InfluenceMode influenceMode,
            float startPercentage,
            float endPercentage,
            long lastSeenGameTime
    ) {}
}
