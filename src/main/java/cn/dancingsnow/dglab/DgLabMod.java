package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.client.ClientData;
import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.server.Connection;
import cn.dancingsnow.dglab.server.ConnectionManager;
import cn.dancingsnow.dglab.server.Strength;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
        modEventBus.addListener(DgLabMod::onStartUp);
        modEventBus.addListener(DgLabMod::registerPayload);
        NeoForge.EVENT_BUS.addListener(DgLabMod::onShutdown);
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
        registrar.playToClient(
                Strength.TYPE, Strength.STREAM_CODEC, (strength, ctx) -> ClientData.setStrength(strength));
    }

    public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player entity = event.getEntity();
        Connection connection = ConnectionManager.getByPlayer(entity);
        if (connection != null) {
            connection.disconnect();
        }
        ClientData.setStrength(null);
    }

    public static void onStartUp(FMLCommonSetupEvent event) {
        if (ConfigHolder.INSTANCE.webSocket.enabled) {
            WebSocketServer.run();
        }
    }

    public static void onShutdown(GameShuttingDownEvent event) {
        WebSocketServer.stop();
    }
}
