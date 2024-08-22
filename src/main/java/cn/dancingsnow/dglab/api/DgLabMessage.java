package cn.dancingsnow.dglab.api;

import cn.dancingsnow.dglab.DgLabCommon;

public record DgLabMessage(
        DgLabMessageType type, String clientId, String targetId, String message) {

    public String toJson() {
        return DgLabCommon.GSON.toJson(this);
    }

    public static DgLabMessage msg(String clientId, String targetId, String message) {
        return new DgLabMessage(DgLabMessageType.MSG, clientId, targetId, message);
    }

    public static DgLabMessage bind(String clientId, String targetId, String message) {
        return new DgLabMessage(DgLabMessageType.BIND, clientId, targetId, message);
    }
}
