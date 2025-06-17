package dev.aidanarredondo.emergency_systems.blockentities;

import dev.aidanarredondo.emergency_systems.Config;
import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CommercialFireAlarmBlockEntity extends BlockEntity {
    private boolean isAlarming = false;
    private boolean isSilenced = false;
    private boolean isTesting = false;
    private int tickCounter = 0;
    private BlockPos controlPanelPos;
    
    public CommercialFireAlarmBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COMMERCIAL_FIRE_ALARM.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        
        tickCounter++;
        
        // Check for fire every 20 ticks (1 second)
        if (tickCounter % 20 == 0 && Config.enableFireDetection && !isTesting) {
            checkForFire(level, pos);
        }
        
        // Play alarm sound every 20 ticks (1 second) when alarming
        if ((isAlarming || isTesting) && !isSilenced && tickCounter % 20 == 0 && Config.enableAlarmSounds) {
            level.playSound(null, pos, ModSounds.COMMERCIAL_FIRE_ALARM.get(), 
                    SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        
        // Stop test after 10 seconds
        if (isTesting && tickCounter % 200 == 0) {
            isTesting = false;
            setChanged();
        }
    }
    
    private void checkForFire(Level level, BlockPos pos) {
        int range = Config.alarmRange;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.FIRE) || 
                        level.getBlockState(checkPos).is(Blocks.SOUL_FIRE)) {
                        triggerAlarm();
                        return;
                    }
                }
            }
        }
    }
    
    public void triggerAlarm() {
        if (!isAlarming) {
            isAlarming = true;
            isSilenced = false;
            setChanged();
            
            // Notify control panel
            if (controlPanelPos != null && level != null) {
                BlockEntity entity = level.getBlockEntity(controlPanelPos);
                if (entity instanceof FireAlarmControlPanelBlockEntity panel) {
                    panel.triggerAlarm();
                }
            }
        }
    }
    
    public void silenceAlarm() {
        isSilenced = true;
        setChanged();
    }
    
    public void resetAlarm() {
        isAlarming = false;
        isSilenced = false;
        isTesting = false;
        setChanged();
    }
    
    public void testAlarm() {
        isTesting = true;
        tickCounter = 0; // Reset counter for test duration
        setChanged();
    }
    
    public void setControlPanel(BlockPos panelPos) {
        this.controlPanelPos = panelPos;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isAlarming", isAlarming);
        tag.putBoolean("isSilenced", isSilenced);
        tag.putBoolean("isTesting", isTesting);
        
        if (controlPanelPos != null) {
            tag.putLong("controlPanelPos", controlPanelPos.asLong());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isAlarming = tag.getBoolean("isAlarming");
        isSilenced = tag.getBoolean("isSilenced");
        isTesting = tag.getBoolean("isTesting");
        
        if (tag.contains("controlPanelPos")) {
            controlPanelPos = BlockPos.of(tag.getLong("controlPanelPos"));
        }
    }
    
    public boolean isAlarming() { return isAlarming; }
    public boolean isSilenced() { return isSilenced; }
    public boolean isTesting() { return isTesting; }
}
