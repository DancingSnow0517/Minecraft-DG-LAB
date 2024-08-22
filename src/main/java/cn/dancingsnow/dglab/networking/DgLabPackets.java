package cn.dancingsnow.dglab.networking;

import cn.dancingsnow.dglab.DgLabCommon;
import cn.dancingsnow.dglab.api.Strength;
import cn.dancingsnow.dglab.client.ClientData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class DgLabPackets {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            DgLabCommon.id("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void init() {
        INSTANCE.registerMessage(
                0, Strength.class, Strength::encode, Strength::decode, ((strength, ctx) -> {
                    ctx.get()
                            .enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                                    Dist.CLIENT, () -> () -> ClientData.setStrength(strength)));
                    ctx.get().setPacketHandled(true);
                }));

        INSTANCE.registerMessage(
                1,
                ClearStrength.class,
                (o, friendlyByteBuf) -> {},
                buf -> new ClearStrength(),
                (clear, ctx) -> {
                    ctx.get()
                            .enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                                    Dist.CLIENT, () -> () -> ClientData.setStrength(null)));
                    ctx.get().setPacketHandled(true);
                });

        INSTANCE.registerMessage(
                2, ShowQrCode.class, ShowQrCode::encode, ShowQrCode::decode, ((show, ctx) -> {
                    ctx.get()
                            .enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                                    Dist.CLIENT, () -> () -> ClientData.setQrText(show.text())));
                    ctx.get().setPacketHandled(true);
                }));
    }

    public static class ClearStrength {}

    public record ShowQrCode(String text) {
        public static void encode(ShowQrCode code, FriendlyByteBuf buf) {
            buf.writeUtf(code.text);
        }

        public static ShowQrCode decode(FriendlyByteBuf buf) {
            return new ShowQrCode(buf.readUtf());
        }
    }
}
