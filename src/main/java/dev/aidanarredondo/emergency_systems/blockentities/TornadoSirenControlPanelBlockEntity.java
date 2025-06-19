package dev.aidanarredondo.emergency_systems.blockentities;

import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.menu.TornadoSirenControlPanelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TornadoSirenControlPanelBlockEntity extends BlockEntity implements MenuProvider {
    private boolean isActive = false;
    private SirenMode currentMode = SirenMode.OFF;
    private int tickCounter = 0;
    private final List<BlockPos> linkedSirens = new ArrayList<>();

    public enum SirenMode {
        OFF, ALERT, RAID, TEST
    }

    public TornadoSirenControlPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TORNADO_SIREN_CONTROL_PANEL.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        tickCounter++;

        // Handle siren operations
        if (isActive && currentMode != SirenMode.OFF) {
            // Update linked sirens
            for (BlockPos sirenPos : linkedSirens) {
                BlockEntity entity = level.getBlockEntity(sirenPos);
                if (entity instanceof TornadoSirenBlockEntity siren) {
                    siren.setMode(currentMode);
                }
            }
        }
    }

    public void openGUI(ServerPlayer player) {
        player.openMenu(this, buf -> buf.writeBlockPos(this.getBlockPos()));
    }

    public void setSirenMode(SirenMode mode) {
        this.currentMode = mode;
        this.isActive = (mode != SirenMode.OFF);
        setChanged();

        // Update all linked sirens
        for (BlockPos sirenPos : linkedSirens) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(sirenPos);
                if (entity instanceof TornadoSirenBlockEntity siren) {
                    siren.setMode(mode);
                }
            }
        }
    }

    public void soundAlert() {
        setSirenMode(SirenMode.ALERT);
    }

    public void soundRaid() {
        setSirenMode(SirenMode.RAID);
    }

    public void testSiren() {
        setSirenMode(SirenMode.TEST);
    }

    public void turnOff() {
        setSirenMode(SirenMode.OFF);
    }

    public void linkSiren(BlockPos pos) {
        if (!linkedSirens.contains(pos)) {
            linkedSirens.add(pos);
            setChanged();
        }
    }

    public void unlinkSiren(BlockPos pos) {
        linkedSirens.remove(pos);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putString("currentMode", currentMode.name());

        // Save linked sirens
        CompoundTag linkedTag = new CompoundTag();
        for (int i = 0; i < linkedSirens.size(); i++) {
            BlockPos pos = linkedSirens.get(i);
            linkedTag.putLong("pos" + i, pos.asLong());
        }
        linkedTag.putInt("count", linkedSirens.size());
        tag.put("linkedSirens", linkedTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive").orElse(false);
        String modeString = tag.getString("currentMode").orElse("");
        try {
            currentMode = !modeString.isEmpty() ? SirenMode.valueOf(modeString) : SirenMode.OFF;
        } catch (IllegalArgumentException e) {
            currentMode = SirenMode.OFF; // Default fallback
        }

        // Load linked sirens
        linkedSirens.clear();
        if (tag.contains("linkedSirens")) {
            CompoundTag linkedTag = tag.getCompound("linkedSirens").orElse(new CompoundTag());
            int count = linkedTag.getInt("count").orElse(0);
            for (int i = 0; i < count; i++) {
                if (linkedTag.contains("pos" + i)) {
                    long posLong = linkedTag.getLong("pos" + i).orElse(0L);
                    if (posLong != 0L) {
                        // Manual conversion from long to BlockPos
                        int x = (int) (posLong >> 38);
                        int y = (int) (posLong << 52 >> 52);
                        int z = (int) (posLong << 26 >> 38);
                        linkedSirens.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Tornado Siren Control Panel");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TornadoSirenControlPanelMenu(containerId, playerInventory, this);
    }

    // Getters for GUI
    public boolean isActive() { return isActive; }
    public SirenMode getCurrentMode() { return currentMode; }
    public List<BlockPos> getLinkedSirens() { return new ArrayList<>(linkedSirens); }
}
