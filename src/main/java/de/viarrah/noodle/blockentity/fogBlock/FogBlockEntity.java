package de.viarrah.noodle.blockentity.fogBlock;

import de.viarrah.noodle.block.ModBlockEntities;
import de.viarrah.noodle.fog.fogBlock.FogSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FogBlockEntity extends BlockEntity {

    public enum InfluenceMode {
        FULL,
        PERCENTAGE
    }

    public enum ActivationMode {
        ENABLED,
        DISABLED,
        REDSTONE
    }
    // whole lotta default values
    private ActivationMode activationMode = ActivationMode.ENABLED; //default value

    private float influenceDistance = 45.0f;

    private float fogStart = -5.0f;
    private float fogEnd = 90.0f;

    //RGB in HEX: 0xRRGGBB
    private int fogColor = 0xCCCCCC;

    private InfluenceMode influenceMode = InfluenceMode.PERCENTAGE; //default value

    private float startPercentage = 0.0f;
    private float endPercentage = 1.0f;

    public FogBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FOG_BLOCK_ENTITY.get(), pos, state);
    }

    // per tick
    public static void tick(Level level, BlockPos pos,
                            BlockState state, FogBlockEntity blockEntity) {
        if (!level.isClientSide) return;

        FogSourceRegistry.update(level, blockEntity.createSource(level.getGameTime()));
    }

    private FogSourceRegistry.FogSource createSource(long gameTime) {
        return new FogSourceRegistry.FogSource(
                worldPosition.immutable(),
                influenceDistance,
                fogStart,
                fogEnd,
                fogColor,
                influenceMode,
                startPercentage,
                endPercentage,
                activationMode,
                gameTime
        );
    }

    // Configure this block. Call on the SERVER.
    public void setFogSettings(
            float influenceDistance,
            float fogStart,
            float fogEnd,
            int fogColor,
            InfluenceMode mode,
            float startPercentage,
            float endPercentage,
            ActivationMode activationMode
    ) {
        this.influenceDistance = Math.max(0.01f, influenceDistance);
        this.activationMode = activationMode;

        this.fogStart = fogStart;
        this.fogEnd = Math.max(fogStart + 0.01f, fogEnd);

        this.fogColor = fogColor;

        this.influenceMode = mode;

        this.startPercentage = Mth.clamp(startPercentage, 0.0f, 1.0f);
        this.endPercentage = Mth.clamp(endPercentage, 0.0f, 1.0f);

        setChanged();

        Level level = getLevel();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    3
            );
        }
    }

    // Getters
    public float getInfluenceDistance() { return influenceDistance; }
    public float getFogStart() { return fogStart; }
    public float getFogEnd() { return fogEnd; }
    public int getFogColor() { return fogColor; }
    public InfluenceMode getInfluenceMode() { return influenceMode; }
    public ActivationMode getActivationMode() { return activationMode; }
    public float getStartPercentage() { return startPercentage; }
    public float getEndPercentage() { return endPercentage; }

    // NBT saving
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putFloat("InfluenceDistance", influenceDistance);
        tag.putFloat("FogStart", fogStart);
        tag.putFloat("FogEnd", fogEnd);
        tag.putInt("FogColor", fogColor);

        tag.putString("InfluenceMode", influenceMode.name());

        tag.putFloat("StartPercentage", startPercentage);
        tag.putFloat("EndPercentage", endPercentage);
        tag.putString("ActivationMode", activationMode.name());
    }

    // NBT loading
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        influenceDistance = Math.max(0.01f, tag.getFloat("InfluenceDistance"));

        fogStart = tag.getFloat("FogStart");
        fogEnd = Math.max(fogStart + 0.01f, tag.getFloat("FogEnd"));

        fogColor = tag.getInt("FogColor");

        try {
            influenceMode = InfluenceMode.valueOf(tag.getString("InfluenceMode"));
        } catch (IllegalArgumentException exception) {
            influenceMode = InfluenceMode.PERCENTAGE;
        }

        startPercentage = Mth.clamp(tag.getFloat("StartPercentage"), 0.0f, 1.0f);
        endPercentage = Mth.clamp(tag.getFloat("EndPercentage"), 0.0f, 1.0f);

        try {
            activationMode = ActivationMode.valueOf(tag.getString("ActivationMode"));
        } catch (IllegalArgumentException exception) {
            activationMode = ActivationMode.ENABLED;
        }
    }

    // Synchronize when a chunk loads.
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    // Synchronize when block settings change.
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setRemoved() {
        if (getLevel() != null) {
            FogSourceRegistry.remove(getLevel(), worldPosition);
        }

        super.setRemoved();
    }
}

