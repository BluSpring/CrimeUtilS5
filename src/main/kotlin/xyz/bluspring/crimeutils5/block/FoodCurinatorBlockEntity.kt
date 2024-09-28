package xyz.bluspring.crimeutils5.block

import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import xyz.bluspring.crimeutils5.CrimeUtilS5
import xyz.bluspring.crimeutils5.components.CrimecraftItemComponents
import xyz.bluspring.crimeutils5.components.CuredComponent

class FoodCurinatorBlockEntity(pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(CrimeUtilS5.FOOD_CURINATOR_TYPE, pos, state) {
    // 0 - input (food)
    // 1 - fuel (coal)
    // 2 - copper
    // 3 - water
    // 4 - output
    private var inventory = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)

    var waterAmount = 0L

    var curinationTicks = 0
    var fuelTicks = 0
    var maxFuelTicks = 0
    var copperTicks = 0
    var maxCopperTicks = 0

    override fun getContainerSize(): Int {
        return 5
    }

    override fun createMenu(containerId: Int, inventory: Inventory): AbstractContainerMenu {
        return FoodCurinatorMenu(containerId, inventory, this, FoodCurinatorData(this))
    }

    override fun getDefaultName(): Component {
        return Component.literal("Food Curinator")
    }

    override fun getItems(): NonNullList<ItemStack> {
        return inventory
    }

    override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        ContainerHelper.saveAllItems(tag, this.items, registries)
        tag.putLong("FluidAmount", waterAmount)
        tag.putInt("Fuel", fuelTicks)
        tag.putInt("MaxFuel", maxFuelTicks)
        tag.putInt("Copper", copperTicks)
        tag.putInt("MaxCopper", maxCopperTicks)
        tag.putInt("CureTime", curinationTicks)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        this.items = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
        ContainerHelper.loadAllItems(tag, this.items, registries)

        if (tag.contains("FluidAmount"))
            waterAmount = tag.getLong("FluidAmount")

        if (tag.contains("Fuel"))
            fuelTicks = tag.getInt("Fuel")

        if (tag.contains("MaxFuel"))
            maxFuelTicks = tag.getInt("MaxFuel")

        if (tag.contains("Copper"))
            copperTicks = tag.getInt("Copper")

        if (tag.contains("MaxCopper"))
            maxCopperTicks = tag.getInt("MaxCopper")

        if (tag.contains("CureTime"))
            curinationTicks = tag.getInt("CureTime")
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (level.isClientSide())
            return

        // automatically refill water
        if (!this.getItem(3).isEmpty) {
            val stack = this.getItem(3)

            if (stack.`is`(Items.WATER_BUCKET) && MAX_FLUID_AMOUNT - waterAmount >= 1000) {
                waterAmount += 1000
                this.setItem(3, ItemStack(Items.BUCKET))
                this.setChanged()
            } else if (stack.`is`(Items.POTION) && stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS)?.`is`(Potions.WATER) == true
                && MAX_FLUID_AMOUNT - waterAmount >= 334
            ) {
                waterAmount += 334
                this.setItem(3, ItemStack(Items.GLASS_BOTTLE))
                this.setChanged()
            }
        }

        // make sure this is actually a curinator. sometimes this doesn't happen, it's funny like that.
        if (state.`is`(CrimeUtilS5.FOOD_CURINATOR)) {
            val inputStack = this.getItem(0)
            val fuelStack = this.getItem(1)
            val copperStack = this.getItem(2)
            var outputStack = this.getItem(4)

            // update copper ticks
            if (!copperStack.isEmpty && copperTicks <= 0) {
                val copperDuration = getCopperDuration(copperStack)

                if (copperDuration != null) {
                    copperStack.shrink(1)
                    copperTicks = copperDuration
                    maxCopperTicks = copperDuration
                    this.setChanged()
                }
            }

            // Update block state
            if (fuelTicks <= 0 && fuelStack.isEmpty && state.getValue(FoodCurinatorBlock.FUELED)) {
                level.setBlock(pos, state.setValue(FoodCurinatorBlock.FUELED, false), 3)
                this.setChanged()
            } else if (waterAmount <= 0 && state.getValue(FoodCurinatorBlock.FILLED)) {
                level.setBlock(pos, state.setValue(FoodCurinatorBlock.FILLED, false), 3)
                this.setChanged()
            } else if (fuelTicks > 0 && !state.getValue(FoodCurinatorBlock.FUELED)) {
                level.setBlock(pos, state.setValue(FoodCurinatorBlock.FUELED, true), 3)
                this.setChanged()
            } else if (waterAmount > 0 && !state.getValue(FoodCurinatorBlock.FILLED)) {
                level.setBlock(pos, state.setValue(FoodCurinatorBlock.FILLED, true), 3)
                this.setChanged()
            }

            // Food curination
            if (!inputStack.isEmpty && inputStack.`is`(CrimeUtilS5.CURABLE_TAG) &&
                (outputStack.isEmpty || (ItemStack.isSameItem(inputStack, outputStack) && outputStack.count < outputStack.maxStackSize))
            ) {
                // update fuel ticks
                if (!fuelStack.isEmpty && fuelTicks <= 0) {
                    val fuel = FuelRegistry.INSTANCE.get(fuelStack.item)

                    if (fuel != null) {
                        fuelStack.shrink(1)
                        fuelTicks = fuel
                        maxFuelTicks = fuel
                        this.setChanged()
                    }
                }

                if (fuelTicks > 0 && copperTicks > 0 && waterAmount > 0) {
                    if (curinationTicks++ >= FOOD_CURINATION_TICKS) {
                        curinationTicks = 0

                        if (outputStack.isEmpty) {
                            outputStack = inputStack.copyWithCount(1)
                        } else {
                            outputStack.count += 1
                        }

                        outputStack.set(CrimecraftItemComponents.CURED, CuredComponent.INSTANCE)

                        inputStack.shrink(1)
                        this.setItem(4, outputStack)
                    }

                    copperTicks--
                    waterAmount--
                }
            } else {
                curinationTicks = 0
            }

            // Decrement fuel ticks if there's still some left over
            if (fuelTicks > 0)
                fuelTicks--
        }
    }

    companion object {
        const val MAX_FLUID_AMOUNT = 32_000L
        const val FOOD_CURINATION_TICKS = 900

        fun getCopperDuration(copperStack: ItemStack): Int? {
            return if (copperStack.`is`(Items.COPPER_INGOT))
                2700
            else if (copperStack.`is`(Items.COPPER_BLOCK))
                24300
            else null
        }

        fun hasWater(stack: ItemStack): Boolean {
            return stack.`is`(Items.WATER_BUCKET) || (stack.`is`(Items.POTION) && stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS)?.`is`(Potions.WATER) == true)
        }

        class FoodCurinatorData(val curinator: FoodCurinatorBlockEntity) : ContainerData {
            override fun get(index: Int): Int {
                return when (index) {
                    0 -> curinator.waterAmount.toInt()
                    1 -> curinator.curinationTicks
                    2 -> curinator.fuelTicks
                    3 -> curinator.copperTicks
                    4 -> curinator.maxCopperTicks
                    5 -> curinator.maxFuelTicks
                    else -> -1
                }
            }

            override fun set(index: Int, value: Int) {
                /*when (index) {
                    0 -> curinator.waterAmount = value.toLong()
                    1 -> curinator.curinationTicks = value
                    2 -> curinator.fuelTicks = value
                    3 -> curinator.copperTicks = value
                    4 -> curinator.maxCopperTicks = value
                    5 -> curinator.maxFuelTicks = value
                }*/
            }

            override fun getCount(): Int {
                return 6
            }
        }
    }
}