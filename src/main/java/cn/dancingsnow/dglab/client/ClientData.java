package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.api.Strength;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ClientData {
    @Getter
    @Setter
    @Nullable private static Strength strength;
}
