package io.github.carolynkrell.client.model.item;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.gun.Colt45Item;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Colt45Model extends AnimatedGeoModel<Colt45Item> {

    @Override
    public Identifier getModelLocation(Colt45Item object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "geo/colt_45.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Colt45Item object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "textures/item/colt_45.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Colt45Item animatable) {
        return new Identifier(VariousWesternStuff.MOD_ID, "animations/colt_45.animation.json");
    }
}
