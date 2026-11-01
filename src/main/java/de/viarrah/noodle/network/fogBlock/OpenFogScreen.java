package de.viarrah.noodle.network.fogBlock;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenFogScreen(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenFogScreen> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "open_fog_screen"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenFogScreen> STREAM_CODEC =
            StreamCodec.ofMember(OpenFogScreen::write, OpenFogScreen::new);

    private OpenFogScreen(RegistryFriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    private void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static BlockPos pendingPos;

    public static void handle(OpenFogScreen message, IPayloadContext context) {
        pendingPos = message.pos();
    }

    public static BlockPos consumePendingPos() {
        BlockPos pos = pendingPos;
        pendingPos = null;
        return pos;
    }
}