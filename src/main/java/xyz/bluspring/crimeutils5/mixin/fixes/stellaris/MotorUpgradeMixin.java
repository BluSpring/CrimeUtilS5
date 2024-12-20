package xyz.bluspring.crimeutils5.mixin.fixes.stellaris;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.st0x0ef.stellaris.common.vehicle_upgrade.FuelType;
import com.st0x0ef.stellaris.common.vehicle_upgrade.MotorUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MotorUpgrade.class, remap = false)
public class MotorUpgradeMixin {
    @ModifyReturnValue(method = "getFuelType", at = @At("RETURN"))
    private FuelType.Type ensureFuelTypeNotNullable(FuelType.Type original) {
        if (original == null)
            return FuelType.Type.FUEL;

        return original;
    }
}
