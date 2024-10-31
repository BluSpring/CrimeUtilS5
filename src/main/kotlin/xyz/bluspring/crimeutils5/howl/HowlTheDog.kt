package xyz.bluspring.crimeutils5.howl

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.Wolf
import net.minecraft.world.level.entity.EntityTypeTest
import xyz.bluspring.crimeutils5.CrimeUtilS5

class HowlTheDog {
    companion object {
        const val NAME = "\ue43f7 howl"
        const val DISPLAY_NAME = "Howl"

        val HOWL_HEALTH_UUID = CrimeUtilS5.id("howl_health")
        val HOWL_STRENGTH_UUID = CrimeUtilS5.id("howl_strength")
        val HOWL_TOUGHNESS_UUID = CrimeUtilS5.id("howl_toughness")
        val HOWL_SPEED_UUID = CrimeUtilS5.id("howl_speed")

        val HOWL_HEALTH_MODIFIER = AttributeModifier(HOWL_HEALTH_UUID, 300.0, AttributeModifier.Operation.ADD_VALUE)
        val HOWL_STRENGTH_MODIFIER = AttributeModifier(HOWL_STRENGTH_UUID, 9.96, AttributeModifier.Operation.ADD_VALUE)
        val HOWL_TOUGHNESS_MODIFIER = AttributeModifier(HOWL_TOUGHNESS_UUID, 25.6, AttributeModifier.Operation.ADD_VALUE)
        val HOWL_SPEED_MODIFIER = AttributeModifier(HOWL_SPEED_UUID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)

        fun init() {
            EntityTrackingEvents.START_TRACKING.register { entity, player ->
                if (entity !is Wolf)
                    return@register

                if (isHowl(entity) && !isHowlMatchingVersion(entity))
                    applyHowlHealth(entity)
            }

            ServerTickEvents.END_WORLD_TICK.register { level ->
                for (entity in level.getEntities(EntityTypeTest.forClass(Wolf::class.java)) {
                    isHowl(it)
                }) {
                    if (!isHowlMatchingVersion(entity))
                        applyHowlHealth(entity)

                    if (entity.y <= level.dimensionType().minY) {
                        // Teleport Howl to safety
                        val player = entity.owner?.run { if (this.onGround()) this else null } ?: level.players().filter { it.onGround() }.randomOrNull()

                        if (player == null) {
                            entity.fallDistance = 0f
                            entity.isNoGravity = true
                        } else {
                            entity.fallDistance = 0f
                            entity.teleportTo(player.x, player.y, player.z)
                            entity.isNoGravity = false
                        }
                    }

                    entity.health = entity.maxHealth
                }
            }
        }

        fun applyHowlHealth(entity: Wolf) {
            CrimeUtilS5.logger.info("Detected a Howl dog without any health modifiers, applying health modifier.")

            entity.getAttribute(Attributes.MAX_HEALTH)?.removeModifier(HOWL_HEALTH_UUID)
            entity.getAttribute(Attributes.ATTACK_DAMAGE)?.removeModifier(HOWL_STRENGTH_UUID)
            entity.getAttribute(Attributes.ARMOR)?.removeModifier(HOWL_TOUGHNESS_UUID)
            entity.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(HOWL_SPEED_UUID)

            entity.getAttribute(Attributes.MAX_HEALTH)?.addPermanentModifier(HOWL_HEALTH_MODIFIER)
            entity.getAttribute(Attributes.ATTACK_DAMAGE)?.addPermanentModifier(HOWL_STRENGTH_MODIFIER)
            entity.getAttribute(Attributes.ARMOR)?.addPermanentModifier(HOWL_TOUGHNESS_MODIFIER)
            entity.getAttribute(Attributes.MOVEMENT_SPEED)?.addPermanentModifier(HOWL_SPEED_MODIFIER)

            entity.health = entity.maxHealth

            if (entity is HowlEntity)
                entity.ccUpdateVersion()
        }


        @JvmStatic
        fun isHowl(entity: LivingEntity): Boolean {
            return entity is Wolf && entity.hasCustomName() && entity.customName?.string?.lowercase() == NAME
        }

        @JvmStatic
        fun isHowlMatchingVersion(entity: LivingEntity): Boolean {
            return isHowl(entity) && entity is HowlEntity && entity.ccVersion == 1
        }
    }
}