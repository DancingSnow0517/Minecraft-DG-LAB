package cn.dancingsnow.dglab.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.vertex.PoseStack;

public class QrCodeHUD {
    public static void render(GuiGraphics graphics, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (ClientData.getQrText() == null || mc.getDebugOverlay().showDebugScreen()) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        graphics.blit(
                ClientData.QR_CODE_ID, graphics.guiWidth() - 128, 0, 128, 128, 0, 0, 128, 128, 128, 128);

        poseStack.popPose();
    }
}
