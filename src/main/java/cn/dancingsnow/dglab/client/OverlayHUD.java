package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.config.ConfigHolder;
import cn.dancingsnow.dglab.server.Strength;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.vertex.PoseStack;

public class OverlayHUD {
    public static void render(GuiGraphics graphics, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Strength strength = ClientData.getStrength();
        if (!ConfigHolder.INSTANCE.client.enabled
                || strength == null
                || mc.getDebugOverlay().showDebugScreen()) {
            return;
        }
        Font font = mc.font;
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        PoseStack poseStack = graphics.pose();

        int x = ConfigHolder.INSTANCE.client.hudX;
        int y = ConfigHolder.INSTANCE.client.hudY;
        float scale = ConfigHolder.INSTANCE.client.hudScale;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        String text = "Test HUD Text";
        int textWidth = font.width(text);
        //        graphics.drawString(font, Component.translatable("gui."), x, y, 0xDC143C);
        graphics.drawCenteredString(font, "A", x, y, 0x55FF55);
        graphics.drawCenteredString(font, "B", x + 20, y, 0x55FF55);

        graphics.drawCenteredString(
                font, String.valueOf(strength.getACurrentStrength()), x, y + 10, 0xFFE99D);
        graphics.drawCenteredString(
                font, String.valueOf(strength.getBCurrentStrength()), x + 20, y + 10, 0xFFE99D);

        graphics.drawCenteredString(
                font, String.valueOf(strength.getAMaxStrength()), x, y + 20, 0xDC143C);
        graphics.drawCenteredString(
                font, String.valueOf(strength.getBMaxStrength()), x + 20, y + 20, 0xDC143C);

        poseStack.popPose();
    }
}
