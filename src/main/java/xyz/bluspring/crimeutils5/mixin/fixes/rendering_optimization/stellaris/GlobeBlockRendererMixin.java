package xyz.bluspring.crimeutils5.mixin.fixes.rendering_optimization.stellaris;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlobeBlockRenderer.class)
public class GlobeBlockRendererMixin {
    @WrapWithCondition(method = "render(Lcom/st0x0ef/stellaris/common/blocks/entities/GlobeBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
    private boolean whyWouldYouEverFuckingDoThat(MultiBufferSource.BufferSource instance) {
        return false;
    }
}
