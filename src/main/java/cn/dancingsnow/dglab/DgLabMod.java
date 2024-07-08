package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

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
        NeoForge.EVENT_BUS.addListener(DgLabMod::onShutdown);
        NeoForge.EVENT_BUS.addListener(DgLabMod::registerCommand);
    }

    public static void registerCommand(RegisterCommandsEvent event) {
        DgLabCommand.register(event.getDispatcher());
    }

    public static void onStartUp(FMLCommonSetupEvent event) {
        WebSocketServer.run();
    }

    public static void onShutdown(GameShuttingDownEvent event) {
        WebSocketServer.stop();
    }
}
