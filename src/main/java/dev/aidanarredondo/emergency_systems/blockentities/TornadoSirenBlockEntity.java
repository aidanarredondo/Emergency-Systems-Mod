package dev.aidanarredondo.emergency_systems.blockentities;

import dev.aidanarredondo.emergency_systems.Config;
import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TornadoSirenBlockEntity extends BlockEntity {
    private TornadoSirenControlPanelBlockEntity.SirenMode currentMode = TornadoSirenControlPanelBlockEntity.SirenMode.OFF;
    private int tickCounter = 0;

    public TornadoSirenBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TORNADO_SIREN.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        tickCounter++;

        // Play siren sounds based on mode
        if (currentMode != TornadoSirenControlPanelBlockEntity.SirenMode.OFF && Config.enableAlarmSounds) {
            switch (currentMode) {
                case ALERT:
                    if (tickCounter % 60 == 0) { // Every 3 seconds
                        level.playSound(null, pos, ModSounds.TORNADO_SIREN_ALERT.get(),
                                SoundSource.BLOCKS, 2.0f, 1.0f);
                    }
                    break;
                case RAID:
                    if (tickCounter % 40 == 0) { // Every 2 seconds
                        level.playSound(null, pos, ModSounds.TORNADO_SIREN_RAID.get(),
                                SoundSource.BLOCKS, 2.0f, 1.0f);
                    }
                    break;
                case TEST:
                    if (tickCounter % 100 == 0) { // Every 5 seconds
                        level.playSound(null, pos, ModSounds.TORNADO_SIREN_ALERT.get(),
                                SoundSource.BLOCKS, 1.0f, 1.2f);
                    }
                    break;
                case OFF:
                default:
                    // No sound for OFF mode or unknown modes
                    break;
            }
        }
    }

    public void setMode(TornadoSirenControlPanelBlockEntity.SirenMode mode) {
        this.currentMode = mode;
        this.tickCounter = 0; // Reset counter when mode changes
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("currentMode", currentMode.name());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        String modeString = tag.getString("currentMode").orElse("");
        if (!modeString.isEmpty()) {
            try {
                currentMode = TornadoSirenControlPanelBlockEntity.SirenMode.valueOf(modeString);
            } catch (IllegalArgumentException e) {
                currentMode = TornadoSirenControlPanelBlockEntity.SirenMode.OFF; // Default fallback
            }
        } else {
            currentMode = TornadoSirenControlPanelBlockEntity.SirenMode.OFF;
        }
    }

    public TornadoSirenControlPanelBlockEntity.SirenMode getCurrentMode() {
        return currentMode;
    }
}
