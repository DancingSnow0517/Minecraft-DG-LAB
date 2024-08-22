package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.DgLabCommon;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;

public class DgLabClient extends DgLabCommon {

    public DgLabClient() {
        MinecraftForge.EVENT_BUS.addListener(DgLabClient::onRenderGui);
        MinecraftForge.EVENT_BUS.addListener(DgLabClient::onLoggingOut);
    }

    public static void onRenderGui(RenderGuiEvent.Post event) {
        OverlayHUD.render(event.getGuiGraphics(), event.getPartialTick());
        QrCodeHUD.render(event.getGuiGraphics(), event.getPartialTick());
    }

    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientData.clear();
    }
}
