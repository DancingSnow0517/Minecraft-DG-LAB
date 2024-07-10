package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.api.Connection;
import cn.dancingsnow.dglab.api.ConnectionManager;
import cn.dancingsnow.dglab.api.DgLabMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.UUID;

public class DgLabHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Connection connection = ConnectionManager.getByChannel(ctx.channel());
        if (connection != null) {
            connection.handle(msg.text());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手完成，在这里发送消息
            Connection connection = ConnectionManager.getByChannel(ctx.channel());
            UUID uuid = UUID.randomUUID();
            if (connection != null) {
                Attribute<String> attr = connection.getChannel().attr(AttributeKey.valueOf("clientId"));
                connection.setClientId(attr.get());
                connection.setTargetId(uuid.toString());
                connection.sendMessage(DgLabMessage.bind(uuid.toString(), "", "targetId"));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
