package dev.aidanarredondo.emergency_systems.blocks;

import dev.aidanarredondo.emergency_systems.Config;
import dev.aidanarredondo.emergency_systems.blockentities.CommercialFireAlarmBlockEntity;
import dev.aidanarredondo.emergency_systems.blockentities.FireAlarmControlPanelBlockEntity;
import dev.aidanarredondo.emergency_systems.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class FireAlarmPullStationBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public FireAlarmPullStationBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && !state.getValue(ACTIVATED)) {
            // Activate the pull station
            level.setBlock(pos, state.setValue(ACTIVATED, true), 3);

            // Play activation sound
            if (Config.enableAlarmSounds) {
                level.playSound(null, pos, ModSounds.PULL_STATION_ACTIVATE.get(),
                        SoundSource.BLOCKS, 1.0f, 1.0f);
            }

            // Trigger all fire alarms and control panels in range
            triggerFireAlarms(level, pos);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    private void triggerFireAlarms(Level level, BlockPos pullStationPos) {
        int range = Config.alarmRange;

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = pullStationPos.offset(x, y, z);
                    BlockEntity entity = level.getBlockEntity(checkPos);

                    if (entity instanceof CommercialFireAlarmBlockEntity alarm) {
                        alarm.triggerAlarm();
                    } else if (entity instanceof FireAlarmControlPanelBlockEntity panel) {
                        panel.triggerAlarm();
                    }
                }
            }
        }
    }

    public void reset(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getValue(ACTIVATED)) {
            level.setBlock(pos, state.setValue(ACTIVATED, false), 3);
        }
    }
}
