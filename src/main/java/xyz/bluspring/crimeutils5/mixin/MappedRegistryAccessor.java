package xyz.bluspring.crimeutils5.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor<T> {
    @Accessor
    ObjectList<Holder.Reference<T>> getById();

    @Accessor
    Map<ResourceLocation, Holder.Reference<T>> getByLocation();
}
