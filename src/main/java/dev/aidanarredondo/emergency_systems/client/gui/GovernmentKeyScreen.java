package dev.aidanarredondo.emergency_systems.client.gui;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GovernmentKeyScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "textures/gui/government_key.png");
    private final BlockPos blockPos;
    private Button authorizeButton;
    private Button cancelButton;
    private String statusMessage = "";

    public GovernmentKeyScreen(BlockPos blockPos) {
        super(Component.literal("Government Authorization Required"));
        this.blockPos = blockPos;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Authorize button
        this.authorizeButton = Button.builder(Component.literal("Use Government Key"), 
                button -> this.onAuthorize())
                .bounds(centerX - 75, centerY + 20, 150, 20)
                .build();
        this.addRenderableWidget(this.authorizeButton);
        
        // Cancel button
        this.cancelButton = Button.builder(Component.literal("Cancel"), 
                button -> this.onClose())
                .bounds(centerX - 35, centerY + 50, 70, 20)
                .build();
        this.addRenderableWidget(this.cancelButton);
    }

    private void onAuthorize() {
        // Check if player has government key in inventory
        if (minecraft != null && minecraft.player != null) {
            boolean hasKey = minecraft.player.getInventory().contains(
                dev.aidanarredondo.emergency_systems.init.ModItems.GOVERNMENT_KEY.get().getDefaultInstance()
            );
            
            if (hasKey) {
                this.statusMessage = "Authorization successful";
                // TODO: Send authorization packet to server
                // PacketHandler.sendToServer(new GovernmentAuthPacket(blockPos));
                this.onClose();
            } else {
                this.statusMessage = "Government Key required in inventory";
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Background panel
        guiGraphics.fill(centerX - 120, centerY - 80, centerX + 120, centerY + 90, 0xFF404040);
        guiGraphics.fill(centerX - 118, centerY - 78, centerX + 118, centerY + 88, 0xFF202020);
        
        // Title
        guiGraphics.drawCenteredString(this.font, "RESTRICTED ACCESS", centerX, centerY - 65, 0xFF5555);
        guiGraphics.drawCenteredString(this.font, "Government Authorization Required", centerX, centerY - 50, 0xFFFFFF);
        
        // Warning text
        guiGraphics.drawCenteredString(this.font, "This tornado siren system requires", centerX, centerY - 30, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "government clearance to operate.", centerX, centerY - 15, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "Please present your Government Key.", centerX, centerY, 0xFFFFFF);
        
        // Status message
        if (!statusMessage.isEmpty()) {
            int color = statusMessage.contains("successful") ? 0x55FF55 : 0xFF5555;
            guiGraphics.drawCenteredString(this.font, statusMessage, centerX, centerY + 75, color);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
