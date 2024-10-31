package xyz.bluspring.crimeutils5.mixin.weapons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraftfr.ninjaarmor.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModItems.class)
public class NinjaModItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SwordItem;createAttributes(Lnet/minecraft/world/item/Tier;IF)Lnet/minecraft/world/item/component/ItemAttributeModifiers;"))
    private static ItemAttributeModifiers increaseSwordDamage(Tier tier, int damage, float attackSpeed, Operation<ItemAttributeModifiers> original) {
        switch (tier) {
            case Tiers.WOOD -> damage = 8;
            case Tiers.STONE, Tiers.GOLD -> damage = 9;
            case Tiers.IRON -> damage = 10;
            case Tiers.DIAMOND -> damage = 12;
            case Tiers.NETHERITE -> damage = 13;
            default -> {}
        }

        damage = damage - (int) tier.getAttackDamageBonus() - 1;

        return original.call(tier, damage, attackSpeed);
    }
}
