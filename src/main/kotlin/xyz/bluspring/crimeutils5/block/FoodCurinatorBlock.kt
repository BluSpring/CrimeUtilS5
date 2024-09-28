package xyz.bluspring.crimeutils5.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class FoodCurinatorBlock : HorizontalDirectionalBlock(Properties.ofFullCopy(Blocks.CAULDRON)
    .lightLevel {
        if (it.getValue(FUELED))
            12
        else 0
    }
), EntityBlock {
    init {
        this.registerDefaultState(this.defaultBlockState()
            .setValue(FUELED, false)
            .setValue(FILLED, false)
        )
    }

    override fun codec(): MapCodec<out HorizontalDirectionalBlock>? {
        return null
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return this.defaultBlockState()
            .setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.block()
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return FoodCurinatorBlockEntity(pos, state)
    }

    override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider? {
        val blockEntity = level.getBlockEntity(pos)
        return if (blockEntity is MenuProvider) blockEntity else super.getMenuProvider(state, level, pos)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (!state.`is`(newState.block)) {
            val blockEntity = level.getBlockEntity(pos)

            if (blockEntity is FoodCurinatorBlockEntity && level is ServerLevel) {
                Containers.dropContents(level, pos, blockEntity)
            }

            super.onRemove(state, level, pos, newState, movedByPiston)
        }
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        } else {
            val blockEntity = level.getBlockEntity(pos)

            if (blockEntity is FoodCurinatorBlockEntity) {
                player.openMenu(blockEntity)
            }

            return InteractionResult.CONSUME
        }
    }

    override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { level, blockPos, blockState, blockEntity ->
            if (blockEntity is FoodCurinatorBlockEntity) {
                blockEntity.tick(level, blockPos, blockState)
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FILLED, FUELED, FACING)
    }

    companion object {
        val FILLED = BooleanProperty.create("filled")
        val FUELED = BooleanProperty.create("fueled")
    }
}