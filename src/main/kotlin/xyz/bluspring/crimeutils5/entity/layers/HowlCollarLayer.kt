package xyz.bluspring.crimeutils5.entity.layers


import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.animal.Wolf
import xyz.bluspring.crimeutils5.entity.HowlModel

class HowlCollarLayer(renderLayerParent: RenderLayerParent<Wolf, HowlModel<Wolf>>) : RenderLayer<Wolf, HowlModel<Wolf>>(renderLayerParent) {
    override fun render(poseStack: PoseStack, multiBufferSource: MultiBufferSource, i: Int, wolf: Wolf,
                        f: Float, g: Float, h: Float,
                        j: Float, k: Float, l: Float
    ) {
        if (wolf.isTame && !wolf.isInvisible) {
            renderColoredCutoutModel(
                this.parentModel, WOLF_COLLAR_LOCATION, poseStack, multiBufferSource, i, wolf,
                wolf.collarColor.textureDiffuseColor
            )
        }
    }

    companion object {
        private val WOLF_COLLAR_LOCATION = ResourceLocation.fromNamespaceAndPath("crimecraft", "textures/entity/howl_collar.png")
    }
}