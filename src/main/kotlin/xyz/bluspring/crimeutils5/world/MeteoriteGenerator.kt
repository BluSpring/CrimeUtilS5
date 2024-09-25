package xyz.bluspring.crimeutils5.world

import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.synth.SimplexNoise
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object MeteoriteGenerator {
    private const val IS_DEBUG = false
    
    fun generate(level: ServerLevelAccessor, centerPos: Vec3, velocity: Vec3, radius: Int) {
        generateTail(level, BlockPos.containing(centerPos), radius, velocity)
        generateCrater(level, BlockPos.containing(centerPos), radius, velocity.length())
        generateMeteoriteBall(level, BlockPos.containing(centerPos), radius)
    }

    fun generateTail(level: ServerLevelAccessor, meteorCenter: BlockPos, meteorRadius: Int, velocity: Vec3) {
        val slope = velocity.normalize()
        val avgVelocity = velocity.length()
        val finalRadius = (meteorRadius.toDouble() * (avgVelocity / 2.0)).toInt()

        val offsetMeteorCenter = meteorCenter.offset(0, meteorRadius + (meteorRadius / 2), 0)
        val landCenter = offsetMeteorCenter.offset(BlockPos.containing(
            velocity.scale((finalRadius / meteorRadius) * avgVelocity).reverse()
        ))

        val length = offsetMeteorCenter.subtract(landCenter).center.length().absoluteValue.toInt()
        val pos = BlockPos.MutableBlockPos()
        val radialPos = BlockPos.MutableBlockPos()
        val radialPos2 = BlockPos.MutableBlockPos()

        for (i in 0..length) {
            val progress = (i.toFloat() / length.toFloat())
            pos.lerp(progress, offsetMeteorCenter, landCenter)

            // generate new random based on center, so we can be consistent across restarts
            val random = RandomSource.create(pos.asLong())
            val noise = SimplexNoise(random)

            val radius = Mth.lerpInt(progress, finalRadius, meteorRadius)
            val diameter = radius * 2

            for (y in -radius..radius) {
                val verticalIntegrity = (y + radius).toDouble() / diameter.toDouble() + 0.1
                val verticalIntegrityRandom = (verticalIntegrity * diameter).roundToInt().absoluteValue

                for (x in -radius..radius) {
                    for (z in -radius..radius) {
                        radialPos2.set(x, y, z)

                        if (radialPos2.center.length() > radius)
                            continue

                        radialPos.setWithOffset(pos, radialPos2)

                        if (IS_DEBUG) {
                            if (!level.getBlockState(radialPos).`is`(Blocks.GREEN_STAINED_GLASS)) {
                                level.setBlock(radialPos, Blocks.RED_STAINED_GLASS.defaultBlockState(), 2)
                            }
                        } else
                            level.removeBlock(radialPos, false)

                        // Magma generation
                        val value = noise.getValue(x.toDouble() * 15, y.toDouble(), z.toDouble() * 15)

                        if (level.getHeight(Heightmap.Types.OCEAN_FLOOR, radialPos.x, radialPos.z) >= radialPos.y) {
                            if (y <= -radius + (radius * 0.15) && pos.center.length() <= radius) {
                                if (value > 0.5) {
                                    level.setBlock(radialPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2)
                                }
                            } else if (radialPos2.center.length() <= radius + 2 && radialPos2.center.length() >= radius - 2) {
                                if (random.nextInt(verticalIntegrityRandom * 2) == 0) {
                                    level.setBlock(radialPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2)
                                }
                            }
                        }
                    }
                }
            }

            if (IS_DEBUG)
                level.setBlock(pos, Blocks.GREEN_STAINED_GLASS.defaultBlockState(), 2)
        }
    }

    private fun BlockPos.MutableBlockPos.lerp(progress: Float, from: BlockPos, to: BlockPos): BlockPos {
        return this.set(
            Mth.lerpInt(progress, from.x, to.x),
            Mth.lerpInt(progress, from.y, to.y),
            Mth.lerpInt(progress, from.z, to.z)
        )
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