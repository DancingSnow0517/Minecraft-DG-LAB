package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.ChannelType;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.api.DgLabMessage;
import cn.dancingsnow.dglab.api.DgLabMessageType;
import cn.dancingsnow.dglab.util.DgLabPulseUtil;

import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;

public class DgLabKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerClasses(ClassFilter filter) {
        KubeJSPlugin.super.registerClasses(filter);
        filter.allow("cn.dancingsnow.dglab");
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        KubeJSPlugin.super.registerBindings(bindings);

        bindings.add("ChannelType", ChannelType.class);
        bindings.add("DgLabMessage", DgLabMessage.class);
        bindings.add("DgLabMessageType", DgLabMessageType.class);
        bindings.add("DgLabManager", ConnectionManager.class);
        bindings.add("DgLabPulseUtil", DgLabPulseUtil.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.registerEnumFromStringCodec(ChannelType.class, ChannelType.CODEC);
    }
}
