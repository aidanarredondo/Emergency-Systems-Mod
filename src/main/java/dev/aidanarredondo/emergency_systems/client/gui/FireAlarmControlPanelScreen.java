package dev.aidanarredondo.emergency_systems.client.gui;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import dev.aidanarredondo.emergency_systems.menu.FireAlarmControlPanelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FireAlarmControlPanelScreen extends AbstractContainerScreen<FireAlarmControlPanelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "textures/gui/container/fire_alarm_control_panel.png");

    public FireAlarmControlPanelScreen(FireAlarmControlPanelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        int x = this.leftPos;
        int y = this.topPos;

        // Panel Acknowledge button
        this.addRenderableWidget(Button.builder(Component.literal("Panel ACK"),
                        button -> this.menu.panelAcknowledge())
                .bounds(x + 10, y + 30, 70, 20)
                .build());

        // Alarm Silence button
        this.addRenderableWidget(Button.builder(Component.literal("Alarm Silence"),
                        button -> this.menu.alarmSilence())
                .bounds(x + 90, y + 30, 70, 20)
                .build());

        // Test button
        this.addRenderableWidget(Button.builder(Component.literal("Test"),
                        button -> this.menu.testSystem())
                .bounds(x + 10, y + 60, 70, 20)
                .build());

        // Reset button
        this.addRenderableWidget(Button.builder(Component.literal("Reset"),
                        button -> this.menu.resetSystem())
                .bounds(x + 90, y + 60, 70, 20)
                .build());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        // Use a simple colored background for now since we don't have the texture
        guiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF8B8B8B);

        // If you want to use the texture instead, use this line:
        // guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // Render status indicators
        int x = this.leftPos;
        int y = this.topPos;

        if (this.menu.getBlockEntity().isAlarming()) {
            guiGraphics.drawString(this.font, "ALARM ACTIVE", x + 10, y + 15, 0xFF0000);
        } else {
            guiGraphics.drawString(this.font, "SYSTEM NORMAL", x + 10, y + 15, 0x00FF00);
        }
    }
}
