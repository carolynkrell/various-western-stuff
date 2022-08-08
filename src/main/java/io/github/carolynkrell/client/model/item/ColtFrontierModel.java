package io.github.carolynkrell.client.model.item;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.firearm.ColtFrontierItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ColtFrontierModel extends AnimatedGeoModel<ColtFrontierItem> {

    @Override
    public Identifier getModelLocation(ColtFrontierItem object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "geo/colt_frontier.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ColtFrontierItem object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "textures/item/colt_frontier.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ColtFrontierItem animatable) {
        return new Identifier(VariousWesternStuff.MOD_ID, "animations/colt_frontier.animation.json");
    }
}
