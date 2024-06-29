package com.example.examplemod;

import com.example.examplemod.config.ConfigHolder;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod(IEventBus modEventBus) {
        ConfigHolder.init();
    }
}
