package xyz.bluspring.crimeutils5.network

import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import xyz.bluspring.crimeutils5.CrimeUtilS5

data class RedSkyPacket(
    val isEnabled: Boolean
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<RedSkyPacket> = CustomPacketPayload.Type(CrimeUtilS5.id("red_sky"))
        val CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            RedSkyPacket::isEnabled,
            ::RedSkyPacket
        )
    }
}
