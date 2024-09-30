package xyz.bluspring.crimeutils5.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.crimeutils5.Workarounds;

import java.util.HashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
    @Mutable
    @Shadow @Final private ObjectList<Holder.Reference<T>> byId;

    @Shadow @Nullable private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
    @Unique private Map<Holder.Reference<T>, Throwable> fuckOff = new HashMap<>();

    /*@Inject(method = "<init>(Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
    private void useForcedTracingMap(ResourceKey<T> key, Lifecycle registryLifecycle, boolean hasIntrusiveHolders, CallbackInfo ci) {
        this.byId = new Workarounds.ForcedTracedObjectMap<>(256);
    }*/

    @Inject(method = "register", at = @At("TAIL"))
    private void storeIdDump(ResourceKey<T> key, T value, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder.Reference<T>> cir, @Local int id) {
        Workarounds.INSTANCE.getFuckThatsAwful().put(id, key);
    }

    @WrapOperation(method = "method_40271", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder$Reference;createIntrusive(Lnet/minecraft/core/HolderOwner;Ljava/lang/Object;)Lnet/minecraft/core/Holder$Reference;"))
    private Holder.Reference<T> detectIntrusiveCrashCall(HolderOwner<T> owner, T value, Operation<Holder.Reference<T>> original) {
        //noinspection MixinExtrasOperationParameters
        var call = original.call(owner, value);
        fuckOff.put(call, new Exception("fuck"));

        return call;
    }

    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/lang/IllegalStateException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void dumpIntrusiveCallers(CallbackInfoReturnable<Registry<T>> cir) {
        for (Holder.Reference<T> reference : this.unregisteredIntrusiveHolders.values()) {
            if (this.fuckOff.containsKey(reference)) {
                this.fuckOff.get(reference).printStackTrace();
            }
        }
    }
}
