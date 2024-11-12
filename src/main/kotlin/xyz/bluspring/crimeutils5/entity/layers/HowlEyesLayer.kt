package xyz.bluspring.crimeutils5.entity.layers

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.animal.Wolf
import xyz.bluspring.crimeutils5.entity.HowlModel

class HowlEyesLayer(renderLayerParent: RenderLayerParent<Wolf, HowlModel<Wolf>>) : EyesLayer<Wolf, HowlModel<Wolf>>(renderLayerParent) {
    override fun renderType(): RenderType {
        return RENDER_TYPE
    }

    companion object {
        private val RENDER_TYPE = RenderType.eyes(ResourceLocation.fromNamespaceAndPath("crimecraft", "textures/entity/howl_eyes.png"))
    }
}