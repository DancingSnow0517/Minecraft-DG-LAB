package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.DgLabCommon;
import cn.dancingsnow.dglab.api.Strength;
import cn.dancingsnow.dglab.util.QRCodeUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.zxing.WriterException;
import com.mojang.blaze3d.platform.NativeImage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class ClientData {
    public static final ResourceLocation QR_CODE_ID = DgLabCommon.id("qr_code");
    private static final Minecraft mc = Minecraft.getInstance();

    @Getter
    @Setter
    @Nullable private static Strength strength;

    @Getter
    @Nullable private static String qrText;

    public static void setQrText(@NotNull String qrText) {
        if (!qrText.isEmpty()) {
            ClientData.qrText = qrText;
            try {
                NativeImage image = NativeImage.read(QRCodeUtil.generateQRCode(qrText));
                DynamicTexture dynamicTexture = new DynamicTexture(image);
                mc.getTextureManager().register(QR_CODE_ID, dynamicTexture);
            } catch (IOException | WriterException e) {
                DgLabCommon.LOGGER.error(e.getMessage(), e);
            }
        } else {
            ClientData.qrText = null;
        }
    }

    public static void clear() {
        ClientData.strength = null;
        ClientData.qrText = null;
    }
}
