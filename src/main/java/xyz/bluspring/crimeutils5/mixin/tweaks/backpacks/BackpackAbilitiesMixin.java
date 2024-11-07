package xyz.bluspring.crimeutils5.mixin.tweaks.backpacks;

import com.llamalad7.mixinextras.sugar.Local;
import com.tiviacz.travelersbackpack.common.BackpackAbilities;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BackpackAbilities.class)
public class BackpackAbilitiesMixin {
    @ModifyConstant(method = "blazeAbility(Lnet/minecraft/world/entity/player/Player;)V", constant = @Constant(floatValue = 0.0f))
    private float dontReduceFallDistance(float constant, @Local(argsOnly = true) Player player) {
        return player.fallDistance;
    }
}
