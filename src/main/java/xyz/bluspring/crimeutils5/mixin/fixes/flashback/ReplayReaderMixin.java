package xyz.bluspring.crimeutils5.mixin.fixes.flashback;

import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.flashback.io.ReplayReader;
import com.moulberry.flashback.playback.ReplayServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

@Mixin(ReplayReader.class)
public abstract class ReplayReaderMixin {
    @Shadow private ResourceLocation lastActionName;

    @Inject(method = "handleSnapshot", at = @At(value = "INVOKE", target = "Lcom/moulberry/flashback/action/Action;handle(Lcom/moulberry/flashback/playback/ReplayServer;Lnet/minecraft/network/RegistryFriendlyByteBuf;)V", shift = At.Shift.AFTER))
    private void checkAndAvoidInvalidPacketReadCrash(ReplayServer replayServer, CallbackInfo ci, @Local ByteBuf slice) {
        if (slice.readerIndex() < slice.writerIndex()) {
            String var10002 = String.valueOf(this.lastActionName);
            CrimeUtilS5.Companion.getLogger().error("Action {} failed to fully read. Had {} bytes available, only read {}", var10002, slice.writerIndex(), slice.readerIndex());
            slice.readerIndex(slice.writerIndex());
        }
    }
}
