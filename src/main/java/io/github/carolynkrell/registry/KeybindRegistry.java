package io.github.carolynkrell.registry;

import io.github.carolynkrell.accessor.PlayerEntityAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {

    public static KeyBinding RELOAD_KEY;

    public static void registerKeybinds() {
        RELOAD_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.various_western_stuff.reload",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.various_western_stuff.generic"
        ));
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (RELOAD_KEY.isPressed()) {
                if (client.player != null && !((PlayerEntityAccessor) client.player).isAimingDownSights()) {

                }
            }
        });
    }
}
