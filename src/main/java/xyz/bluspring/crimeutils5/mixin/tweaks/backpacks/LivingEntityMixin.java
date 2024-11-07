package xyz.bluspring.crimeutils5.mixin.tweaks.backpacks;

import com.tiviacz.travelersbackpack.common.BackpackAbilities;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import com.tiviacz.travelersbackpack.init.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "calculateFallDamage", cancellable = true)
    private void damage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        if (TravelersBackpackConfig.getConfig().backpackAbilities.enableBackpackAbilities) {
            if ((Object) this instanceof Player player) {
                if (BackpackAbilities.ABILITIES.checkBackpack(player, ModItems.BLAZE_TRAVELERS_BACKPACK)) {
                    cir.setReturnValue(0);
                }
            }
        }
    }
}
