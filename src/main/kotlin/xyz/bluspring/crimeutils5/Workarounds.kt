package xyz.bluspring.crimeutils5

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.ResourceKey

object Workarounds {
    val fuckThatsAwful = Int2ObjectArrayMap<ResourceKey<*>>()

    class ForcedTracedObjectMap<T>(capacity: Int) : ObjectArrayList<T>(capacity) {
        override fun remove(element: T): Boolean {
            try {
                throw Exception("calling a test for $element")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return super.remove(element)
        }

        override fun removeAt(index: Int): T {
            return super.removeAt(index).apply {
                try {
                    throw Exception("removing $index with value $this")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun set(index: Int, element: T): T {
            try {
                throw Exception("storing $index with $element")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return super.set(index, element)
        }
    }
}