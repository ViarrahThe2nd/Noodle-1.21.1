package de.viarrah.noodle.network;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SaveFogSettings(
        BlockPos pos,
        float influenceDistance,
        float fogStart,
        float fogEnd,
        int fogColor,
        String influenceMode,
        float startPercentage,
        float endPercentage,
        String activationMode
) implements CustomPacketPayload {

    public static final Type<SaveFogSettings> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID,
                    "save_fog_settings"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, SaveFogSettings> STREAM_CODEC =
            StreamCodec.ofMember(
                    SaveFogSettings::write,
                    SaveFogSettings::new
            );

    private SaveFogSettings(RegistryFriendlyByteBuf buffer) {
        this(
                buffer.readBlockPos(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readInt(),
                buffer.readUtf(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readUtf()
        );
    }

    private void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeFloat(influenceDistance);
        buffer.writeFloat(fogStart);
        buffer.writeFloat(fogEnd);
        buffer.writeInt(fogColor);
        buffer.writeUtf(influenceMode);
        buffer.writeFloat(startPercentage);
        buffer.writeFloat(endPercentage);
        buffer.writeUtf(activationMode);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(
            SaveFogSettings message,
            net.neoforged.neoforge.network.handling.IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof net.minecraft.server.level.ServerPlayer player)) {
                return;
            }

            if (!player.hasPermissions(2)) {
                return;
            }

            // Only accept edits from a player near the block.
            if (player.distanceToSqr(message.pos().getX() + 0.5,
                    message.pos().getY() + 0.5,
                    message.pos().getZ() + 0.5) > 64 * 64) {
                return;
            }

            if (!(player.level().getBlockEntity(message.pos())
                    instanceof de.viarrah.noodle.blockentity.FogBlockEntity fog)) {
                return;
            }

            try {
                var influenceMode =
                        de.viarrah.noodle.blockentity.FogBlockEntity.InfluenceMode
                                .valueOf(message.influenceMode());

                var activationMode =
                        de.viarrah.noodle.blockentity.FogBlockEntity.ActivationMode
                                .valueOf(message.activationMode());

                fog.setFogSettings(
                        message.influenceDistance(),
                        message.fogStart(),
                        message.fogEnd(),
                        message.fogColor(),
                        influenceMode,
                        message.startPercentage(),
                        message.endPercentage(),
                        activationMode
                );
            } catch (IllegalArgumentException exception) {
                // Ignore a message containing an unknown mode name.
            }
        });
    }
}
