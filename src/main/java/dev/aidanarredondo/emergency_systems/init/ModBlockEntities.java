package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import dev.aidanarredondo.emergency_systems.blockentities.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EmergencySystemsMod.MODID);

    public static final Supplier<BlockEntityType<ResidentialFireAlarmBlockEntity>> RESIDENTIAL_FIRE_ALARM =
            BLOCK_ENTITIES.register("residential_fire_alarm", () ->
                    new BlockEntityType<>(ResidentialFireAlarmBlockEntity::new,
                            java.util.Set.of(ModBlocks.RESIDENTIAL_FIRE_ALARM.get())));

    public static final Supplier<BlockEntityType<CommercialFireAlarmBlockEntity>> COMMERCIAL_FIRE_ALARM =
            BLOCK_ENTITIES.register("commercial_fire_alarm", () ->
                    new BlockEntityType<>(CommercialFireAlarmBlockEntity::new,
                            java.util.Set.of(ModBlocks.COMMERCIAL_FIRE_ALARM_CEILING.get(),
                                    ModBlocks.COMMERCIAL_FIRE_ALARM_WALL.get())));

    public static final Supplier<BlockEntityType<FireAlarmControlPanelBlockEntity>> FIRE_ALARM_CONTROL_PANEL =
            BLOCK_ENTITIES.register("fire_alarm_control_panel", () ->
                    new BlockEntityType<>(FireAlarmControlPanelBlockEntity::new,
                            java.util.Set.of(ModBlocks.FIRE_ALARM_CONTROL_PANEL.get())));

    public static final Supplier<BlockEntityType<TornadoSirenBlockEntity>> TORNADO_SIREN =
            BLOCK_ENTITIES.register("tornado_siren", () ->
                    new BlockEntityType<>(TornadoSirenBlockEntity::new,
                            java.util.Set.of(ModBlocks.TORNADO_SIREN.get())));

    public static final Supplier<BlockEntityType<TornadoSirenControlPanelBlockEntity>> TORNADO_SIREN_CONTROL_PANEL =
            BLOCK_ENTITIES.register("tornado_siren_control_panel", () ->
                    new BlockEntityType<>(TornadoSirenControlPanelBlockEntity::new,
                            java.util.Set.of(ModBlocks.TORNADO_SIREN_CONTROL_PANEL.get())));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
