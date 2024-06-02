package net.vetador.runescapestack.handlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;

import static net.vetador.runescapestack.RunescapeStack.*;
import static com.mojang.text2speech.Narrator.LOGGER;

public class DisableModHandler implements ClientLoginConnectionEvents.Disconnect {



    @Override
    public void onLoginDisconnect(ClientHandshakePacketListenerImpl handler, Minecraft client) {
        featuresEnabled = false;
        LOGGER.info("Runescape stack features disabled.");
    }
}
