package net.vetador.runescapestack.asm.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.vetador.runescapestack.RunescapeStack.featuresEnabled;


@Mixin(GuiGraphics.class)
public abstract class RunescapeStackMixin {

    private static final ResourceLocation COIN_1 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/one_coin.png");
    private static final ResourceLocation COIN_2 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/two_coins.png");
    private static final ResourceLocation COIN_3 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/three_coins.png");
    private static final ResourceLocation COIN_4 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/four_coins.png");
    private static final ResourceLocation COIN_5 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/five_coins.png");
    private static final ResourceLocation COIN_25 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/twenty_five_coins.png");
    private static final ResourceLocation COIN_1000 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/thousand_coins.png");
    private static final ResourceLocation COIN_10_000 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/ten_thousands_coins.png");
    private static final ResourceLocation COIN_100_000 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/hundread_thousands_coins.png");
    private static final ResourceLocation COIN_1_000_000 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/million_coins.png");
    private static final ResourceLocation COIN_10_000_000 = new ResourceLocation(net.vetador.runescapestack.RunescapeStack.MODID, "textures/item/ten_millions_coins.png");

    private static final Map<ResourceLocation, Integer> textureDimensions = new HashMap<>();

    static {
        textureDimensions.put(COIN_1, 16);
        textureDimensions.put(COIN_2, 27);
        textureDimensions.put(COIN_3, 28);
        textureDimensions.put(COIN_4, 32);
        textureDimensions.put(COIN_5, 32);
        textureDimensions.put(COIN_25, 32);
        textureDimensions.put(COIN_1000, 36);
        textureDimensions.put(COIN_10_000, 42);
        textureDimensions.put(COIN_100_000, 48);
        textureDimensions.put(COIN_1_000_000, 64);
        textureDimensions.put(COIN_10_000_000, 64);
    }
    //
    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At("HEAD"), cancellable = true)
    private void renderItem(LivingEntity livingEntity, Level level, ItemStack itemStack, int i, int j, int k, CallbackInfo ci)
    {
        if (!itemStack.isEmpty()) {
            List<Component> tooltip = itemStack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? net.minecraft.world.item.TooltipFlag.Default.ADVANCED : net.minecraft.world.item.TooltipFlag.Default.NORMAL);
            String itemName = itemStack.getHoverName().getString();
            long itemCount = getItemCountFromTooltip(tooltip);
            ResourceLocation texture = getTextureForItemCount(itemCount);
            Integer dimension = textureDimensions.get(texture);

            if (resourceExists(texture) && itemName.toLowerCase().contains("coins")) {
                ci.cancel();
                GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
                float scaleFactor = 16.0f / dimension;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(scaleFactor, scaleFactor, 1);
                guiGraphics.blit(texture, (int) ( i / scaleFactor), (int) (j / scaleFactor), 0, 0, dimension, dimension, dimension, dimension);
                guiGraphics.pose().popPose();
            }
        }
    }


    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "JUMP", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"), cancellable = true)
    private void onRenderItemDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String text, CallbackInfo ci) {

        float scale = 0.6f;

        if (!itemStack.isEmpty() && featuresEnabled) {
            // Get the item tooltip
            List<Component> tooltip = itemStack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? net.minecraft.world.item.TooltipFlag.Default.ADVANCED : net.minecraft.world.item.TooltipFlag.Default.NORMAL);
            // Extract the number from the tooltip
            long itemCount = getItemCountFromTooltip(tooltip);

            // Format the item count string, color, texture and dimension
            String formattedCount = formatCount(itemCount);
            int color = formatColor(itemCount);
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;


            int textWidth = font.width(formattedCount);
            float originalX = x + (16 - textWidth * scale) / 2;
            float originalY = y + 6 + 3;
            // Calculate scaled positions
            int scaledX = (int) (originalX / scale);
            int scaledY = (int) (originalY / scale) - 16;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, 1);
            guiGraphics.pose().translate(0, 0, 200.0f);
            guiGraphics.drawString(font, formattedCount, scaledX, scaledY, color, true);
            guiGraphics.pose().popPose();
            ci.cancel();
        }
    }

    private int getItemCountFromTooltip(List<Component> tooltip) {
        // Assume the number is in the first line and follows a pattern like "Feathers x3399"
        if (tooltip.toString().contains("withdraw")){
            return 1;
        }
        Pattern pattern = Pattern.compile(".* [xX](\\d+)");
        for (Component line : tooltip) {
            Matcher matcher = pattern.matcher(line.getString());
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        // Fallback to 1 if no pattern is found
        return 1;
    }

    private ResourceLocation getTextureForItemCount(long count) {
        if (count >= 10_000_000l) {
            return COIN_10_000_000;
        } else if (count >= 1_000_000l) {
            return COIN_1_000_000;
        } else if (count >= 100_000l) {
            return COIN_100_000;
        } else if (count >= 10_000l) {
            return COIN_10_000;
        } else if (count >= 1_000l) {
            return COIN_1000;
        } else if (count >= 25l) {
            return COIN_25;
        } else if (count >= 5l) {
            return COIN_5;
        } else if (count == 4l) {
            return COIN_4;
        } else if (count == 3l) {
            return COIN_3;
        } else if (count == 2l) {
            return COIN_2;
        } else if (count == 1l){
            return COIN_1;
        } else return null;
    }

    private String formatCount(long count) {
        if (count >= 10_000_000_000_000l) {
            return (count / 1_000_000_000_000l) + "T";
        } else if (count >= 10_000_000_000l) {
            return (count / 1_000_000_000l) + "B";
        } else if (count >= 10_000_000l) {
            return (count / 1_000_000l) + "M";
        } else if (count >= 100_000l) {
            return (count / 1_000l) + "K";
        } else  if (count > 1) {
            return String.valueOf(count);
        } else return null;
    }

    private int formatColor(long count) {
        if (count >= 10_000_000_000_000l) {
            return 0x00FFFF;
        } else if (count >= 10_000_000_000l) {
            return 0xFF00FF;
        } else if (count >= 10_000_000l) {
            return 0x00FF33;
        } else if (count >= 100_000l) {
            return 0xFFFFFF;
        } else {
            return 0xFFFF22;
        }
    }

    private static boolean resourceExists(ResourceLocation resourceLocation) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        Optional<?> resource = resourceManager.getResource(resourceLocation);
        return resource.isPresent();
    }
}
