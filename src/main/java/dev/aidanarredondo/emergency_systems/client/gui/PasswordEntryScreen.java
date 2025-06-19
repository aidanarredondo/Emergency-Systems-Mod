package dev.aidanarredondo.emergency_systems.client.gui;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PasswordEntryScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "textures/gui/password_entry.png");
    private final BlockPos blockPos;
    private final boolean isSetup;
    private EditBox passwordField;
    private EditBox confirmPasswordField;
    private Button submitButton;
    private Button cancelButton;
    private String errorMessage = "";

    public PasswordEntryScreen(BlockPos blockPos, boolean isSetup) {
        super(Component.literal(isSetup ? "Set Password" : "Enter Password"));
        this.blockPos = blockPos;
        this.isSetup = isSetup;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Password field
        this.passwordField = new EditBox(this.font, centerX - 75, centerY - 30, 150, 20, Component.literal("Password"));
        this.passwordField.setMaxLength(4);
        this.passwordField.setFilter(s -> s.matches("\\d*")); // Only digits
        this.addWidget(this.passwordField);

        if (isSetup) {
            // Confirm password field for setup
            this.confirmPasswordField = new EditBox(this.font, centerX - 75, centerY, 150, 20, Component.literal("Confirm Password"));
            this.confirmPasswordField.setMaxLength(4);
            this.confirmPasswordField.setFilter(s -> s.matches("\\d*")); // Only digits
            this.addWidget(this.confirmPasswordField);
        }

        // Submit button
        this.submitButton = Button.builder(Component.literal(isSetup ? "Set Password" : "Enter"),
                        button -> this.onSubmit())
                .bounds(centerX - 75, centerY + 40, 70, 20)
                .build();
        this.addRenderableWidget(this.submitButton);

        // Cancel button
        this.cancelButton = Button.builder(Component.literal("Cancel"),
                        button -> this.onClose())
                .bounds(centerX + 5, centerY + 40, 70, 20)
                .build();
        this.addRenderableWidget(this.cancelButton);

        this.setInitialFocus(this.passwordField);
    }

    private void onSubmit() {
        String password = this.passwordField.getValue();

        if (password.length() != 4) {
            this.errorMessage = "Password must be 4 digits";
            return;
        }

        if (isSetup) {
            String confirmPassword = this.confirmPasswordField.getValue();
            if (!password.equals(confirmPassword)) {
                this.errorMessage = "Passwords do not match";
                return;
            }
        }

        // Send password to server
        // TODO: Implement network packet when networking is added
        // For now, just close the screen

        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Background panel
        guiGraphics.fill(centerX - 100, centerY - 60, centerX + 100, centerY + 80, 0xFF404040);
        guiGraphics.fill(centerX - 98, centerY - 58, centerX + 98, centerY + 78, 0xFF202020);

        // Title
        Component title = isSetup ? Component.literal("Set 4-Digit Password") : Component.literal("Enter Password");
        guiGraphics.drawCenteredString(this.font, title, centerX, centerY - 50, 0xFFFFFF);

        // Labels
        guiGraphics.drawString(this.font, "Password:", centerX - 75, centerY - 42, 0xFFFFFF);
        if (isSetup) {
            guiGraphics.drawString(this.font, "Confirm:", centerX - 75, centerY - 12, 0xFFFFFF);
        }

        // Error message
        if (!errorMessage.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, errorMessage, centerX, centerY + 25, 0xFF5555);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC key
            this.onClose();
            return true;
        }
        if (keyCode == 257 || keyCode == 335) { // ENTER or NUMPAD_ENTER
            this.onSubmit();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
