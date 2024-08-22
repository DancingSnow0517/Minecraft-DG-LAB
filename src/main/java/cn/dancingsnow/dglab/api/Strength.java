package cn.dancingsnow.dglab.api;

import net.minecraft.network.FriendlyByteBuf;

import lombok.Data;

@Data
public class Strength {

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

    public static void encode(Strength strength, FriendlyByteBuf buf) {
        buf.writeInt(strength.aCurrentStrength);
        buf.writeInt(strength.bCurrentStrength);
        buf.writeInt(strength.aMaxStrength);
        buf.writeInt(strength.bMaxStrength);
    }

    public static Strength decode(FriendlyByteBuf buf) {
        int aCurrentStrength = buf.readInt();
        int bCurrentStrength = buf.readInt();
        int aMaxStrength = buf.readInt();
        int bMaxStrength = buf.readInt();
        return new Strength(aCurrentStrength, bCurrentStrength, aMaxStrength, bMaxStrength);
    }
}
