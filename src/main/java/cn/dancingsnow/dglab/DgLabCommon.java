package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.Connection;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.networking.DgLabPackets;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file

public class DgLabCommon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dglab";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Gson GSON = new GsonBuilder().create();

    public DgLabCommon() {

        ConfigHolder.init();
        DgLabPackets.init();

        MinecraftForge.EVENT_BUS.addListener(DgLabCommon::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(DgLabCommon::onServerStopping);
        MinecraftForge.EVENT_BUS.addListener(DgLabCommon::registerCommand);
        MinecraftForge.EVENT_BUS.addListener(DgLabCommon::onPlayerLogOut);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void registerCommand(RegisterCommandsEvent event) {
        DgLabCommand.register(event.getDispatcher());
    }

    public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        Connection connection = ConnectionManager.getByPlayer(player);
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static void onServerStarting(ServerStartingEvent event) {
        if (ConfigHolder.INSTANCE.webSocket.enabled) {
            WebSocketServer.run();
        }
    }

    public static void onServerStopping(ServerStoppingEvent event) {
        if (WebSocketServer.isRunning()) {
            WebSocketServer.stop();
        }
    }
}
