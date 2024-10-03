package xyz.bluspring.crimeutils5.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.crimeutils5.components.CrimecraftItemComponents;

@Mixin(Item.class)
public class ItemMixin {
    @WrapOperation(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack crimecraft$givePoisonEffects(LivingEntity instance, Level level, ItemStack food, FoodProperties foodProperties, Operation<ItemStack> original) {
        boolean isCured = food.has(CrimecraftItemComponents.CURED);
        var result = original.call(instance, level, food, foodProperties);

        if (!isCured) {
            instance.addEffect(new MobEffectInstance(MobEffects.POISON, 5 * 20, 1));
            instance.addEffect(new MobEffectInstance(MobEffects.HUNGER, 10 * 20));
        }

        return result;
    }
}
