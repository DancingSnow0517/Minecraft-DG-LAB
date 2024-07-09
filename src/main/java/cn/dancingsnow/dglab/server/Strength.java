package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.DgLabMod;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Strength implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<Strength> TYPE = new Type<>(DgLabMod.id("strength"));
    public static final StreamCodec<ByteBuf, Strength> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            Strength::getACurrentStrength,
            ByteBufCodecs.VAR_INT,
            Strength::getBCurrentStrength,
            ByteBufCodecs.VAR_INT,
            Strength::getAMaxStrength,
            ByteBufCodecs.VAR_INT,
            Strength::getBMaxStrength,
            Strength::new);

    private int aCurrentStrength = 0;
    private int bCurrentStrength = 0;
    private int aMaxStrength = 0;
    private int bMaxStrength = 0;

    public Strength() {}

    public Strength(int aCurrentStrength, int bCurrentStrength, int aMaxStrength, int bMaxStrength) {
        this.aCurrentStrength = aCurrentStrength;
        this.bCurrentStrength = bCurrentStrength;
        this.aMaxStrength = aMaxStrength;
        this.bMaxStrength = bMaxStrength;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
