package xyz.bluspring.crimeutils5.block

import net.minecraft.util.Mth
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import xyz.bluspring.crimeutils5.CrimeUtilS5

class FoodCurinatorMenu(id: Int, inventory: Inventory, val container: Container, val data: ContainerData) : AbstractContainerMenu(CrimeUtilS5.FOOD_CURINATOR_MENU, id) {
    constructor(id: Int, inventory: Inventory) : this(id, inventory, SimpleContainer(5), SimpleContainerData(6))

    init {
        this.addSlot(Slot(container, 0, 56, 17)) // input slot
        this.addSlot(FuelSlot(container, 1, 56, 53)) // fuel slot
        this.addSlot(CopperSlot(container, 2, 18, 36)) // copper slot
        this.addSlot(WaterSlot(container, 3, 142, 61)) // water slot
        this.addSlot(FurnaceResultSlot(inventory.player, container, 4, 116, 35)) // output slot

        var i = 0
        while (i < 3) {
            for (j in 0..8) {
                this.addSlot(Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
            }
            ++i
        }

        i = 0
        while (i < 9) {
            this.addSlot(Slot(inventory, i, 8 + i * 18, 142))
            ++i
        }

        this.addDataSlots(data)
    }

    val waterAmount: Float
        get() {
            return Mth.clamp(this.data[0].toFloat() / FoodCurinatorBlockEntity.MAX_FLUID_AMOUNT, 0f, 1f)
        }

    val curinationProgress: Float
        get() {
            return Mth.clamp(this.data[1].toFloat() / FoodCurinatorBlockEntity.FOOD_CURINATION_TICKS, 0f, 1f)
        }

    val copperAmount: Float
        get() {
            if (this.data[4] == 0)
                return 0f

            return Mth.clamp(this.data[3].toFloat() / this.data[4].toFloat(), 0f, 1f)
        }

    val fuelAmount: Float
        get() {
            if (this.data[5] == 0)
                return 0f

            return Mth.clamp(this.data[2].toFloat() / this.data[5], 0f, 1f)
        }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = slots[index]
        if (slot != null && slot.hasItem()) {
            val itemStack2 = slot.item
            itemStack = itemStack2.copy()
            if (index == 2) {
                if (!this.moveItemStackTo(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY
                }

                slot.onQuickCraft(itemStack2, itemStack)
            } else if (index != 1 && index != 0) {
                if (itemStack2.`is`(CrimeUtilS5.CURABLE_TAG)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (AbstractFurnaceBlockEntity.isFuel(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (FoodCurinatorBlockEntity.getCopperDuration(itemStack2) != null) {
                    if (!this.moveItemStackTo(itemStack2, 2, 3, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (FoodCurinatorBlockEntity.hasWater(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 3, 4, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index >= 3 + 2 && index < 30 + 2) {
                    if (!this.moveItemStackTo(itemStack2, 30 + 2, 39 + 2, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index >= 30 + 2 && index < 39 + 2 && !this.moveItemStackTo(itemStack2, 3 + 2, 30 + 2, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(itemStack2, 3 + 2, 39 + 2, false)) {
                return ItemStack.EMPTY
            }

            if (itemStack2.isEmpty) {
                slot.setByPlayer(ItemStack.EMPTY)
            } else {
                slot.setChanged()
            }

            if (itemStack2.count == itemStack.count) {
                return ItemStack.EMPTY
            }

            slot.onTake(player, itemStack2)
        }

        return itemStack
    }

    override fun stillValid(player: Player): Boolean {
        return this.container.stillValid(player)
    }

    private class FuelSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean {
            return AbstractFurnaceBlockEntity.isFuel(stack)
        }
    }

    private class CopperSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean {
            return FoodCurinatorBlockEntity.getCopperDuration(stack) != null
        }
    }

    private class WaterSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean {
            return FoodCurinatorBlockEntity.hasWater(stack)
        }
    }
}