package xyz.bluspring.crimeutils5.mixin.weapons;

import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {
    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 8.0))
    private static double boostTridentDamage(double constant) {
        return 12.0;
    }
}
