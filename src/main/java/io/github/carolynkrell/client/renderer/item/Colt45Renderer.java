package io.github.carolynkrell.client.renderer.item;

import io.github.carolynkrell.client.model.item.Colt45Model;
import io.github.carolynkrell.item.gun.Colt45Item;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class Colt45Renderer extends GeoItemRenderer<Colt45Item> {
    public Colt45Renderer() {
        super(new Colt45Model());
    }
}
