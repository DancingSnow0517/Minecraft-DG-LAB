package cn.dancingsnow.dglab.networking;

import cn.dancingsnow.dglab.DgLabMod;
import cn.dancingsnow.dglab.api.Strength;
import cn.dancingsnow.dglab.client.ClientData;

import net.minecraft.network.codec.ByteBufCodecs;
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

        registrar.playToClient(
                ShowQrCode.TYPE, ShowQrCode.STREAM_CODEC, (show, ctx) -> ClientData.setQrText(show.text()));
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

    public record ShowQrCode(String text) implements CustomPacketPayload {
        public static final Type<ShowQrCode> TYPE = new Type<>(DgLabMod.id("show_qr_code"));
        public static final StreamCodec<ByteBuf, ShowQrCode> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ShowQrCode::text, ShowQrCode::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
