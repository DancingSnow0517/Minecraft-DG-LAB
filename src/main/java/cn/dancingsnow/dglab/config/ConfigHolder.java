package cn.dancingsnow.dglab.config;

import cn.dancingsnow.dglab.DgLabCommon;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = DgLabCommon.MODID)
public class ConfigHolder {

    public static ConfigHolder INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml())
                    .getConfigInstance();
        }
    }

    @Configurable
    @Configurable.Comment({"WebSocket related settings"})
    public WebSocketConfigs webSocket = new WebSocketConfigs();

    @Configurable
    @Configurable.Comment({"Client related settings"})
    public ClientConfigs client = new ClientConfigs();

    public static class WebSocketConfigs {
        @Configurable
        @Configurable.Comment({"If true, will start WebSocket server", "Default: false"})
        public boolean enabled = false;

        @Configurable
        @Configurable.Comment({
            "The address for the DgLab-App to connect",
            "Default: 127.0.0.1",
            "In most cases, this is same with the address you use to connect to the MC server"
        })
        public String address = "127.0.0.1";

        @Configurable
        @Configurable.Comment({"The port for the DgLab ws server port number", "Default: 8080"})
        @Configurable.Range(min = 1, max = 65535)
        public int port = 8080;
    }

    public static class ClientConfigs {
        @Configurable
        @Configurable.Comment({"If true, will show current strength in hud", "Default: true"})
        public boolean enabled = true;

        @Configurable
        @Configurable.Comment({"The Gui Hud Scale", "Default: 1"})
        public float hudScale = 1;

        @Configurable
        @Configurable.Comment({"The gui hud x position", "Default: 25"})
        public int hudX = 25;

        @Configurable
        @Configurable.Comment({"The gui hud y position", "Default: 25"})
        public int hudY = 25;

        @Configurable
        @Configurable.Comment({"If true, will show the max strength in hud", "Default: false"})
        public boolean showMaxStrength = false;
    }
}
