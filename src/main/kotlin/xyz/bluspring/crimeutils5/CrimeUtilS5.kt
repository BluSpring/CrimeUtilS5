package xyz.bluspring.crimeutils5

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import xyz.bluspring.crimeutils5.components.CrimecraftItemComponents

class CrimeUtilS5 : ModInitializer {
    val STELLARIS_METEOR = ResourceLocation.fromNamespaceAndPath("stellaris", "chests/meteor")

    override fun onInitialize() {
        CrimecraftItemComponents.init()

        LootTableEvents.MODIFY.register { key, builder, source, registries ->
            if (key.location() == STELLARIS_METEOR) {
                builder
                    .withPool(
                        LootPool.lootPool()
                            .add(
                                LootItem.lootTableItem(Blocks.DIAMOND_BLOCK)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f)))
                            )
                        .setRolls(UniformGenerator.between(2f, 4f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK))
                            .add(LootItem.lootTableItem(Items.RAW_GOLD))
                            .setRolls(UniformGenerator.between(0f, 2f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Blocks.COAL_BLOCK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 2f)))
                            )
                            .setRolls(UniformGenerator.between(0f, 3f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
                            .setRolls(ConstantValue.exactly(1f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Blocks.OBSIDIAN)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 2f)))
                            )
                            .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f)))
                            )
                            .setRolls(UniformGenerator.between(2f, 7f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP)
                                .setWeight(15)
                            )
                            .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                                .setWeight(5)
                            )
                            .add(
                                EmptyLootItem.emptyItem()
                                    .setWeight(95)
                            )
                            .setRolls(UniformGenerator.between(0f, 1f))
                    )
                    .withPool(
                        LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.RAW_IRON)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 2f)))
                            )
                            .setRolls(UniformGenerator.between(3f, 6f))
                    )
            }
        }
    }

    companion object {
        const val MOD_ID = "crimecraft"

        fun id(name: String): ResourceLocation {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
        }
    }
}
