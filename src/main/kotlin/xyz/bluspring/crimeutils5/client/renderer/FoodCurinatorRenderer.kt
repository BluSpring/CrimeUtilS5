package xyz.bluspring.crimeutils5.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import xyz.bluspring.crimeutils5.CrimeUtilS5
import xyz.bluspring.crimeutils5.block.FoodCurinatorBlockEntity

class FoodCurinatorRenderer(val ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<FoodCurinatorBlockEntity> {
    override fun render(
        blockEntity: FoodCurinatorBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val state = blockEntity.blockState

        if (state.`is`(CrimeUtilS5.FOOD_CURINATOR)) {
            val inputStack = blockEntity.getItem(0)
            val outputStack = blockEntity.getItem(4)
            val facing = state.getValue(HorizontalDirectionalBlock.FACING)

            val offset = if (facing == Direction.EAST || facing == Direction.NORTH)
                -1.25f
            else .3f

            val direction = if (facing == Direction.WEST || facing == Direction.NORTH) -1 else 1

            poseStack.pushPose()
            poseStack.mulPose(facing.rotation)
            poseStack.scale(0.65f, 0.65f, 0.65f)
            poseStack.translate(offset, 0.8f * direction, -2f)
            poseStack.mulPose(Axis.YP.rotationDegrees(90f))
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45f))

            if (!inputStack.isEmpty) {
                poseStack.pushPose()
                ctx.itemRenderer.renderStatic(inputStack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.level, 0)
                poseStack.popPose()
            }

            if (!outputStack.isEmpty) {
                poseStack.pushPose()
                poseStack.translate(0f, 0f, 1f)

                ctx.itemRenderer.renderStatic(outputStack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.level, 0)
                poseStack.popPose()
            }

            poseStack.popPose()
        }
    }
}