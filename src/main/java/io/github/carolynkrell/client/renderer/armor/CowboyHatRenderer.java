package io.github.carolynkrell.client.renderer.armor;

import io.github.carolynkrell.client.model.armor.CowboyHatModel;
import io.github.carolynkrell.item.armor.CowboyHatArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class CowboyHatRenderer extends GeoArmorRenderer<CowboyHatArmorItem> {
    public CowboyHatRenderer() {
        super(new CowboyHatModel());
    }
}
