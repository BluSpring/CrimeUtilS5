package xyz.bluspring.crimeutils5.mixin.tweaks.red_sky;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.client.CrimeUtilS5Client;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Unique
    private static final Vec3 RED_COLOR = new Vec3(0.59216, 0.0, 0.0);

    @WrapOperation(method = "method_24873", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;getBrightnessDependentFogColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 useRedSkyFogColor(DimensionSpecialEffects instance, Vec3 vec3, float v, Operation<Vec3> original) {
        if (!CrimeUtilS5Client.Companion.getUseRedSky())
            return original.call(instance, vec3, v);

        return RED_COLOR;
    }
}
