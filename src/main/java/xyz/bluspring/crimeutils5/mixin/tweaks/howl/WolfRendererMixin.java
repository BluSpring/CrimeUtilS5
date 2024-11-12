package xyz.bluspring.crimeutils5.mixin.tweaks.howl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.crimeutils5.entity.HowlRenderer;
import xyz.bluspring.crimeutils5.entity.HowlTheDog;

@Mixin(WolfRenderer.class)
public class WolfRendererMixin {
    private HowlRenderer howlRenderer;

    @Inject(method = "getBob(Lnet/minecraft/world/entity/animal/Wolf;F)F", at = @At("HEAD"), cancellable = true)
    public void deferBob(Wolf wolf, float f, CallbackInfoReturnable<Float> cir) {
        if (wolf.hasCustomName() && wolf.getCustomName().getString().equalsIgnoreCase(HowlTheDog.NAME)) {
            cir.setReturnValue(wolf.getTailAngle());
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDeferredRenderer(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.howlRenderer = new HowlRenderer(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/animal/Wolf;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    public void deferRenderIfNeeded(Wolf wolf, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (wolf.hasCustomName() && wolf.getCustomName().getString().equalsIgnoreCase(HowlTheDog.NAME)) {
            ci.cancel();
            this.howlRenderer.render(wolf, f, g, poseStack, multiBufferSource, i);
        }
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void deferTextureLocationIfNeeded(Wolf wolf, CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolf.hasCustomName() && wolf.getCustomName().getString().equalsIgnoreCase(HowlTheDog.NAME)) {
            cir.setReturnValue(this.howlRenderer.getTextureLocation(wolf));
        }
    }
}