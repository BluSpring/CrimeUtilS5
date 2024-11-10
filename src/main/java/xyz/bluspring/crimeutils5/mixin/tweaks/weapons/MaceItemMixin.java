package xyz.bluspring.crimeutils5.mixin.tweaks.weapons;

import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MaceItem.class)
public abstract class MaceItemMixin {
    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 5.0))
    private static double boostMaceDamage(double constant) {
        return 7.0;
    }
}
