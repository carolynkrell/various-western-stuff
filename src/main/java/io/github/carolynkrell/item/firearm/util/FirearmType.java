package io.github.carolynkrell.item.firearm.util;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.carolynkrell.util.PlayerFirearmPose;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class FirearmType {
    public static BipedEntityModel.ArmPose ADS_POSE;
    public static BipedEntityModel.ArmPose RELOAD_POSE;

    public FirearmType(String key) {
        ADS_POSE = ClassTinkerers.getEnum(BipedEntityModel.ArmPose.class, key + "_FIREARM_ADS");
        RELOAD_POSE = ClassTinkerers.getEnum(BipedEntityModel.ArmPose.class, key + "_FIREARM_RELOAD");
    }

    public BipedEntityModel.ArmPose getPose(PlayerFirearmPose playerFirearmPose) {
        return switch (playerFirearmPose) {
            case ADS -> ADS_POSE;
            case RELOAD -> RELOAD_POSE;
            case NONE -> BipedEntityModel.ArmPose.ITEM;
        };
    }
}
