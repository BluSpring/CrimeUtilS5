package xyz.bluspring.crimeutils5.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.resources.ResourceLocation
import xyz.bluspring.crimeutils5.CrimeUtilS5
import xyz.bluspring.crimeutils5.client.gui.FoodCurinatorScreen
import xyz.bluspring.crimeutils5.client.renderer.FoodCurinatorRenderer
import xyz.bluspring.crimeutils5.howl.HowlModel

class CrimeUtilS5Client : ClientModInitializer {

    override fun onInitializeClient() {
        MenuScreens.register(CrimeUtilS5.FOOD_CURINATOR_MENU, ::FoodCurinatorScreen)
        BlockEntityRenderers.register(CrimeUtilS5.FOOD_CURINATOR_TYPE, ::FoodCurinatorRenderer)

        BlockRenderLayerMap.INSTANCE.putBlock(CrimeUtilS5.FOOD_CURINATOR, RenderType.cutoutMipped())

        EntityModelLayerRegistry.registerModelLayer(HOWL_LAYER) {
            HowlModel.createBodyLayer()
        }
    }

    companion object {
        val HOWL_LAYER = ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("crimecraft", "howl"), "main")
    }
}
