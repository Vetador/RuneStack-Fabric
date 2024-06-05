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
import net.vetador.runescapestack.RunescapeStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.vetador.runescapestack.RunescapeStack.featuresEnabled;


@Mixin(GuiGraphics.class)
public abstract class RunescapeStackMixin {

    private static final Map<Long, ResourceLocation> COIN_TEXTURES = new HashMap<>();

    static {
        COIN_TEXTURES.put(1l, new ResourceLocation(RunescapeStack.MODID, "textures/item/one_coin.png"));
        COIN_TEXTURES.put(2l, new ResourceLocation(RunescapeStack.MODID, "textures/item/two_coins.png"));
        COIN_TEXTURES.put(3l, new ResourceLocation(RunescapeStack.MODID, "textures/item/three_coins.png"));
        COIN_TEXTURES.put(4l, new ResourceLocation(RunescapeStack.MODID, "textures/item/four_coins.png"));
        COIN_TEXTURES.put(5l, new ResourceLocation(RunescapeStack.MODID, "textures/item/five_coins.png"));
        COIN_TEXTURES.put(100l, new ResourceLocation(RunescapeStack.MODID, "textures/item/hundread_coins.png"));
        COIN_TEXTURES.put(1000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/thousand_coins.png"));
        COIN_TEXTURES.put(10000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/ten_thousands_coins.png"));
        COIN_TEXTURES.put(100000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/hundread_thousands_coins.png"));
        COIN_TEXTURES.put(1000000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/million_coins.png"));
        COIN_TEXTURES.put(10000000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/ten_millions_coins.png"));
        COIN_TEXTURES.put(100000000l, new ResourceLocation(RunescapeStack.MODID, "textures/item/hundread_millions_coins.png"));
    }

    //
    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At("HEAD"), cancellable = true)
    private void renderItem(LivingEntity livingEntity, Level level, ItemStack itemStack, int i, int j, int k, CallbackInfo ci) {
        if (!itemStack.isEmpty() && featuresEnabled) {
            String itemName = itemStack.getHoverName().getString();
            if (itemName.contains("Coins")) {
                long itemCount = getItemCountFromTooltip(itemName);
                ResourceLocation texture = getTextureForItemCount(itemCount);
                if (resourceExists(texture)) {
                    ci.cancel();
                    GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
                    guiGraphics.pose().pushPose();
                    guiGraphics.blit(texture, i, j, 0, 0, 16, 16, 16, 16);
                    guiGraphics.pose().popPose();
                }
            }
        }
    }


    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "JUMP", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"), cancellable = true)
    private void onRenderItemDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String text, CallbackInfo ci) {

        float scale = 0.6f;
        if (!itemStack.isEmpty() && featuresEnabled) {
            String itemName = itemStack.getHoverName().getString();
            long itemCount = getItemCountFromTooltip(itemName);
            if (itemCount >= 10000 && itemCount <= 99999) scale = 0.5f;
            String formattedCount = formatCount(itemCount);
            int color = formatColor(itemCount);
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
            int textWidth = font.width(formattedCount);
            float originalX = x + (16 - textWidth * scale) / 2;
            float originalY = y + 6 + 3;
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

    private int getItemCountFromTooltip(String tooltip) {
        if (tooltip.contains("withdraw")){
            return 1;
        }
        Pattern pattern = Pattern.compile(".* [xX](\\d+)");
        Matcher matcher = pattern.matcher(tooltip);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 1;
    }

    private ResourceLocation getTextureForItemCount(long count) {
        if (count >= 100_000_000l) {
            return COIN_TEXTURES.get(100_000_000l);
        } else if (count >= 10_000_000l) {
            return COIN_TEXTURES.get(10_000_000l);
        } else if (count >= 1_000_000l) {
            return COIN_TEXTURES.get(1_000_000l);
        } else if (count >= 100_000l) {
            return COIN_TEXTURES.get(100_000l);
        } else if (count >= 10_000l) {
            return COIN_TEXTURES.get(10_000l);
        } else if (count >= 1_000l) {
            return COIN_TEXTURES.get(1_000l);
        } else if (count >= 100l) {
            return COIN_TEXTURES.get(100l);
        } else if (count >= 5l) {
            return COIN_TEXTURES.get(5l);
        } else if (count == 4l) {
            return COIN_TEXTURES.get(4l);
        } else if (count == 3l) {
            return COIN_TEXTURES.get(3l);
        } else if (count == 2l) {
            return COIN_TEXTURES.get(2l);
        } else if (count == 1l){
            return COIN_TEXTURES.get(1l);
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
            return 0x33FFFF;
        } else if (count >= 10_000_000_000l) {
            return 0xFF33FF;
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
