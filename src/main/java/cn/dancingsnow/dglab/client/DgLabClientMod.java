package cn.dancingsnow.dglab.client;

import cn.dancingsnow.dglab.DgLabMod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = DgLabMod.MODID, dist = Dist.CLIENT)
public class DgLabClientMod {

    public DgLabClientMod(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(DgLabClientMod::onRenderGui);
    }

    public static void onRenderGui(RenderGuiEvent.Post event) {
        OverlayHUD.render(event.getGuiGraphics(), event.getPartialTick());
        QrCodeHUD.render(event.getGuiGraphics(), event.getPartialTick());
    }
}
