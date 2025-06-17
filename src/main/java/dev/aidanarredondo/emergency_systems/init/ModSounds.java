package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, EmergencySystemsMod.MODID);

    public static final Supplier<SoundEvent> RESIDENTIAL_FIRE_ALARM = SOUND_EVENTS.register("residential_fire_alarm",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "residential_fire_alarm")));

    public static final Supplier<SoundEvent> COMMERCIAL_FIRE_ALARM = SOUND_EVENTS.register("commercial_fire_alarm",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "commercial_fire_alarm")));

    public static final Supplier<SoundEvent> TORNADO_SIREN_ALERT = SOUND_EVENTS.register("tornado_siren_alert",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "tornado_siren_alert")));

    public static final Supplier<SoundEvent> TORNADO_SIREN_RAID = SOUND_EVENTS.register("tornado_siren_raid",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "tornado_siren_raid")));

    public static final Supplier<SoundEvent> PANEL_BEEP = SOUND_EVENTS.register("panel_beep",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "panel_beep")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
