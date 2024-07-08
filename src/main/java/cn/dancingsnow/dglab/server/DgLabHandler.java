package cn.dancingsnow.dglab.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.Optional;
import java.util.UUID;

public class DgLabHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Optional<Connection> connection = ConnectionManager.CONNECTIONS.stream()
                .filter(c -> c.getChannel().equals(ctx.channel()))
                .findFirst();
        connection.ifPresent(value -> value.handle(msg.text()));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手完成，在这里发送消息
            Optional<Connection> connection = ConnectionManager.CONNECTIONS.stream()
                    .filter(c -> c.getChannel().equals(ctx.channel()))
                    .findFirst();
            UUID uuid = UUID.randomUUID();
            connection.ifPresent(value -> {
                Attribute<String> attr = value.getChannel().attr(AttributeKey.valueOf("clientId"));
                value.setClientId(attr.get());
                value.setTargetId(uuid.toString());
                value.sendMessage(DgLabMessage.bind(uuid.toString(), "", "targetId"));
            });
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
