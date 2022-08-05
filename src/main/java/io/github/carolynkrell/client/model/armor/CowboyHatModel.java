package io.github.carolynkrell.client.model.armor;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.armor.CowboyHatArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CowboyHatModel extends AnimatedGeoModel<CowboyHatArmorItem> {
    @Override
    public Identifier getModelLocation(CowboyHatArmorItem object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "geo/cowboy_hat.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CowboyHatArmorItem object) {
        return new Identifier(VariousWesternStuff.MOD_ID, "textures/item/cowboy_hat.png");
    }

    @Override
    public Identifier getAnimationFileLocation(CowboyHatArmorItem animatable) {
        return null;
    }
}
