package net.vetador.runestack;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import static com.mojang.text2speech.Narrator.LOGGER;

public class RuneStack implements ClientModInitializer {

    public static final String MODID = "runestack";

    public static boolean featuresEnabled = true;

    private KeyMapping toggleKeyBinding;

    @Override
    public void onInitializeClient() {
        toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.runestack.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "enable.runestack.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeyBinding.consumeClick()) {
                toggleEnabled();
            }
        });

    }

    private void toggleEnabled() {
        featuresEnabled = !featuresEnabled;
        LOGGER.info("RuneStack is now: " + (featuresEnabled ? "enabled" : "disabled"));
    }
}
