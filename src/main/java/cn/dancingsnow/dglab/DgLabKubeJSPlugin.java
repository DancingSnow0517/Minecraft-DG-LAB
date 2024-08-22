package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.ChannelType;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.api.DgLabMessage;
import cn.dancingsnow.dglab.api.DgLabMessageType;
import cn.dancingsnow.dglab.util.DgLabPulseUtil;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

public class DgLabKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
        filter.allow("cn.dancingsnow.dglab");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("ChannelType", ChannelType.class);
        event.add("DgLabMessage", DgLabMessage.class);
        event.add("DgLabMessageType", DgLabMessageType.class);
        event.add("DgLabManager", ConnectionManager.class);
        event.add("DgLabPulseUtil", DgLabPulseUtil.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        super.registerTypeWrappers(type, typeWrappers);
        typeWrappers.registerSimple(ChannelType.class, ChannelType::of);
    }
}
