package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import dev.aidanarredondo.emergency_systems.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK, EmergencySystemsMod.MODID);

    // Fire Alarm Blocks
    public static final Supplier<Block> RESIDENTIAL_FIRE_ALARM = BLOCKS.register("residential_fire_alarm",
            () -> new ResidentialFireAlarmBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(2.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> COMMERCIAL_FIRE_ALARM_CEILING = BLOCKS.register("commercial_fire_alarm_ceiling",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(2.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> COMMERCIAL_FIRE_ALARM_WALL = BLOCKS.register("commercial_fire_alarm_wall",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(2.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> FIRE_ALARM_PULL_STATION = BLOCKS.register("fire_alarm_pull_station",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(3.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> FIRE_ALARM_CONTROL_PANEL = BLOCKS.register("fire_alarm_control_panel",
            () -> new FireAlarmControlPanelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> RESIDENTIAL_FIRE_PANEL = BLOCKS.register("residential_fire_panel",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(3.0f)
                    .sound(SoundType.METAL)));

    // Tornado Siren Blocks
    public static final Supplier<Block> TORNADO_SIREN = BLOCKS.register("tornado_siren",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f)
                    .sound(SoundType.METAL)));

    public static final Supplier<Block> TORNADO_SIREN_CONTROL_PANEL = BLOCKS.register("tornado_siren_control_panel",
            () -> new TornadoSirenControlPanelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0f)
                    .sound(SoundType.METAL)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
