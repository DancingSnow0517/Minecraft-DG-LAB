package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.Connection;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.networking.DgLabPackets;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DgLabMod.MODID)
public class DgLabMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dglab";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Gson GSON = new GsonBuilder().create();

    public DgLabMod(IEventBus modEventBus) {
        ConfigHolder.init();

        modEventBus.addListener(DgLabMod::registerPayload);

        NeoForge.EVENT_BUS.addListener(DgLabMod::onServerStarting);
        NeoForge.EVENT_BUS.addListener(DgLabMod::onServerStopping);
        NeoForge.EVENT_BUS.addListener(DgLabMod::registerCommand);
        NeoForge.EVENT_BUS.addListener(DgLabMod::onPlayerLogOut);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void registerCommand(RegisterCommandsEvent event) {
        DgLabCommand.register(event.getDispatcher());
    }

    public static void registerPayload(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        DgLabPackets.init(registrar);
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
