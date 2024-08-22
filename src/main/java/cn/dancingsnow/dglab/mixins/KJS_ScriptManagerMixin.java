package cn.dancingsnow.dglab.mixins;

import cn.dancingsnow.dglab.DgLabCommon;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Shadow
    @Final
    public ScriptType scriptType;

    @Inject(
            method = "loadFromDirectory",
            at = @At(value = "INVOKE", target = "Ljava/io/OutputStream;write([B)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onWriteExampleScript(CallbackInfo ci, OutputStream out) throws IOException {
        if (scriptType != ScriptType.SERVER) return;
        Optional<? extends ModContainer> optional =
                ModList.get().getModContainerById(DgLabCommon.MODID);
        optional.ifPresent(container -> {
            Path example = container.getModInfo().getOwningFile().getFile().findResource("example.js");
            try {
                out.write(Files.readAllBytes(example));
            } catch (IOException e) {
                DgLabCommon.LOGGER.error("Failed to write example script to {}", scriptType.path, e);
            }
        });
    }
}
