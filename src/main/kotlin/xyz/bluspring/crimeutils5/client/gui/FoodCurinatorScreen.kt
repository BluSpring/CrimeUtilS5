package xyz.bluspring.crimeutils5.client.gui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import xyz.bluspring.crimeutils5.CrimeUtilS5
import xyz.bluspring.crimeutils5.block.FoodCurinatorMenu

class FoodCurinatorScreen(menu: FoodCurinatorMenu, inventory: Inventory, title: Component) : AbstractContainerScreen<FoodCurinatorMenu>(menu, inventory, title) {
    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight)

        if (this.menu.fuelAmount > 0) {
            val height = ((this.menu.fuelAmount * 13f) + 1).toInt()
            guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - height, leftPos + 56, topPos + 36 + 14 - height, 14, height)
        }

        if (this.menu.curinationProgress > 0) {
            val width = ((this.menu.curinationProgress * 24f)).toInt()
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftPos + 79, topPos + 34, width, 16)
        }

        if (this.menu.copperAmount > 0) {
            val height = ((this.menu.copperAmount * 61f) + 1).toInt()
            guiGraphics.blitSprite(COPPER_FILLED_SPRITE, 6, 62, 0, 62 - height, leftPos + 7, topPos + 12 + 62 - height, 6, height)
        }

        if (this.menu.waterAmount > 0) {
            val height = ((this.menu.waterAmount * 61f) + 1).toInt()
            guiGraphics.blitSprite(WATER_FILLED_SPRITE, 6, 62, 0, 62 - height, leftPos + 163, topPos + 12 + 62 - height, 6, height)
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    companion object {
        val BACKGROUND = CrimeUtilS5.id("textures/gui/container/food_curinator.png")
        val COPPER_FILLED_SPRITE = CrimeUtilS5.id("food_curinator/copper_filled")
        val WATER_FILLED_SPRITE = CrimeUtilS5.id("food_curinator/water_filled")
        val LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress")
        val BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress")
    }
}