package xyz.bluspring.crimeutils5.mixin.weapons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

@Mixin(Items.class)
public class ItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/AxeItem;createAttributes(Lnet/minecraft/world/item/Tier;FF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseAxeDamage(Tier tier, float damage, float speed, Operation<ItemAttributeModifiers> original) {
        damage = CrimeUtilS5.Companion.getDamage(tier, damage, 8, 9, 10, 12, 14.5f);
        damage = damage - tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, speed);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SwordItem;createAttributes(Lnet/minecraft/world/item/Tier;IF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseSwordDamage(Tier tier, int damage, float attackSpeed, Operation<ItemAttributeModifiers> original) {
        damage = CrimeUtilS5.Companion.getDamage(tier, damage, 6, 8, 9, 10, 12);
        damage = damage - (int) tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, attackSpeed);
    }
}
