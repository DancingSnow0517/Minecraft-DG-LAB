package cn.dancingsnow.dglab.mixins;

import cn.dancingsnow.dglab.DgLabMod;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Mixin(value = ScriptManager.class, remap = false)
public class KJS_ScriptManagerMixin {
    @Inject(
            method = "loadPackFromDirectory",
            at = @At(value = "INVOKE", target = "Ljava/io/OutputStream;write([B)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onWriteExampleScript(
            Path path, String name, boolean exampleFile, CallbackInfo ci, OutputStream out) {
        if (path != ScriptType.SERVER.path) return;
        Optional<? extends ModContainer> optional = ModList.get().getModContainerById(DgLabMod.MODID);
        optional.ifPresent(container -> {
            Path example = container.getModInfo().getOwningFile().getFile().findResource("example.js");
            try {
                out.write(Files.readAllBytes(example));
            } catch (IOException e) {
                DgLabMod.LOGGER.error("Failed to write example script to {}", path, e);
            }
        });
    }
}
