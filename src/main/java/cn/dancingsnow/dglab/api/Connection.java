package cn.dancingsnow.dglab.api;

import cn.dancingsnow.dglab.DgLabMod;
import cn.dancingsnow.dglab.util.DgLabStringUtil;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import com.google.gson.JsonSyntaxException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Connection {

    public static final Pattern strengthPatten =
            Pattern.compile("strength-(\\d+)\\+(\\d+)\\+(\\d+)\\+(\\d+)", Pattern.MULTILINE);

    private final Channel channel;
    private final Strength strength;

    @Setter
    private String clientId = "";

    @Setter
    private String targetId = "";

    public Connection(Channel channel) {
        this.channel = channel;
        this.strength = new Strength();
    }

    public void handle(String msg) {
        DgLabMod.LOGGER.info("Received: {}", msg);
        DgLabMessage message;
        try {
            message = DgLabMod.GSON.fromJson(msg, DgLabMessage.class);
            Objects.requireNonNull(message.type());
            Objects.requireNonNull(message.clientId());
            Objects.requireNonNull(message.targetId());
            Objects.requireNonNull(message.message());
        } catch (JsonSyntaxException | NullPointerException exception) {
            sendMessage(DgLabMessage.msg("", "", "403"));
            return;
        }

        switch (message.type()) {
            case BIND -> sendMessage(DgLabMessage.bind(message.clientId(), message.targetId(), "200"));
            case MSG -> {
                Matcher matcher = strengthPatten.matcher(message.message());
                if (matcher.find()) {
                    strength.setACurrentStrength(Integer.parseInt(matcher.group(1)));
                    strength.setBCurrentStrength(Integer.parseInt(matcher.group(2)));
                    strength.setAMaxStrength(Integer.parseInt(matcher.group(3)));
                    strength.setBMaxStrength(Integer.parseInt(matcher.group(4)));

                    MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
                    if (currentServer != null) {
                        ServerPlayer player =
                                currentServer.getPlayerList().getPlayer(UUID.fromString(clientId));
                        if (player != null) {
                            PacketDistributor.sendToPlayer(player, strength);
                        }
                    }
                }
            }
        }
    }

    public void sendMessage(DgLabMessage message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(String message) {
        DgLabMod.LOGGER.info("Sending message: {}", message);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public void disconnect() {
        sendMessage(new DgLabMessage(DgLabMessageType.BREAK, clientId, targetId, ""));
        channel.close();
    }

    public void reduceStrength(ChannelType type, int value) {
        sendMessage(DgLabMessage.msg(
                clientId, targetId, "strength-%d+0+%d".formatted(type.getTypeNumber(), value)));
    }

    public void addStrength(ChannelType type, int value) {
        sendMessage(DgLabMessage.msg(
                clientId, targetId, "strength-%d+1+%d".formatted(type.getTypeNumber(), value)));
    }

    public void setStrength(ChannelType type, int value) {
        sendMessage(DgLabMessage.msg(
                clientId, targetId, "strength-%d+2+%d".formatted(type.getTypeNumber(), value)));
    }

    public void addPulse(ChannelType type, String... pulse) {
        sendMessage(DgLabMessage.msg(
                clientId,
                targetId,
                "pulse-%d:%s".formatted(type.getTypeNumber(), DgLabStringUtil.toStringArray(pulse))));
    }

    public void clearPulse(ChannelType type) {
        sendMessage(DgLabMessage.msg(clientId, targetId, "clear-%s".formatted(type.getTypeNumber())));
    }
}
