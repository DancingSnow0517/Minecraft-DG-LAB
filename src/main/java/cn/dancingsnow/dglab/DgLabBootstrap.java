package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.client.DgLabClient;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(DgLabCommon.MODID)
public class DgLabBootstrap {
    public DgLabBootstrap() {
        DistExecutor.unsafeRunForDist(() -> DgLabClient::new, () -> DgLabServer::new);
    }
}
