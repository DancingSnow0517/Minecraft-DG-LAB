package cn.dancingsnow.dglab.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class DgLabMixinPlugin implements IMixinConfigPlugin {

    private static boolean isKJSLoaded = false;

    @SuppressWarnings("SameParameterValue")
    private boolean isLoaded(String className) {
        return DgLabMixinPlugin.class.getClassLoader().getResource(className) != null;
    }

    @Override
    public void onLoad(String mixinPackage) {
        isKJSLoaded = isLoaded("dev/latvian/mods/kubejs/KubeJS.class");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return isKJSLoaded || !mixinClassName.contains("KJS_");
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(
            String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(
            String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
