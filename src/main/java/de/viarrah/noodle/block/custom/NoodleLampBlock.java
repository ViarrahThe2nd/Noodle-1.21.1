package de.viarrah.noodle.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class NoodleLampBlock extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public NoodleLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            boolean currentState = state.getValue(ACTIVE);
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, !currentState));

            // adjacent blocks shall also be changed
            Direction[] directions = Direction.values();

            for (Direction dir : directions) {
                BlockPos neighborPos = pos.offset(dir.getNormal());
                BlockState neighborState = level.getBlockState(neighborPos);

                if (neighborState.is(this)) {
                    boolean neighborActive = neighborState.getValue(ACTIVE);
                    BlockState newState = neighborState.setValue(ACTIVE, !neighborActive);

                    level.setBlockAndUpdate(neighborPos, newState);

                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
