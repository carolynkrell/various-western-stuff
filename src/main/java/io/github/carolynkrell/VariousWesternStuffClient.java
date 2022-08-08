package io.github.carolynkrell;

import io.github.carolynkrell.registry.PacketRegistry;
import io.github.carolynkrell.registry.RendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public class VariousWesternStuffClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PacketRegistry.registerS2CPackets();
        RendererRegistry.registerRenderers();
    }
}
