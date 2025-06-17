package dev.aidanarredondo.emergency_systems.menu;

import dev.aidanarredondo.emergency_systems.blockentities.FireAlarmControlPanelBlockEntity;
import dev.aidanarredondo.emergency_systems.init.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class FireAlarmControlPanelMenu extends AbstractContainerMenu {
    private final FireAlarmControlPanelBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    public FireAlarmControlPanelMenu(int containerId, Inventory playerInventory, FireAlarmControlPanelBlockEntity blockEntity) {
        super(ModMenuTypes.FIRE_ALARM_CONTROL_PANEL.get(), containerId);
        this.blockEntity = blockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, blockEntity.getBlockState().getBlock());
    }

    public FireAlarmControlPanelBlockEntity getBlockEntity() {
        return blockEntity;
    }

    // Methods for GUI buttons
    public void panelAcknowledge() {
        blockEntity.panelAcknowledge();
    }

    public void alarmSilence() {
        blockEntity.alarmSilence();
    }

    public void testSystem() {
        blockEntity.testSystem();
    }

    public void resetSystem() {
        blockEntity.resetSystem();
    }
}
