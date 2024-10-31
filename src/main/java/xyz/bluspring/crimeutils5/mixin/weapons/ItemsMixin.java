package xyz.bluspring.crimeutils5.mixin.weapons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Items.class)
public class ItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/AxeItem;createAttributes(Lnet/minecraft/world/item/Tier;FF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseAxeDamage(Tier tier, float damage, float speed, Operation<ItemAttributeModifiers> original) {
        switch (tier) {
            case Tiers.WOOD -> damage = 8;
            case Tiers.STONE, Tiers.GOLD -> damage = 9;
            case Tiers.IRON -> damage = 10;
            case Tiers.DIAMOND -> damage = 12;
            case Tiers.NETHERITE -> damage = 14.5f;
            default -> {}
        }

        damage = damage - tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, speed);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SwordItem;createAttributes(Lnet/minecraft/world/item/Tier;IF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseSwordDamage(Tier tier, int damage, float attackSpeed, Operation<ItemAttributeModifiers> original) {
        switch (tier) {
            case Tiers.WOOD -> damage = 6;
            case Tiers.STONE, Tiers.GOLD -> damage = 8;
            case Tiers.IRON -> damage = 9;
            case Tiers.DIAMOND -> damage = 10;
            case Tiers.NETHERITE -> damage = 12;
            default -> {}
        }

        damage = damage - (int) tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, attackSpeed);
    }
}
