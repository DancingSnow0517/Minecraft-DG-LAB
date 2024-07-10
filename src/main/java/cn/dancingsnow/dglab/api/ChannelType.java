package cn.dancingsnow.dglab.api;

import lombok.Getter;

@Getter
public enum ChannelType {
    A(1),
    B(2);
    private final int typeNumber;

    ChannelType(int type) {
        this.typeNumber = type;
    }
}
