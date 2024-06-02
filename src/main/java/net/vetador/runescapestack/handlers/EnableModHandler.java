package net.vetador.runescapestack.handlers;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.chat.Component;

import static net.vetador.runescapestack.RunescapeStack.*;
import static com.mojang.text2speech.Narrator.LOGGER;

public class EnableModHandler implements ClientReceiveMessageEvents.Game {

    @Override
    public void onReceiveGameMessage(Component message, boolean overlay) {
        String gameChat = message.getString();
        if (gameChat.contains("Welcome to the GamesLabs Network"))
        {
            featuresEnabled = true;
            LOGGER.info("Runescape stack features enabled.");
        }
    }
}
