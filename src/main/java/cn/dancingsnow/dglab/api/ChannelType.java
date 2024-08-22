package cn.dancingsnow.dglab.api;

import net.minecraft.MethodsReturnNonnullByDefault;

import lombok.Getter;

@Getter
@MethodsReturnNonnullByDefault
public enum ChannelType {
    A(1),
    B(2);
    private final int typeNumber;

    ChannelType(int type) {
        this.typeNumber = type;
    }

    public static ChannelType of(Object o) {
        if (o instanceof ChannelType type) {
            return type;
        }

        String asString = String.valueOf(o);
        return ChannelType.valueOf(asString.toUpperCase());
    }
}
