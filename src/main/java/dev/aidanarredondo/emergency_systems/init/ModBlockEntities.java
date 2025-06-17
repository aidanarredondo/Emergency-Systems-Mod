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
                    BlockEntityType.Builder.of(ResidentialFireAlarmBlockEntity::new,
                            ModBlocks.RESIDENTIAL_FIRE_ALARM.get()).build(null));

    public static final Supplier<BlockEntityType<FireAlarmControlPanelBlockEntity>> FIRE_ALARM_CONTROL_PANEL =
            BLOCK_ENTITIES.register("fire_alarm_control_panel", () ->
                    BlockEntityType.Builder.of(FireAlarmControlPanelBlockEntity::new,
                            ModBlocks.FIRE_ALARM_CONTROL_PANEL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
