package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.ChannelType;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.api.DgLabMessage;
import cn.dancingsnow.dglab.api.DgLabMessageType;

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

        bindings.add("DgLabMessage", DgLabMessage.class);
        bindings.add("DgLabMessageType", DgLabMessageType.class);
        bindings.add("DgLabManager", ConnectionManager.class);
        bindings.add("ChannelType", ChannelType.class);
    }
}
