package dev.aidanarredondo.emergency_systems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = EmergencySystemsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ===== ORIGINAL CONFIG OPTIONS =====
    private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    // ===== EMERGENCY SYSTEMS CONFIG OPTIONS =====
    private static final ModConfigSpec.BooleanValue ENABLE_FIRE_DETECTION = BUILDER
            .comment("Whether fire alarms should automatically detect fire blocks")
            .define("enableFireDetection", true);

    private static final ModConfigSpec.IntValue ALARM_RANGE = BUILDER
            .comment("Range in blocks for fire alarm detection")
            .defineInRange("alarmRange", 16, 1, 64);

    private static final ModConfigSpec.BooleanValue REQUIRE_GOVERNMENT_KEY = BUILDER
            .comment("Whether tornado sirens require a government key to operate")
            .define("requireGovernmentKey", true);

    private static final ModConfigSpec.IntValue SIREN_VOLUME_RANGE = BUILDER
            .comment("Range in blocks for tornado siren sound")
            .defineInRange("sirenVolumeRange", 128, 32, 512);

    private static final ModConfigSpec.BooleanValue ENABLE_ALARM_SOUNDS = BUILDER
            .comment("Whether alarms should play sounds when triggered")
            .define("enableAlarmSounds", true);

    private static final ModConfigSpec.IntValue MAX_LINKED_DEVICES = BUILDER
            .comment("Maximum number of devices that can be linked to one control panel")
            .defineInRange("maxLinkedDevices", 32, 1, 128);

    private static final ModConfigSpec.BooleanValue REQUIRE_PASSWORD_FOR_COMMERCIAL = BUILDER
            .comment("Whether commercial fire alarm panels require a password")
            .define("requirePasswordForCommercial", true);

    // ===== ADVANCED SECURITY OPTIONS =====
    private static final ModConfigSpec.IntValue PASSWORD_ATTEMPTS = BUILDER
            .comment("Maximum password attempts before lockout")
            .defineInRange("passwordAttempts", 3, 1, 10);

    // ===== SOUND SYSTEM OPTIONS =====
    private static final ModConfigSpec.BooleanValue ENABLE_VOICE_ALERTS = BUILDER
            .comment("Enable voice announcements for fire alarms")
            .define("enableVoiceAlerts", true);

    private static final ModConfigSpec.DoubleValue MASTER_VOLUME = BUILDER
            .comment("Master volume for all emergency system sounds")
            .defineInRange("masterVolume", 1.0, 0.0, 2.0);

    private static final ModConfigSpec.ConfigValue<String> DEFAULT_ALARM_PATTERN = BUILDER
            .comment("Default alarm pattern for fire alarms (TEMPORAL_3, TEMPORAL_4, CONTINUOUS, MARCH_TIME)")
            .define("defaultAlarmPattern", "TEMPORAL_3");

    private static final ModConfigSpec.ConfigValue<String> DEFAULT_SIREN_PATTERN = BUILDER
            .comment("Default siren pattern for tornado sirens (ALERT, ATTACK, WAIL, YELP, RAID)")
            .define("defaultSirenPattern", "ALERT");

    // ===== SYSTEM MAINTENANCE OPTIONS =====
    private static final ModConfigSpec.BooleanValue ENABLE_LOW_BATTERY_WARNING = BUILDER
            .comment("Enable low battery warnings for battery-powered devices")
            .define("enableLowBatteryWarning", true);

    private static final ModConfigSpec.IntValue BATTERY_LIFE_HOURS = BUILDER
            .comment("Battery life in hours for residential alarms")
            .defineInRange("batteryLifeHours", 168, 24, 8760); // 1 week to 1 year

    private static final ModConfigSpec.BooleanValue ENABLE_MAINTENANCE_MODE = BUILDER
            .comment("Enable maintenance mode for testing systems")
            .define("enableMaintenanceMode", true);

    private static final ModConfigSpec.BooleanValue ENABLE_SYSTEM_ANNOUNCEMENTS = BUILDER
            .comment("Enable system startup and shutdown announcements")
            .define("enableSystemAnnouncements", true);

    private static final ModConfigSpec.IntValue PULL_STATION_RESET_TIME = BUILDER
            .comment("Time in seconds before pull stations can be manually reset")
            .defineInRange("pullStationResetTime", 30, 10, 300);

    static final ModConfigSpec SPEC = BUILDER.build();

    // ===== ORIGINAL CONFIG VARIABLES =====
    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    // ===== EMERGENCY SYSTEMS CONFIG VARIABLES =====
    public static boolean enableFireDetection;
    public static int alarmRange;
    public static boolean requireGovernmentKey;
    public static int sirenVolumeRange;
    public static boolean enableAlarmSounds;
    public static int maxLinkedDevices;
    public static boolean requirePasswordForCommercial;

    // ===== ADVANCED CONFIG VARIABLES =====
    public static int passwordAttempts;
    public static boolean enableVoiceAlerts;
    public static double masterVolume;
    public static String defaultAlarmPattern;
    public static String defaultSirenPattern;
    public static boolean enableLowBatteryWarning;
    public static int batteryLifeHours;
    public static boolean enableMaintenanceMode;
    public static boolean enableSystemAnnouncements;
    public static int pullStationResetTime;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        // ===== ORIGINAL CONFIG LOADING =====
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemName)))
                .collect(Collectors.toSet());

        // ===== EMERGENCY SYSTEMS CONFIG LOADING =====
        enableFireDetection = ENABLE_FIRE_DETECTION.get();
        alarmRange = ALARM_RANGE.get();
        requireGovernmentKey = REQUIRE_GOVERNMENT_KEY.get();
        sirenVolumeRange = SIREN_VOLUME_RANGE.get();
        enableAlarmSounds = ENABLE_ALARM_SOUNDS.get();
        maxLinkedDevices = MAX_LINKED_DEVICES.get();
        requirePasswordForCommercial = REQUIRE_PASSWORD_FOR_COMMERCIAL.get();

        // ===== ADVANCED CONFIG LOADING =====
        passwordAttempts = PASSWORD_ATTEMPTS.get();
        enableVoiceAlerts = ENABLE_VOICE_ALERTS.get();
        masterVolume = MASTER_VOLUME.get();
        defaultAlarmPattern = DEFAULT_ALARM_PATTERN.get();
        defaultSirenPattern = DEFAULT_SIREN_PATTERN.get();
        enableLowBatteryWarning = ENABLE_LOW_BATTERY_WARNING.get();
        batteryLifeHours = BATTERY_LIFE_HOURS.get();
        enableMaintenanceMode = ENABLE_MAINTENANCE_MODE.get();
        enableSystemAnnouncements = ENABLE_SYSTEM_ANNOUNCEMENTS.get();
        pullStationResetTime = PULL_STATION_RESET_TIME.get();
    }
}
