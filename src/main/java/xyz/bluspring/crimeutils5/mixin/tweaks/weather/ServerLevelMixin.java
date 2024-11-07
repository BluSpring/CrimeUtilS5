package xyz.bluspring.crimeutils5.mixin.tweaks.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Shadow @Final @Mutable
    public static IntProvider RAIN_DELAY;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void increaseRainFrequency(CallbackInfo ci) {
        RAIN_DELAY = UniformInt.of(6_000, 48_000); // between 0.25 to 2 in-game days
    }
}
