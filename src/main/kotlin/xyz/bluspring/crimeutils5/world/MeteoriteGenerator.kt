package xyz.bluspring.crimeutils5.world

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.synth.SimplexNoise
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object MeteoriteGenerator {
    private const val IS_DEBUG = true
    
    fun generate(level: ServerLevelAccessor, entryPos: Vec3, velocity: Vec3, radius: Int) {
        val slope = velocity.normalize()

        generateCrater(level, BlockPos.containing(entryPos), radius, velocity.length())
        generateMeteoriteBall(level, BlockPos.containing(entryPos), radius)
    }

    fun generateCrater(level: ServerLevelAccessor, meteorCenter: BlockPos, meteorRadius: Int, meteorAvgVelocity: Double) {
        val radius = (meteorRadius.toDouble() * (meteorAvgVelocity / 2.0)).toInt()
        val diameter = radius * 2
        val center = meteorCenter.offset(0, radius - (meteorRadius / 2), 0)

        val allGoneRadius = (radius / 3) * 2

        // generate new random based on center, so we can be consistent across restarts
        val random = RandomSource.create(center.asLong())
        val noise = SimplexNoise(random)

        val pos = BlockPos.MutableBlockPos()
        for (offsetY in -radius..radius) {
            val verticalIntegrity = (offsetY + radius).toDouble() / diameter.toDouble() + 0.1
            val verticalIntegrityRandom = (verticalIntegrity * diameter).roundToInt().absoluteValue

            for (offsetX in -radius..radius) {
                val horizontalIntegrity = if (offsetX >= (-radius + meteorRadius) && offsetX <= (radius - meteorRadius))
                    1.0
                else
                    1.0 - (offsetX + radius - meteorRadius).toDouble() / (diameter - meteorRadius).toDouble() + 0.1
                val horizontalIntegrityRandom = (horizontalIntegrity * diameter).roundToInt()

                for (offsetZ in -radius..radius) {
                    pos.set(offsetX, offsetY, offsetZ)

                    if ((pos.center.length() <= allGoneRadius || offsetY <= -radius + (radius / 3)) && pos.center.length() <= radius) {
                        pos.setWithOffset(center, offsetX, offsetY, offsetZ)

                        if (IS_DEBUG)
                            level.setBlock(pos, Blocks.GLASS.defaultBlockState(), 2)
                        else
                            level.removeBlock(pos, false)
                    } else if (pos.center.length() <= radius + 2 && pos.center.length() >= radius - 2) {
                        if (random.nextInt(horizontalIntegrityRandom.absoluteValue) != 0) {
                            pos.setWithOffset(center, offsetX, offsetY, offsetZ)

                            if (IS_DEBUG)
                                level.setBlock(pos, Blocks.YELLOW_STAINED_GLASS.defaultBlockState(), 2)
                            else
                                level.removeBlock(pos, false)
                        }
                    } else if (pos.center.length() <= radius) {
                        pos.setWithOffset(center, offsetX, offsetY, offsetZ)
                        if (IS_DEBUG)
                            level.setBlock(pos, Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState(), 2)
                        else
                            level.removeBlock(pos, false)
                    }

                    // Magma generation
                    val value = noise.getValue(offsetX.toDouble() * 15, offsetY.toDouble(), offsetZ.toDouble() * 15)

                    pos.set(offsetX, offsetY, offsetZ)

                    if (level.getHeight(Heightmap.Types.OCEAN_FLOOR, center.x + offsetX, center.z + offsetZ) >= center.y + offsetY) {
                        if (offsetY <= -radius + (radius * 0.15) && pos.center.length() <= radius) {
                            if (value > 0.5) {
                                pos.setWithOffset(center, offsetX, offsetY, offsetZ)
                                level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2)
                            }
                        } else if (pos.center.length() <= radius + 2 && pos.center.length() >= radius - 2) {
                            if (random.nextInt(verticalIntegrityRandom * 2) == 0) {
                                pos.setWithOffset(center, offsetX, offsetY, offsetZ)
                                level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2)
                            }
                        }
                    }
                }
            }
        }
    }

    fun generateMeteoriteBall(level: ServerLevelAccessor, center: BlockPos, radius: Int) {
        // generate new random based on center, so we can be consistent across restarts
        val random = RandomSource.create(center.asLong())
        val diameter = radius * 2

        val pos = BlockPos.MutableBlockPos()
        for (offsetY in -radius..radius) {
            // the general meteorite integrity, where the bottom remains generally intact whereas the top is generally shattered
            val integrity = 1.0 - (offsetY + radius).toDouble() / diameter.toDouble()
            val integrityRandom = (integrity * diameter).roundToInt()

            for (offsetX in -radius..radius) {
                for (offsetZ in -radius..radius) {
                    pos.set(offsetX, offsetY, offsetZ)
                    if (pos.center.length() > radius)
                        continue

                    if (pos.center.length() <= (radius / 2) && random.nextInt(2) == 0) {
                        pos.setWithOffset(center, offsetX, offsetY, offsetZ)
                        level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2)
                    }

                    pos.setWithOffset(center, offsetX, offsetY, offsetZ)

                    if (random.nextInt(integrityRandom) == 0) {
                        // lava gets spawned above the meteorite
                        if (integrity < 0.3 && random.nextInt(5) == 0) {
                            level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 2)
                            level.blockUpdated(pos, Blocks.LAVA)
                        }

                        continue
                    }

                    if (IS_DEBUG)
                        level.setBlock(pos, Blocks.TINTED_GLASS.defaultBlockState(), 2)
                    else
                        level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 2)
                }
            }
        }
    }
}