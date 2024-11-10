package xyz.bluspring.crimeutils5.mixin.tweaks.weapons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraftfr.ninjaarmor.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.Workarounds;

@Mixin(ModItems.class)
public class NinjaModItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SwordItem;createAttributes(Lnet/minecraft/world/item/Tier;IF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseSwordDamage(Tier tier, int damage, float attackSpeed, Operation<ItemAttributeModifiers> original) {
        damage = Workarounds.INSTANCE.getDamage(tier, damage, 8, 9, 10,12, 13);
        damage = damage - (int) tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, attackSpeed);
    }
}
