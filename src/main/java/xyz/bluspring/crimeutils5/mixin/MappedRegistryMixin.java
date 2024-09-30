package xyz.bluspring.crimeutils5.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.crimeutils5.Workarounds;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
    @Inject(method = "register", at = @At("TAIL"))
    private void storeIdDump(ResourceKey<T> key, T value, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder.Reference<T>> cir, @Local int id) {
        Workarounds.INSTANCE.getFuckThatsAwful().put(id, key);
    }
}
