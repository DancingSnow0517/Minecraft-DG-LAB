package cn.dancingsnow.dglab.api;

import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

@Getter
@MethodsReturnNonnullByDefault
public enum ChannelType implements StringRepresentable {
    A(1),
    B(2);
    public static final StringRepresentableCodec<ChannelType> CODEC = StringRepresentable.fromEnum(ChannelType::values);
    private final int typeNumber;

    ChannelType(int type) {
        this.typeNumber = type;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
