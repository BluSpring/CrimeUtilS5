package xyz.bluspring.crimeutils5.components

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import xyz.bluspring.crimeutils5.CrimeUtilS5

object CrimecraftItemComponents {
    @JvmField
    val CURED = register("cured", DataComponentType.builder<CuredComponent>()
        .persistent(CuredComponent.CODEC)
        .networkSynchronized(CuredComponent.STREAM_CODEC)
        .build()
    )

    fun <T> register(id: String, type: DataComponentType<T>): DataComponentType<T> {
        return Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE, CrimeUtilS5.id(id),
            type
        )
    }

    fun init() {}
}