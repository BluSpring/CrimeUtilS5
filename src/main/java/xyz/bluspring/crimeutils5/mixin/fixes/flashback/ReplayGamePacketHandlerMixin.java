package xyz.bluspring.crimeutils5.mixin.fixes.flashback;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.moulberry.flashback.playback.ReplayGamePacketHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(ReplayGamePacketHandler.class)
public class ReplayGamePacketHandlerMixin {
    @WrapOperation(method = "handleSetEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;id()I"))
    private int crimecraft$fixBrokenEntityDataCrash(SynchedEntityData.DataValue<?> instance, Operation<Integer> original, @Local SynchedEntityData entityData, @Share("fuckOff") LocalBooleanRef fuckOff) {
        var index = original.call(instance);

        var itemsById = ((SynchedEntityDataAccessor) entityData).getItemsById();
        if (itemsById.length <= index) {
            fuckOff.set(true);
            return itemsById.length - 1;
        }

        return index;
    }

    @WrapWithCondition(method = "handleSetEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V"))
    private <T> boolean crimecraft$preventSetBrokenValue(SynchedEntityData instance, EntityDataAccessor<T> key, T value, boolean force, @Share("fuckOff") LocalBooleanRef fuckOff) {
        if (fuckOff.get()) {
            fuckOff.set(false);
            return false;
        }

        return true;
    }
}
