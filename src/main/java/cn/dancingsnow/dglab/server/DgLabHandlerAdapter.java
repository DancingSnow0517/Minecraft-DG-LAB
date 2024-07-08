package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.DgLabMod;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

import java.util.Optional;

class DgLabHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {

            String clientId = request.uri();
            if (clientId.startsWith("/")) {
                clientId = clientId.substring(1);
            }
            ctx.channel().attr(AttributeKey.valueOf("clientId")).set(clientId);
            System.out.println(clientId);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Connection connection = new Connection(ctx.channel());
        ConnectionManager.CONNECTIONS.add(connection);
        DgLabMod.LOGGER.info("new DgLab connected.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Optional<Connection> connection = ConnectionManager.CONNECTIONS.stream()
                .filter(c -> c.getChannel().equals(ctx.channel()))
                .findFirst();
        connection.ifPresent(value -> {
            ConnectionManager.CONNECTIONS.remove(value);
            DgLabMod.LOGGER.info("DgLab disconnected, clientId: {}", value.getClientId());
        });
    }
}
