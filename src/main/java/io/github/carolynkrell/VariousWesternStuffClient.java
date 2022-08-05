package io.github.carolynkrell;

import io.github.carolynkrell.client.renderer.armor.CowboyHatRenderer;
import io.github.carolynkrell.client.renderer.item.Colt45Renderer;
import io.github.carolynkrell.registry.ItemRegistry;
import io.github.carolynkrell.registry.PacketRegistry;
import io.github.carolynkrell.registry.RendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class VariousWesternStuffClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PacketRegistry.registerS2CPackets();
        RendererRegistry.registerRenderers();
    }
}
