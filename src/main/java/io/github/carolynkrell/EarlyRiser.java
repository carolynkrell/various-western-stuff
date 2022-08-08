package io.github.carolynkrell;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        String armPose = remapper.mapClassName("intermediary", "net.minecraft.class_572$class_573");
        ClassTinkerers.enumBuilder(armPose, boolean.class)
                .addEnum("ONE_HANDED_FIREARM_ADS", true)
                .addEnum("ONE_HANDED_FIREARM_RELOAD", true)
                .build();
    }
}
