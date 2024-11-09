package xyz.bluspring.crimeutils5.compat.polymorph

import com.illusivesoulworks.polymorph.common.integration.AbstractCompatibilityModule
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.block.entity.BlockEntity
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity
import xyz.bluspring.crimeutils5.mixin.compat.techreborn.ElectricFurnaceBlockEntityAccessor

class TRElectricFurnaceModule : AbstractCompatibilityModule() {
    override fun selectRecipe(blockEntity: BlockEntity, recipe: RecipeHolder<*>): Boolean {
        if (recipe.value is SmeltingRecipe) {
            if (blockEntity is ElectricFurnaceBlockEntity) {
                (blockEntity as ElectricFurnaceBlockEntityAccessor).currentRecipe = recipe.value as SmeltingRecipe
            }
        }

        return super.selectRecipe(blockEntity, recipe)
    }
}