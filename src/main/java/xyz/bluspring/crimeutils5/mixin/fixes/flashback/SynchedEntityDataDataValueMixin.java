package xyz.bluspring.crimeutils5.mixin.fixes.flashback;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

@Mixin(SynchedEntityData.DataValue.class)
public class SynchedEntityDataDataValueMixin<T> {
    @Shadow @Final private EntityDataSerializer<T> serializer;

    @Shadow @Final private T value;

    @Shadow @Final private int id;

    @WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/StreamCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V"))
    private void ensureCheckEncode(StreamCodec<? super RegistryFriendlyByteBuf, T> instance, Object buf, T o, Operation<Void> original, @Local int id) {
        try {
            //noinspection MixinExtrasOperationParameters
            original.call(instance, buf, o);
        } catch (Throwable e) {
            CrimeUtilS5.Companion.getLogger().error("Failed to encode data value for serializer {} (internal: {}, serialized: {}), provided value: {}", this.serializer, this.id, id, this.value);
            throw e;
        }
    }
}
