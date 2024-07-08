package cn.dancingsnow.dglab.config;

import cn.dancingsnow.dglab.DgLabMod;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = DgLabMod.MODID)
public class ConfigHolder {

    public static ConfigHolder INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml())
                    .getConfigInstance();
        }
    }

    @Configurable
    @Configurable.Comment({
        "The address for the DgLab-App to connect",
        "Default: 127.0.0.1",
        "In most cases, this is same with the address you use to connect to the MC server"
    })
    public String address = "127.0.0.1";

    @Configurable
    @Configurable.Comment({"The port for the DgLab ws server port number", "Default: 8080"})
    public int port = 8080;
}
