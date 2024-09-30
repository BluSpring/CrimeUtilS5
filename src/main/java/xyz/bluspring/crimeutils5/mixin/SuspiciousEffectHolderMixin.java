package xyz.bluspring.crimeutils5.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.bluspring.crimeutils5.CrimeUtilS5;

import java.util.LinkedList;
import java.util.List;

@Mixin(SuspiciousEffectHolder.class)
public interface SuspiciousEffectHolderMixin {
    /**
     * @author BluSpring
     * @reason trying to fix a crash by doing literally the same thing
     */
    @Overwrite
    static List<SuspiciousEffectHolder> getAllEffectHolders() {
        CrimeUtilS5.Companion.validateHolders();

        var list = new LinkedList<SuspiciousEffectHolder>();

        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
            var item = BuiltInRegistries.ITEM.getHolder(id);

            if (item.isEmpty()) {
                CrimeUtilS5.Companion.getLogger().warn("Missing item entry $id!");
                continue;
            }

            if (!item.orElseThrow().isBound()) {
                CrimeUtilS5.Companion.getLogger().warn("Item entry $id not bound!");
                continue;
            }

            if (item.orElseThrow().value() == null) {
                CrimeUtilS5.Companion.getLogger().warn("Item entry $id is null!");
                continue;
            }

            var effectHolder = SuspiciousEffectHolder.tryGet(item.orElseThrow().value());

            if (effectHolder != null) {
                list.add(effectHolder);
            }
        }

        return list;
    }
}
