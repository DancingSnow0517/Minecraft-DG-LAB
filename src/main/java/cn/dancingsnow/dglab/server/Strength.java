package cn.dancingsnow.dglab.server;

import lombok.Data;

@Data
public class Strength {
    private int aCurrentStrength = 0;
    private int bCurrentStrength = 0;
    private int aMaxStrength = 0;
    private int bMaxStrength = 0;
}
