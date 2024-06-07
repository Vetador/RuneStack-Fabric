package net.vetador.runestack.handlers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.vetador.runestack.RuneStack.featuresEnabled;

public class EnableModHandler {



    /*
    private void printScoreboardInfo() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            LOGGER.info("No player instance available.");
            return;
        }
        Scoreboard scoreboard = minecraft.player.getScoreboard();
        if (scoreboard == null) {
            LOGGER.info("No scoreboard instance available.");
            return;
        }
        for (Objective objective : scoreboard.getObjectives()) {
            LOGGER.info("Objective: " + objective.getDisplayName().getString());

            for (Score score : scoreboard.getPlayerScores(objective)) {
                String playerName = score.getOwner();
                int scoreValue = score.getScore();
                LOGGER.info(playerName + ": " + scoreValue);
            }
        }
    }

     */


}
