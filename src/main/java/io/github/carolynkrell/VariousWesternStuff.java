package io.github.carolynkrell;

import io.github.carolynkrell.registry.ItemRegistry;
import io.github.carolynkrell.registry.PacketRegistry;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;

public class VariousWesternStuff implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "various_western_stuff";
    public static final String MOD_NAME = "VariousWesternStuff";

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");

        ItemRegistry.registerItems();
        PacketRegistry.registerC2SPackets();
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}