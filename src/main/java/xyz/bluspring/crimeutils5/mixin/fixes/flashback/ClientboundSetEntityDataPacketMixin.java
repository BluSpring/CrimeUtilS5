package xyz.bluspring.crimeutils5.mixin.fixes.flashback;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

@Mixin(ClientboundSetEntityDataPacket.class)
public class ClientboundSetEntityDataPacketMixin {
    @Shadow @Final private int id;

    @WrapMethod(method = "write")
    private void checkIfDataFailedWriting(RegistryFriendlyByteBuf buffer, Operation<Void> original) {
        try {
            original.call(buffer);
        } catch (Throwable e) {
            CrimeUtilS5.Companion.getLogger().error("Failed to encode entity data! Entity ID - {}", this.id);
            var entity = Minecraft.getInstance().level.getEntity(this.id);

            if (entity != null) {
                CrimeUtilS5.Companion.getLogger().error("Entity: {}, Class: {}", entity, entity.getClass());
            } else {
                CrimeUtilS5.Companion.getLogger().error("No associated entity could be found.");
            }

            e.printStackTrace();

            throw e;
        }
    }
}
