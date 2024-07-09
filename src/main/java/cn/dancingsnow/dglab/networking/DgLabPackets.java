package cn.dancingsnow.dglab.networking;

import cn.dancingsnow.dglab.DgLabMod;
import cn.dancingsnow.dglab.client.ClientData;
import cn.dancingsnow.dglab.server.Strength;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import io.netty.buffer.ByteBuf;

public class DgLabPackets {
    public static void init(PayloadRegistrar registrar) {
        registrar.playToClient(
                Strength.TYPE, Strength.STREAM_CODEC, (strength, ctx) -> ClientData.setStrength(strength));

        registrar.playToClient(
                ClearStrength.TYPE, ClearStrength.STREAM_CODEC, (c, ctx) -> ClientData.setStrength(null));
    }

    public static class ClearStrength implements CustomPacketPayload {
        public static final ClearStrength INSTANCE = new ClearStrength();

        public static final Type<ClearStrength> TYPE = new Type<>(DgLabMod.id("clear_strength"));
        public static final StreamCodec<ByteBuf, ClearStrength> STREAM_CODEC =
                StreamCodec.unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
