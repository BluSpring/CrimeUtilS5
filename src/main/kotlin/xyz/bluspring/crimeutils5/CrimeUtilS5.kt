package xyz.bluspring.crimeutils5

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import org.slf4j.LoggerFactory
import xyz.bluspring.crimeutils5.block.FoodCurinatorBlock
import xyz.bluspring.crimeutils5.block.FoodCurinatorBlockEntity
import xyz.bluspring.crimeutils5.block.FoodCurinatorMenu
import xyz.bluspring.crimeutils5.components.CrimecraftItemComponents
import xyz.bluspring.crimeutils5.mixin.MappedRegistryAccessor

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

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register { entries ->
            entries.accept(FOOD_CURINATOR_ITEM)
        }
    }

    companion object {
        const val MOD_ID = "crimecraft"
        val logger = LoggerFactory.getLogger(CrimeUtilS5::class.java)

        fun id(name: String): ResourceLocation {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
        }

        val FOOD_CURINATOR = Registry.register(BuiltInRegistries.BLOCK, id("food_curinator"), FoodCurinatorBlock())
        val FOOD_CURINATOR_TYPE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id("food_curinator"),
            BlockEntityType.Builder.of(::FoodCurinatorBlockEntity, FOOD_CURINATOR)
                .build()
        )
        val FOOD_CURINATOR_ITEM = Registry.register(BuiltInRegistries.ITEM, id("food_curinator"), BlockItem(FOOD_CURINATOR, Item.Properties()))
        val FOOD_CURINATOR_MENU = Registry.register(BuiltInRegistries.MENU, id("food_curinator"), MenuType<FoodCurinatorMenu>(::FoodCurinatorMenu, FeatureFlagSet.of()))

        val CURABLE_TAG = TagKey.create(Registries.ITEM, id("curable"))

        fun validateHolders() {
            for ((i, reference) in (BuiltInRegistries.ITEM as MappedRegistryAccessor<Item>).byId.withIndex()) {
                if (reference == null) {
                    logger.error("Index $i's holder is null!")
                    logger.error("Last bound to: ${Workarounds.fuckThatsAwful.get(i).location()}")
                }
            }

            for ((loc, ref) in (BuiltInRegistries.ITEM as MappedRegistryAccessor<Item>).byLocation.entries) {
                if (ref == null) {
                    logger.error("Item $loc has a null holder!")
                }
            }
        }
    }
}
