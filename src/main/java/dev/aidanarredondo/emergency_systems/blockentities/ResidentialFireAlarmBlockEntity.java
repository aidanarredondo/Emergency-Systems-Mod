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
                    BlockState state = level.getBlockState(checkPos);

                    // Check for fire blocks
                    if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
                        triggerAlarm();
                        return;
                    }

                    // Check for lava (fire hazard)
                    if (state.is(Blocks.LAVA) || state.is(Blocks.LAVA_CAULDRON)) {
                        triggerAlarm();
                        return;
                    }

                    // Check for campfires
                    if (state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
                        // Only trigger if campfire is lit
                        if (state.getValue(net.minecraft.world.level.block.CampfireBlock.LIT)) {
                            triggerAlarm();
                            return;
                        }
                    }

                    // Check for magma blocks (heat source)
                    if (state.is(Blocks.MAGMA_BLOCK)) {
                        triggerAlarm();
                        return;
                    }

                    // Check for burning furnaces and similar blocks
                    if (state.is(Blocks.FURNACE) || state.is(Blocks.BLAST_FURNACE) ||
                            state.is(Blocks.SMOKER)) {
                        // Only trigger if furnace is lit/burning
                        if (state.getValue(net.minecraft.world.level.block.FurnaceBlock.LIT)) {
                            triggerAlarm();
                            return;
                        }
                    }

                    // Check for entities on fire in the area
                    if (checkForBurningEntities(level, checkPos)) {
                        triggerAlarm();
                        return;
                    }
                }
            }
        }
    }

    private boolean checkForBurningEntities(Level level, BlockPos pos) {
        // Check for entities that are on fire within a 1-block radius of this position
        return level.getEntitiesOfClass(net.minecraft.world.entity.Entity.class,
                        new net.minecraft.world.phys.AABB(pos).inflate(1.0))
                .stream()
                .anyMatch(entity -> entity.isOnFire() || entity.getRemainingFireTicks() > 0);
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
            player.displayClientMessage(Component.literal("Fire alarm reset"), false);
        } else {
            player.displayClientMessage(Component.literal("Fire alarm is not active"), false);
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
        isAlarming = tag.getBoolean("isAlarming").orElse(false);
        isSilenced = tag.getBoolean("isSilenced").orElse(false);

        // Load linked alarms
        linkedAlarms.clear();
        if (tag.contains("linkedAlarms")) {
            CompoundTag linkedTag = tag.getCompound("linkedAlarms").orElse(new CompoundTag());
            int count = linkedTag.getInt("count").orElse(0);
            for (int i = 0; i < count; i++) {
                if (linkedTag.contains("pos" + i)) {
                    long posLong = linkedTag.getLong("pos" + i).orElse(0L);
                    if (posLong != 0L) {
                        // Manual conversion from long to BlockPos
                        int x = (int) (posLong >> 38);
                        int y = (int) (posLong << 52 >> 52);
                        int z = (int) (posLong << 26 >> 38);
                        linkedAlarms.add(new BlockPos(x, y, z));
                    }
                }
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
