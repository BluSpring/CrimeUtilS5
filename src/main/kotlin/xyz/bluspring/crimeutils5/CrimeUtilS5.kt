package xyz.bluspring.crimeutils5

import com.illusivesoulworks.polymorph.api.PolymorphApi
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.Commands
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
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
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity
import xyz.bluspring.crimeutils5.block.FoodCurinatorBlock
import xyz.bluspring.crimeutils5.block.FoodCurinatorBlockEntity
import xyz.bluspring.crimeutils5.block.FoodCurinatorMenu
import xyz.bluspring.crimeutils5.compat.polymorph.TRElectricFurnaceDataComponent
import xyz.bluspring.crimeutils5.components.CrimecraftItemComponents
import xyz.bluspring.crimeutils5.entity.HowlTheDog
import xyz.bluspring.crimeutils5.mixin.MappedRegistryAccessor
import xyz.bluspring.crimeutils5.network.RedSkyPacket

class CrimeUtilS5 : ModInitializer {
    val STELLARIS_METEOR = ResourceLocation.fromNamespaceAndPath("stellaris", "chests/meteor")

    override fun onInitialize() {
        CrimecraftItemComponents.init()
        HowlTheDog.init()

        PolymorphApi.getInstance().registerBlockEntity { blockEntity ->
            if (blockEntity is ElectricFurnaceBlockEntity) {
                return@registerBlockEntity TRElectricFurnaceDataComponent(blockEntity)
            }

            null
        }

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

        ServerLifecycleEvents.SERVER_STOPPING.register {
            useRedSky = false
        }

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            ServerPlayNetworking.send(handler.player, RedSkyPacket(useRedSky))
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(Commands.literal("toggleredsky").executes {
                useRedSky = !useRedSky
                it.source.sendSystemMessage(Component.literal("Red sky: $useRedSky"))

                for (player in it.source.server.playerList.players) {
                    ServerPlayNetworking.send(player, RedSkyPacket(useRedSky))
                }

                1
            })
        }

        PayloadTypeRegistry.playS2C().register(RedSkyPacket.TYPE, RedSkyPacket.CODEC)

        /*var isChunkyRunning = true

        ServerPlayConnectionEvents.JOIN.register { handler, _, server ->
            if (isChunkyRunning) {
                isChunkyRunning = false
                logger.info("Player detected, pausing Chunky.")

                val chunky = ChunkyProvider.get()
                val genTasks = chunky.generationTasks

                for (generationTask in genTasks.values) {
                    generationTask.stop(false)
                }
            }
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
            if (server.playerList.players.none { it.uuid != handler.player.uuid }) {
                logger.info("No players are online, restarting Chunky.")
                val chunky = ChunkyProvider.get()
                val loadTasks = chunky.taskLoader.loadTasks()

                val genTasks = chunky.generationTasks
                for (genTask in loadTasks.filter { !it.isCancelled }) {
                    val world = genTask.selection.world()
                    if (!genTasks.containsKey(world.name)) {
                        genTasks[world.name] = genTask
                        chunky.scheduler.runTask(genTask)
                    }
                }

                isChunkyRunning = true
            }
        }*/
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

        var useRedSky = false
    }
}
