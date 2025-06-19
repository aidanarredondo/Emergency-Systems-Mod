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

    // Fire Alarm Sounds
    public static final Supplier<SoundEvent> RESIDENTIAL_FIRE_ALARM = SOUND_EVENTS.register("residential_fire_alarm",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "residential_fire_alarm")));

    public static final Supplier<SoundEvent> COMMERCIAL_FIRE_ALARM = SOUND_EVENTS.register("commercial_fire_alarm",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "commercial_fire_alarm")));

    public static final Supplier<SoundEvent> FIRE_ALARM_CHIRP = SOUND_EVENTS.register("fire_alarm_chirp",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "fire_alarm_chirp")));

    // Tornado Siren Sounds
    public static final Supplier<SoundEvent> TORNADO_SIREN_ALERT = SOUND_EVENTS.register("tornado_siren_alert",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "tornado_siren_alert")));

    public static final Supplier<SoundEvent> TORNADO_SIREN_RAID = SOUND_EVENTS.register("tornado_siren_raid",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "tornado_siren_raid")));

    // System Sounds
    public static final Supplier<SoundEvent> PANEL_BEEP = SOUND_EVENTS.register("panel_beep",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "panel_beep")));

    public static final Supplier<SoundEvent> BUTTON_PRESS = SOUND_EVENTS.register("button_press",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "button_press")));

    public static final Supplier<SoundEvent> SYSTEM_STARTUP = SOUND_EVENTS.register("system_startup",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "system_startup")));

    public static final Supplier<SoundEvent> SYSTEM_SHUTDOWN = SOUND_EVENTS.register("system_shutdown",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "system_shutdown")));

    public static final Supplier<SoundEvent> LOW_BATTERY_CHIRP = SOUND_EVENTS.register("low_battery_chirp",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "low_battery_chirp")));

    // Add the missing pull station sound
    public static final Supplier<SoundEvent> PULL_STATION_ACTIVATE = SOUND_EVENTS.register("pull_station_activate",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmergencySystemsMod.MODID, "pull_station_activate")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
