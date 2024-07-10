package cn.dancingsnow.dglab.api;

import net.minecraft.world.entity.player.Player;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectionManager {
    public static final Set<Connection> CONNECTIONS = new CopyOnWriteArraySet<>();

    @Nullable public static Connection getByPlayer(@NotNull Player player) {
        return getByUUID(player.getUUID());
    }

    @Nullable public static Connection getByUUID(UUID uuid) {
        Optional<Connection> connection = CONNECTIONS.stream()
                .filter(c -> c.getClientId().equals(uuid.toString()))
                .findFirst();
        return connection.orElse(null);
    }

    @Nullable public static Connection getByChannel(@NotNull Channel channel) {
        Optional<Connection> connection = ConnectionManager.CONNECTIONS.stream()
                .filter(c -> c.getChannel().equals(channel))
                .findFirst();
        return connection.orElse(null);
    }

    public static void sendToAll(DgLabMessage message) {
        CONNECTIONS.forEach(connection -> connection.sendMessage(message));
    }
}
