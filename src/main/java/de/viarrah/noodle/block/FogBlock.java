package de.viarrah.noodle.block;


import com.mojang.serialization.MapCodec;
import de.viarrah.noodle.blockentity.FogBlockEntity;
import de.viarrah.noodle.network.OpenFogScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.RenderShape;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FogBlock extends BaseEntityBlock {

    public FogBlock(Properties properties) {
        super(properties);
    }

    private int currentColor = 0xFFFFFF;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FogBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide) {
            return createTickerHelper(
                    type,
                    ModBlockEntities.FOG_BLOCK_ENTITY.get(),
                    FogBlockEntity::tick
            );
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer
                && serverPlayer.hasPermissions(2)
                && level.getBlockEntity(pos) instanceof FogBlockEntity) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenFogScreen(pos));
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
