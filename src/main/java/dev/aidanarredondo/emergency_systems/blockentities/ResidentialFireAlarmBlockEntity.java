package dev.aidanarredondo.emergency_systems.blockentities;

import dev.aidanarredondo.emergency_systems.Config;
import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ResidentialFireAlarmBlockEntity extends BlockEntity {
    private boolean isAlarming = false;
    private boolean isSilenced = false;
    private int tickCounter = 0;
    private final List<BlockPos> linkedAlarms = new ArrayList<>();
    
    public ResidentialFireAlarmBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RESIDENTIAL_FIRE_ALARM.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        
        tickCounter++;
        
        // Check for fire every 20 ticks (1 second)
        if (tickCounter % 20 == 0 && Config.enableFireDetection) {
            checkForFire(level, pos);
        }
        
        // Play alarm sound every 40 ticks (2 seconds) when alarming
        if (isAlarming && !isSilenced && tickCounter % 40 == 0 && Config.enableAlarmSounds) {
            level.playSound(null, pos, ModSounds.RESIDENTIAL_FIRE_ALARM.get(), 
                    SoundSource.BLOCKS, 1.0f, 1.0f);
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
            
            // Trigger linked alarms
            for (BlockPos linkedPos : linkedAlarms) {
                if (level != null) {
                    BlockEntity entity = level.getBlockEntity(linkedPos);
                    if (entity instanceof ResidentialFireAlarmBlockEntity linkedAlarm) {
                        linkedAlarm.triggerAlarm();
                    }
                }
            }
        }
    }
    
    public void resetAlarm() {
        isAlarming = false;
        isSilenced = false;
        setChanged();
        
        // Reset linked alarms
        for (BlockPos linkedPos : linkedAlarms) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(linkedPos);
                if (entity instanceof ResidentialFireAlarmBlockEntity linkedAlarm) {
                    linkedAlarm.resetAlarm();
                }
            }
        }
    }
    
    public void onPlayerInteract(ServerPlayer player) {
        if (isAlarming) {
            resetAlarm();
            player.sendSystemMessage(Component.literal("Fire alarm reset"));
        } else {
            player.sendSystemMessage(Component.literal("Fire alarm is not active"));
        }
    }
    
    public void linkAlarm(BlockPos pos) {
        if (!linkedAlarms.contains(pos) && linkedAlarms.size() < Config.maxLinkedDevices) {
            linkedAlarms.add(pos);
            setChanged();
        }
    }
    
    public void unlinkAlarm(BlockPos pos) {
        linkedAlarms.remove(pos);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isAlarming", isAlarming);
        tag.putBoolean("isSilenced", isSilenced);
        
        // Save linked alarms
        CompoundTag linkedTag = new CompoundTag();
        for (int i = 0; i < linkedAlarms.size(); i++) {
            BlockPos pos = linkedAlarms.get(i);
            linkedTag.putLong("pos" + i, pos.asLong());
        }
        linkedTag.putInt("count", linkedAlarms.size());
        tag.put("linkedAlarms", linkedTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isAlarming = tag.getBoolean("isAlarming");
        isSilenced = tag.getBoolean("isSilenced");
        
        // Load linked alarms
        linkedAlarms.clear();
        if (tag.contains("linkedAlarms")) {
            CompoundTag linkedTag = tag.getCompound("linkedAlarms");
            int count = linkedTag.getInt("count");
            for (int i = 0; i < count; i++) {
                long posLong = linkedTag.getLong("pos" + i);
                linkedAlarms.add(BlockPos.of(posLong));
            }
        }
    }
    
    public boolean isAlarming() {
        return isAlarming;
    }
    
    public List<BlockPos> getLinkedAlarms() {
        return new ArrayList<>(linkedAlarms);
    }
}
