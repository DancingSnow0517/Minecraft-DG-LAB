package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.api.Connection;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.networking.DgLabPackets;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class DgLabCommand {

    private static final LiteralArgumentBuilder<CommandSourceStack> ROOT = Commands.literal(
                    DgLabMod.MODID)
            .then(Commands.literal("connect").executes(ctx -> {
                if (WebSocketServer.isRunning()) {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    String qr = "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#ws://%s:%d/%s"
                            .formatted(
                                    ConfigHolder.INSTANCE.webSocket.address,
                                    ConfigHolder.INSTANCE.webSocket.port,
                                    player.getUUID().toString());
                    PacketDistributor.sendToPlayer(player, new DgLabPackets.ShowQrCode(qr));
                    ctx.getSource().sendSuccess(() -> Component.translatable("message.dglab.scan_qr_code"), true);
                } else {
                    ctx.getSource().sendFailure(Component.translatable("message.dglab.server_not_enabled"));
                }

                return 1;
            }))
            .then(Commands.literal("disconnect").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                PacketDistributor.sendToPlayer(player, new DgLabPackets.ShowQrCode(""));
                Connection connection = ConnectionManager.getByUUID(player.getUUID());
                if (connection != null) {
                    connection.disconnect();
                    ctx.getSource()
                            .sendSuccess(() -> Component.translatable("message.dglab.disconnect"), true);
                } else {
                    ctx.getSource().sendFailure(Component.translatable("message.dglab.not_connected"));
                }
                return 1;
            }));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(ROOT);
    }
}
