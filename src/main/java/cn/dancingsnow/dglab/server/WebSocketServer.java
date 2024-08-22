package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.DgLabCommon;
import cn.dancingsnow.dglab.config.ConfigHolder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.Getter;

public class WebSocketServer {

    @Getter
    private static volatile boolean running = false;

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static volatile Channel serverChannel;

    @SuppressWarnings("BusyWait")
    public static void run() {
        try {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));

                            pipeline.addLast(new DgLabHandlerAdapter());

                            pipeline.addLast(new WebSocketServerProtocolHandler(
                                    "/", "/", true, 65536 * 10, true, true, true, Long.MAX_VALUE));
                            pipeline.addLast(new DgLabHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            new Thread(() -> {
                        try {
                            ChannelFuture f =
                                    bootstrap.bind(ConfigHolder.INSTANCE.webSocket.port).sync();
                            DgLabCommon.LOGGER.info(
                                    "DgLab WebSocket server start in port {}", ConfigHolder.INSTANCE.webSocket.port);
                            serverChannel = f.channel();
                            running = true;

                            while (running) {
                                Thread.sleep(1000);
                            }

                            serverChannel.close().sync();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            if (bossGroup != null) {
                                try {
                                    bossGroup.shutdownGracefully().sync();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                            if (workerGroup != null) {
                                try {
                                    workerGroup.shutdownGracefully().sync();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    })
                    .start();
        } catch (Exception e) {
            DgLabCommon.LOGGER.error(e.getMessage(), e);
        }
    }

    public static void stop() {
        if (running && serverChannel != null) {
            serverChannel.close();
            running = false;
            DgLabCommon.LOGGER.info("DgLab WebSocket server stopped");
        }
    }
}
