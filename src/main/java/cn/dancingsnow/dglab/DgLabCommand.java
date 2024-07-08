package cn.dancingsnow.dglab;

import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.server.WebSocketServer;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Player;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DgLabCommand {

    private static final LiteralArgumentBuilder<CommandSourceStack> ROOT = Commands.literal(
                    DgLabMod.MODID)
            .then(Commands.literal("connect").executes(ctx -> {
                if (WebSocketServer.isRunning()) {
                    Player player = ctx.getSource().getPlayerOrException();
                    String qr = "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#ws://%s:%d/%s"
                            .formatted(
                                    ConfigHolder.INSTANCE.address,
                                    ConfigHolder.INSTANCE.port,
                                    player.getUUID().toString());
                    ctx.getSource()
                            .sendSuccess(
                                    () -> Component.translatable("message.dglab.click_to_show_qr_code")
                                            .withStyle(style -> style
                                                    .withColor(ChatFormatting.AQUA)
                                                    .withHoverEvent(new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Component.translatable("message.dglab.click_to_show_qr_code")))
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.OPEN_URL,
                                                            "https://api.qrtool.cn/?text=%s"
                                                                    .formatted(URLEncoder.encode(qr, StandardCharsets.UTF_8))))),
                                    true);
                } else {
                    ctx.getSource().sendFailure(Component.translatable("message.dglab.server_not_enabled"));
                }

                return 1;
            }));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(ROOT);
    }
}
