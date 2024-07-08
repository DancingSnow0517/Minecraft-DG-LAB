package cn.dancingsnow.dglab.server;

import com.google.gson.annotations.SerializedName;

public enum DgLabMessageType {
    @SerializedName("heartbeat")
    HEARTBEAT,
    @SerializedName("bind")
    BIND,
    @SerializedName("msg")
    MSG,
    @SerializedName("break")
    BREAK,
    @SerializedName("error")
    ERROR
}
