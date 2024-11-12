package xyz.bluspring.crimeutils5.client

import com.illusivesoulworks.polymorph.api.client.PolymorphWidgets
import com.illusivesoulworks.polymorph.api.client.widgets.FurnaceRecipesWidget
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.resources.ResourceLocation
import techreborn.client.gui.GuiElectricFurnace
import xyz.bluspring.crimeutils5.CrimeUtilS5
import xyz.bluspring.crimeutils5.client.gui.FoodCurinatorScreen
import xyz.bluspring.crimeutils5.client.profiling.RenderProfilingHelper
import xyz.bluspring.crimeutils5.client.renderer.FoodCurinatorRenderer
import xyz.bluspring.crimeutils5.entity.HowlModel

class CrimeUtilS5Client : ClientModInitializer {

    override fun onInitializeClient() {
        MenuScreens.register(CrimeUtilS5.FOOD_CURINATOR_MENU, ::FoodCurinatorScreen)
        BlockEntityRenderers.register(CrimeUtilS5.FOOD_CURINATOR_TYPE, ::FoodCurinatorRenderer)

        BlockRenderLayerMap.INSTANCE.putBlock(CrimeUtilS5.FOOD_CURINATOR, RenderType.cutoutMipped())

        EntityModelLayerRegistry.registerModelLayer(HOWL_LAYER) {
            HowlModel.createBodyLayer()
        }

        PolymorphWidgets.getInstance().registerWidget { screen ->
            if (screen is GuiElectricFurnace) {
                return@registerWidget FurnaceRecipesWidget(screen)
            }

            null
        }

        RenderProfilingHelper.init()
    }

    companion object {
        val HOWL_LAYER = ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("crimecraft", "howl"), "main")
    }
}
