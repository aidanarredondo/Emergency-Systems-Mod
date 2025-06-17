package dev.aidanarredondo.emergency_systems.menu;

import dev.aidanarredondo.emergency_systems.blockentities.TornadoSirenControlPanelBlockEntity;
import dev.aidanarredondo.emergency_systems.init.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class TornadoSirenControlPanelMenu extends AbstractContainerMenu {
    private final TornadoSirenControlPanelBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    public TornadoSirenControlPanelMenu(int containerId, Inventory playerInventory, TornadoSirenControlPanelBlockEntity blockEntity) {
        super(ModMenuTypes.TORNADO_SIREN_CONTROL_PANEL.get(), containerId);
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

    public TornadoSirenControlPanelBlockEntity getBlockEntity() {
        return blockEntity;
    }

    // Methods for GUI buttons
    public void soundAlert() {
        blockEntity.soundAlert();
    }

    public void soundRaid() {
        blockEntity.soundRaid();
    }

    public void testSiren() {
        blockEntity.testSiren();
    }

    public void turnOff() {
        blockEntity.turnOff();
    }
}
