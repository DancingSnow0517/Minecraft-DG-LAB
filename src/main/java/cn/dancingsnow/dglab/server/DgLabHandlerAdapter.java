package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.DgLabCommon;
import cn.dancingsnow.dglab.api.Connection;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.networking.DgLabPackets;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

import java.util.UUID;

class DgLabHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {

            String clientId = request.uri();
            if (clientId.startsWith("/")) {
                clientId = clientId.substring(1);
            }
            ctx.channel().attr(AttributeKey.valueOf("clientId")).set(clientId);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Connection connection = new Connection(ctx.channel());
        ConnectionManager.CONNECTIONS.add(connection);
        DgLabCommon.LOGGER.info("new DgLab connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Connection connection = ConnectionManager.getByChannel(ctx.channel());
        if (connection != null) {
            ConnectionManager.CONNECTIONS.remove(connection);
            DgLabCommon.LOGGER.info("DgLab disconnected, clientId: {}", connection.getClientId());

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerPlayer player =
                        server.getPlayerList().getPlayer(UUID.fromString(connection.getClientId()));
                if (player != null) {
                    DgLabPackets.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player), new DgLabPackets.ClearStrength());
                    DgLabPackets.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player), new DgLabPackets.ShowQrCode(""));
                }
            }
        }
    }
}
