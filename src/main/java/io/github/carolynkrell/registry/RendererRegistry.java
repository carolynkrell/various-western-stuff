package io.github.carolynkrell.registry;

import io.github.carolynkrell.client.renderer.armor.CowboyHatRenderer;
import io.github.carolynkrell.client.renderer.item.Colt45Renderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RendererRegistry {

    public static void registerRenderers() {
        GeoArmorRenderer.registerArmorRenderer(new CowboyHatRenderer(), ItemRegistry.COWBOY_HAT);
        GeoItemRenderer.registerItemRenderer(ItemRegistry.COLT_45, new Colt45Renderer());
    }
}
