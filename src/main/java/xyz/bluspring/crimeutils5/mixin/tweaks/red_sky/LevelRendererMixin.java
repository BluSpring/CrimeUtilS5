package xyz.bluspring.crimeutils5.mixin.tweaks.red_sky;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Unique private static final Vec3 RED_COLOR = new Vec3(0.59216, 0.0, 0.0);

    @WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 useRedSkyColor(ClientLevel instance, Vec3 pos, float partialTick, Operation<Vec3> original) {
        if (!instance.dimension().equals(Level.OVERWORLD) || !CrimeUtilS5.Companion.getUseRedSky())
            return original.call(instance, pos, partialTick);

        return RED_COLOR;
    }
}
