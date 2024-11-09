package xyz.bluspring.crimeutils5.compat.polymorph

import com.illusivesoulworks.polymorph.common.components.PolymorphFabricComponents
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity

class CrimecraftPolymorphComponents : BlockComponentInitializer {
    override fun registerBlockComponentFactories(registry: BlockComponentFactoryRegistry) {
        registry.registerFor(ElectricFurnaceBlockEntity::class.java, PolymorphFabricComponents.BLOCK_ENTITY_RECIPE_DATA) { TRElectricFurnaceDataComponent(it) }
    }
}