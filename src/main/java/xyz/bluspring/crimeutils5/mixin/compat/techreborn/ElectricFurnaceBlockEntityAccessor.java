package xyz.bluspring.crimeutils5.mixin.compat.techreborn;

import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;

@Mixin(ElectricFurnaceBlockEntity.class)
public interface ElectricFurnaceBlockEntityAccessor {
    @Accessor
    SmeltingRecipe getCurrentRecipe();

    @Accessor
    void setCurrentRecipe(SmeltingRecipe currentRecipe);
}
