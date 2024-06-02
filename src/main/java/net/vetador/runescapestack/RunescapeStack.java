package net.vetador.runescapestack;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.vetador.runescapestack.handlers.DisableModHandler;
import net.vetador.runescapestack.handlers.EnableModHandler;

public class RunescapeStack implements ClientModInitializer {

    public static final String MODID = "runescapestack";

    public static boolean featuresEnabled = false;

    @Override
    public void onInitializeClient() {
        ClientReceiveMessageEvents.GAME.register(new EnableModHandler());
        ClientLoginConnectionEvents.DISCONNECT.register(new DisableModHandler());
    }
}
