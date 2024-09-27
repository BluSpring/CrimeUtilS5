package xyz.bluspring.crimeutils5.components

import com.mojang.serialization.Codec
import net.minecraft.ChatFormatting
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

class CuredComponent : TooltipProvider {
    override fun addToTooltip(
        context: Item.TooltipContext,
        tooltipAdder: Consumer<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipAdder.accept(Component.literal("Cured").withStyle(ChatFormatting.YELLOW))
    }

    companion object {
        val INSTANCE = CuredComponent()
        val CODEC = Codec.unit { INSTANCE }
        val STREAM_CODEC = StreamCodec.unit<FriendlyByteBuf, CuredComponent>(INSTANCE)
    }
}
