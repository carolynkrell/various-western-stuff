package io.github.carolynkrell.client.renderer.item;

import io.github.carolynkrell.client.model.item.ColtFrontierModel;
import io.github.carolynkrell.item.firearm.ColtFrontierItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ColtFrontierRenderer extends GeoItemRenderer<ColtFrontierItem> {
    public ColtFrontierRenderer() {
        super(new ColtFrontierModel());
    }
}
