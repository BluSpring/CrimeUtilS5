package xyz.bluspring.crimeutils5.howl

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FastColor.ARGB32
import net.minecraft.world.entity.animal.Wolf
import xyz.bluspring.crimeutils5.client.CrimeUtilS5Client
import xyz.bluspring.crimeutils5.howl.layers.HowlCollarLayer
import xyz.bluspring.crimeutils5.howl.layers.HowlEyesLayer

class HowlRenderer(context: EntityRendererProvider.Context) : MobRenderer<Wolf, HowlModel<Wolf>>(context, HowlModel(context.bakeLayer(CrimeUtilS5Client.HOWL_LAYER)), 1.75F) {
    init {
        this.addLayer(HowlCollarLayer(this))
        this.addLayer(HowlEyesLayer(this))
    }

    override fun getTextureLocation(entity: Wolf): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath("crimecraft", "textures/entity/howl.png")
    }

    override fun getBob(wolf: Wolf, f: Float): Float {
        return wolf.tailAngle
    }

    override fun render(wolf: Wolf, yaw: Float, partialTicks: Float, poseStack: PoseStack, multiBufferSource: MultiBufferSource, packedLight: Int) {
        if (wolf.isWet) {
            val h = wolf.getWetShade(partialTicks)
            this.model.setColor(ARGB32.colorFromFloat(1f, h, h, h))
        }
        super.render(wolf, yaw, partialTicks, poseStack, multiBufferSource, packedLight)
        if (wolf.isWet) {
            this.model.setColor(-1)
        }
    }
}