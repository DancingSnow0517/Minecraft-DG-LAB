package cn.dancingsnow.dglab.server;

import cn.dancingsnow.dglab.DgLabMod;
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

public class WebSocketServer {

    private static volatile boolean running = true;
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static volatile Channel serverChannel;

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
                            ChannelFuture f = bootstrap.bind(ConfigHolder.INSTANCE.port).sync();
                            DgLabMod.LOGGER.info(
                                    "DgLab WebSocket server start in port {}", ConfigHolder.INSTANCE.port);
                            serverChannel = f.channel();

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
            DgLabMod.LOGGER.error(e.getMessage(), e);
        }
    }

    public static void stop() {
        running = false;
        if (serverChannel != null) {
            serverChannel.close();
        }
        DgLabMod.LOGGER.info("DgLab WebSocket server stopped");
    }
}
