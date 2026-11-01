package de.viarrah.noodle.fog.fogBlock;


import de.viarrah.noodle.blockentity.fogBlock.FogBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class FogSourceRegistry {

    private static final Map<Level, Map<Long, FogSource>> SOURCES =
            new WeakHashMap<>();

    private static final Map<Level, Map<Long, FogSource>> PREVIEWS =
            new WeakHashMap<>();

    private FogSourceRegistry() {}

    public static void update(Level level, FogSource source) {
        SOURCES.computeIfAbsent(level, ignored -> new HashMap<>())
                .put(source.pos().asLong(), source);
    }

    public static void updatePreview(Level level, FogSource fogSource) {
        PREVIEWS.computeIfAbsent(level, ignored -> new HashMap<>())
                .put(fogSource.pos().asLong(), fogSource);
    }

    public static void remove(Level level, BlockPos pos) {
        Map<Long, FogSource> sources = SOURCES.get(level);

        if (sources != null) {

            sources.remove(pos.asLong());

            if (sources.isEmpty()) {
                SOURCES.remove(level);
            }

        }

        removePreview(level, pos);
    }

    public static void removePreview(Level level, BlockPos pos) {
        Map<Long, FogSource> previews = PREVIEWS.get(level);

        if (previews == null) return;

        previews.remove(pos.asLong());

        if (previews.isEmpty()) {
            PREVIEWS.remove(level);
        }
    }

    public static List<FogSource> getSources(Level level) {
        Map<Long, FogSource> sources = SOURCES.get(level);
        Map<Long, FogSource> previews = PREVIEWS.get(level);

        if (sources == null && previews == null) return List.of();

        Map<Long, FogSource> combined = new HashMap<>();

        if (sources != null) {
            combined.putAll(sources);
        }
        if (previews != null) {
            combined.putAll(previews);
        }

        return List.copyOf(combined.values());
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
            FogBlockEntity.ActivationMode activationMode,
            long lastSeenGameTime

    ) {}
}
