package net.vetador.runescapestack.handlers;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.vetador.runescapestack.RunescapeStack.featuresEnabled;

public class EnableModHandler implements ClientReceiveMessageEvents.Game {

    @Override
    public void onReceiveGameMessage(Component message, boolean overlay) {
        String gameChat = message.getString();
        if (gameChat.equals("You are entering the city of Lumbridge")) {
            featuresEnabled = true;
            LOGGER.info("Runescape stack features enabled.");
        }
    }
}
