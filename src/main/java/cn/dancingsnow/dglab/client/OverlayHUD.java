package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.api.Strength;
import cn.dancingsnow.dglab.config.ConfigHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class OverlayHUD {
    public static void render(GuiGraphics graphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Strength strength = ClientData.getStrength();
        if (!ConfigHolder.INSTANCE.client.enabled || strength == null || mc.options.renderDebug) {
            return;
        }
        Font font = mc.font;
        PoseStack poseStack = graphics.pose();

        int x = ConfigHolder.INSTANCE.client.hudX;
        int y = ConfigHolder.INSTANCE.client.hudY;
        float scale = ConfigHolder.INSTANCE.client.hudScale;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        int textWidth = font.width("100");

        int minX = x - (textWidth / 2) - 5;
        int maxX = x + 20 + (textWidth / 2) + 5;
        int minY = y - 5;
        int maxY = y + 10 + 12;

        if (ConfigHolder.INSTANCE.client.showMaxStrength) {
            maxY += 10;
        }

        // 背景
        graphics.fill(minX, minY, maxX, maxY, 0xC82E2E2E);

        // 框框
        int borderColor = (strength.getACurrentStrength() < strength.getAMaxStrength()
                        && strength.getBCurrentStrength() < strength.getBMaxStrength())
                ? 0xFFFFE99D
                : 0xFFDC143C;

        graphics.hLine(minX, maxX, minY, borderColor);
        graphics.hLine(minX, maxX, maxY, borderColor);
        graphics.vLine(minX, minY, maxY, borderColor);
        graphics.vLine(maxX, minY, maxY, borderColor);

        // 文字
        graphics.drawCenteredString(font, "A", x, y, 0x55FF55);
        graphics.drawCenteredString(font, "B", x + 20, y, 0x55FF55);

        graphics.drawCenteredString(
                font, String.valueOf(strength.getACurrentStrength()), x, y + 10, 0xFFE99D);
        graphics.drawCenteredString(
                font, String.valueOf(strength.getBCurrentStrength()), x + 20, y + 10, 0xFFE99D);

        if (ConfigHolder.INSTANCE.client.showMaxStrength) {
            graphics.drawCenteredString(
                    font, String.valueOf(strength.getAMaxStrength()), x, y + 20, 0xDC143C);
            graphics.drawCenteredString(
                    font, String.valueOf(strength.getBMaxStrength()), x + 20, y + 20, 0xDC143C);
        }

        poseStack.popPose();
    }
}
