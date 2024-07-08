package cn.dancingsnow.dglab.server;

import net.minecraft.world.entity.player.Player;

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
}
