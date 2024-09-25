package xyz.bluspring.crimeutils5

import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.network.chat.Component
import xyz.bluspring.crimeutils5.world.MeteoriteGenerator

class CrimeUtilS5 : ModInitializer {

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("gencrater")
                    .then(
                        Commands.argument("velocity", Vec3Argument.vec3())
                            .then(
                                Commands.argument("radius", IntegerArgumentType.integer(1))
                                    .executes {
                                        val velocity = Vec3Argument.getVec3(it, "velocity")
                                        val pos = it.source.position
                                        val radius = IntegerArgumentType.getInteger(it, "radius")

                                        MeteoriteGenerator.generate(it.source.level, pos, velocity, radius)
                                        it.source.sendSystemMessage(Component.literal("Generated crater"))

                                        1
                                    }
                            )
                    )
            )
        }
    }

    companion object {
        const val MOD_ID = "crimecraft"
    }
}
