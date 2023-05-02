package grojdg.hih;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HeartInHeartsHudOverlay implements HudRenderCallback {
    public static final Logger LOGGER = LoggerFactory.getLogger("hih");

    // left most coord, 16 skips crosshair
    // then each heart texture is 9 pixels wide
    // HEART_CONTAINER is 1st over (start at beginning)
    // HEART_CENTER is 5th over (start at end of 4th)
    // HEART_HALF is 6th over (start at end of 5th)
    final static int HEART_CONTAINER = 16 + (0) * 9;
    final static int HEART_CENTER = 16 + (4) * 9;

    final static int HEART_HALF = 16 + (5) * 9;

    // U coordinate of start of head in skin.png (left -> right)
    final static int HEAD_U = 8;

    // V coordinate of start of head in skin.png
    final static int HEAD_V = 8;

    // vertical spacing between entries
    final static int SPACER = 16;

    // enable single time log on first call of onHudRender
    private boolean log = false;

    private MinecraftClient client;

    private int anchorX;
    private int anchorY;

    private Map<UUID, Float> playerInfos;

    private UUID damagedPlayer;

    private double timeLastDamaged;

    private final long DAMAGE_COOLDOWN = 2250L;

    private final long HEART_JITTER_COOLDOWN = 250L;

    private final long MAX_PLAYERS = 24;

    private boolean secondUp = true;

    public HeartInHeartsHudOverlay() {
        client = MinecraftClient.getInstance();
        anchorX = 0;
        playerInfos = new HashMap<UUID, Float>();
    }

    // SUBTRACT X MOVES LEFT
    // ADD X MOVES RIGHT

    // SUBTRACT Y MOVES UP
    // ADD Y MOVES DOWN
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (client == null || this.playerInfos == null ) {
            return;
        }

        anchorY = (client.getWindow().getScaledHeight() / 2) - 20;

//        if (log) {
//            LOGGER.info("" + MinecraftClient.getInstance().getWindow().getScaleFactor());
//            log = false;
//        }

        // yoinked form PlayerListHud
        // we limit to 12 and don't sort cause that shit is private
        List<PlayerListEntry> playerListEntries = client.player.networkHandler.getListedPlayerListEntries().stream().limit(MAX_PLAYERS).toList();

        // recollect list of players to draw
        ArrayList<Integer> playersToDraw = new ArrayList();
        for (int i = 0; i < playerListEntries.size(); i++) {
            UUID uuid = playerListEntries.get(i).getProfile().getId();

            if (playerInfos.containsKey(uuid)) {
                float health = playerInfos.get(uuid);

                // 20.0f is default max health
                // 0.5f is half heart
                if (health <= 19.4f) {
                    playersToDraw.add(i);
                }
            }
        }

        // expand from center
        // calculate shift up, then draw down
        // odd number means one entry is centered on anchor
        //
        // half of items go above anchor, half below
        int vOffset = 0;
        int playerCount = playersToDraw.size();
        if (playerCount > 2) {
            vOffset = playerCount / 2;
            vOffset -= vOffset * (17 + SPACER); // starting draw point above, now just draw down
        }

        for (int i : playersToDraw) {
            UUID uuid = playerListEntries.get(i).getProfile().getId();
            float health = playerInfos.get(uuid);

            // if less than 3 seconds have passed since last damage taken
            if (Util.getMeasuringTimeMs() - timeLastDamaged < DAMAGE_COOLDOWN) {
                // and if we are at the damaged player
                if (uuid.equals(damagedPlayer)) {
                    // render as damaged
                    drawEntry(matrixStack, tickDelta, playerListEntries.get(i), health, vOffset + i * (17 + SPACER), true);
                    continue;
                }
            }

            drawEntry(matrixStack, tickDelta, playerListEntries.get(i), health, vOffset + i * (17 + SPACER), false);
        }
    }

    private void drawEntry(MatrixStack matrixStack, float tickDelta, PlayerListEntry playerListEntry, float health, int vOffset, boolean damaged) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // draw face, x is 1 to give tiny gap from left side of screen
        RenderSystem.setShaderTexture(0, playerListEntry.getSkinTexture());
        DrawableHelper.drawTexture(matrixStack, 1,anchorY + vOffset,
                12, 12, HEAD_U, HEAD_V,
                8, 8,
                64, 64); // 12 is size of rectangle we draw to, 8 is region size in the texture in pixels, 64(x64) is full size of the skin texture

        int nameColor = 0xFFFFFFFF;
        if (damaged)  { nameColor = 0xFFF70F0F; }

        // draw name slightly higher than face
        // (note) docs said to use the client textRenderer but this one has a catch:
        // text drawn too low/close to hud will be affected by text effects for item name
        // and for chat window
        // if you simply just don't draw there, it works fine :/ / :)
        //
        // i tried to make my own textRenderer from FontManager but it was not obvious
        // how to load a font idk
        client.textRenderer.drawWithShadow(matrixStack, playerListEntry.getProfile().getName(), anchorX + 1 + 16, anchorY - 1 + vOffset, nameColor);

        // hearts are drawn 15 over because they look better if 1 unit to the left of the name
        RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE); // this bitch got allat so we gotta cut it up

        health = (float)Math.ceil(health);

        boolean animate = false;

        // if damaged we animate the hearts a lil
        if (damaged && Util.getMeasuringTimeMs() - timeLastDamaged < HEART_JITTER_COOLDOWN) {
            animate = true;
        }

        // containers
        for (int i = 0; i < 10; i++) {
            int raiseAmount = animate ? i % 2 == 0 ? (secondUp ? 2 : 0) : (secondUp ? 0 : 2) : 0;
            // smaller function signature that assumes a 256x256 texture which is our icons.png
            DrawableHelper.drawTexture(matrixStack, anchorX + 1 + 15 + (i * 9), anchorY + 8 + vOffset - raiseAmount, HEART_CONTAINER, 0, 9, 9);
            // gap + face + gap + increment
        }
        // containers

        // centers
        for (int i = 0; i < (((int)health - (int)health % 2) / 2); i++) {
            int raiseAmount = animate ? i % 2 == 0 ? (secondUp ? 2 : 0) : (secondUp ? 0 : 2) : 0;
            DrawableHelper.drawTexture(matrixStack, anchorX + 1 + 15 + (i * 9), anchorY + 8 + vOffset - raiseAmount, HEART_CENTER, 0, 9, 9);
        }
        // centers

        // draw half heart
        int raiseAmount = animate ? (((int)health / 2) % 2 == 0) ? (secondUp ? 2 : 0) : (secondUp ? 0 : 2) : 0;
        if (health % 2 != 0)
            DrawableHelper.drawTexture(matrixStack, anchorX + 1 + 15 + (((int)health / 2) * 9), anchorY + 8 + vOffset - raiseAmount, HEART_HALF, 0, 9, 9);
    }

    public void setPlayerInfos(Map<UUID, Float> playerInfos) {
        this.playerInfos = playerInfos;
    }

    public void setDamagedPlayer(UUID damagedPlayer) {
        this.damagedPlayer = damagedPlayer;
    }

    public void setHealth(UUID uuid, float health) {
        playerInfos.put(uuid, health);
    }

    public void setTimeLastDamaged(long time) {
        this.timeLastDamaged = time;
    }

    public void setSecondUp(boolean secondUp) { this.secondUp = secondUp; }

    public boolean getSecondUp() { return this.secondUp; }
}
