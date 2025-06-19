package dev.aidanarredondo.emergency_systems.blockentities;

import dev.aidanarredondo.emergency_systems.Config;
import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.init.ModSounds;
import dev.aidanarredondo.emergency_systems.menu.FireAlarmControlPanelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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

public class FireAlarmControlPanelBlockEntity extends BlockEntity implements MenuProvider {
    private String password = "";
    private boolean isPasswordSet = false;
    private boolean isAlarming = false;
    private boolean isPanelSilenced = false;
    private boolean isAlarmSilenced = false;
    private int tickCounter = 0;
    private final List<BlockPos> linkedDevices = new ArrayList<>();

    public FireAlarmControlPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIRE_ALARM_CONTROL_PANEL.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        tickCounter++;

        // Play panel beep when alarming and not silenced
        if (isAlarming && !isPanelSilenced && tickCounter % 60 == 0) {
            level.playSound(null, pos, ModSounds.PANEL_BEEP.get(),
                    SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }

    public void openGUI(ServerPlayer player) {
        if (!isPasswordSet && Config.requirePasswordForCommercial) {
            // First time setup - need to set password
            player.displayClientMessage(Component.literal("First time setup: Set your 4-digit password"), false);
            // TODO: Open password setup GUI
        } else if (Config.requirePasswordForCommercial) {
            // TODO: Open password entry GUI
        } else {
            // Open main control panel
            player.openMenu(this, buf -> buf.writeBlockPos(this.getBlockPos()));
        }
    }

    public void setPassword(String newPassword) {
        if (newPassword.length() == 4 && newPassword.matches("\\d+")) {
            this.password = newPassword;
            this.isPasswordSet = true;
            setChanged();
        }
    }

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void panelAcknowledge() {
        isPanelSilenced = true;
        setChanged();
    }

    public void alarmSilence() {
        isAlarmSilenced = true;
        setChanged();

        // Silence all linked devices
        for (BlockPos devicePos : linkedDevices) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(devicePos);
                if (entity instanceof CommercialFireAlarmBlockEntity alarm) {
                    alarm.silenceAlarm();
                }
            }
        }
    }

    public void testSystem() {
        // Trigger test on all linked devices
        for (BlockPos devicePos : linkedDevices) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(devicePos);
                if (entity instanceof CommercialFireAlarmBlockEntity alarm) {
                    alarm.testAlarm();
                }
            }
        }
    }

    public void resetSystem() {
        isAlarming = false;
        isPanelSilenced = false;
        isAlarmSilenced = false;
        setChanged();

        // Reset all linked devices
        for (BlockPos devicePos : linkedDevices) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(devicePos);
                if (entity instanceof CommercialFireAlarmBlockEntity alarm) {
                    alarm.resetAlarm();
                }
            }
        }
    }

    public void triggerAlarm() {
        isAlarming = true;
        isPanelSilenced = false;
        isAlarmSilenced = false;
        setChanged();
    }

    public void linkDevice(BlockPos pos) {
        if (!linkedDevices.contains(pos) && linkedDevices.size() < Config.maxLinkedDevices) {
            linkedDevices.add(pos);
            setChanged();
        }
    }

    public void unlinkDevice(BlockPos pos) {
        linkedDevices.remove(pos);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("password", password);
        tag.putBoolean("isPasswordSet", isPasswordSet);
        tag.putBoolean("isAlarming", isAlarming);
        tag.putBoolean("isPanelSilenced", isPanelSilenced);
        tag.putBoolean("isAlarmSilenced", isAlarmSilenced);

        // Save linked devices
        CompoundTag linkedTag = new CompoundTag();
        for (int i = 0; i < linkedDevices.size(); i++) {
            BlockPos pos = linkedDevices.get(i);
            linkedTag.putLong("pos" + i, pos.asLong());
        }
        linkedTag.putInt("count", linkedDevices.size());
        tag.put("linkedDevices", linkedTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        password = tag.getString("password").orElse("");
        isPasswordSet = tag.getBoolean("isPasswordSet").orElse(false);
        isAlarming = tag.getBoolean("isAlarming").orElse(false);
        isPanelSilenced = tag.getBoolean("isPanelSilenced").orElse(false);
        isAlarmSilenced = tag.getBoolean("isAlarmSilenced").orElse(false);

        // Load linked devices
        linkedDevices.clear();
        if (tag.contains("linkedDevices")) {
            CompoundTag linkedTag = tag.getCompound("linkedDevices").orElse(new CompoundTag());
            int count = linkedTag.getInt("count").orElse(0);
            for (int i = 0; i < count; i++) {
                if (linkedTag.contains("pos" + i)) {
                    long posLong = linkedTag.getLong("pos" + i).orElse(0L);
                    if (posLong != 0L) {
                        // Manual conversion from long to BlockPos
                        int x = (int) (posLong >> 38);
                        int y = (int) (posLong << 52 >> 52);
                        int z = (int) (posLong << 26 >> 38);
                        linkedDevices.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Fire Alarm Control Panel");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FireAlarmControlPanelMenu(containerId, playerInventory, this);
    }

    // Getters for GUI
    public boolean isAlarming() { return isAlarming; }
    public boolean isPanelSilenced() { return isPanelSilenced; }
    public boolean isAlarmSilenced() { return isAlarmSilenced; }
    public List<BlockPos> getLinkedDevices() { return new ArrayList<>(linkedDevices); }
}
