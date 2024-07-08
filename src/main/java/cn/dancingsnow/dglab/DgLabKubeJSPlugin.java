package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.server.ChannelType;
import cn.dancingsnow.dglab.server.ConnectionManager;

import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class DgLabKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerClasses(ClassFilter filter) {
        KubeJSPlugin.super.registerClasses(filter);
        filter.allow("cn.dancingsnow.dglab");
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        KubeJSPlugin.super.registerBindings(bindings);

        bindings.add("DgLabManager", ConnectionManager.class);
        bindings.add("ChannelType", ChannelType.class);
    }
}
