package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.server.Strength;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public class ClientData {
    @Getter
    @Setter
    @Nullable private static Strength strength;
}
