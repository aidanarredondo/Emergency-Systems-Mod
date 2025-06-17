package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, EmergencySystemsMod.MODID);

    // Block Items
    public static final Supplier<Item> RESIDENTIAL_FIRE_ALARM = ITEMS.register("residential_fire_alarm",
            () -> new BlockItem(ModBlocks.RESIDENTIAL_FIRE_ALARM.get(), new Item.Properties()));

    public static final Supplier<Item> COMMERCIAL_FIRE_ALARM_CEILING = ITEMS.register("commercial_fire_alarm_ceiling",
            () -> new BlockItem(ModBlocks.COMMERCIAL_FIRE_ALARM_CEILING.get(), new Item.Properties()));

    public static final Supplier<Item> COMMERCIAL_FIRE_ALARM_WALL = ITEMS.register("commercial_fire_alarm_wall",
            () -> new BlockItem(ModBlocks.COMMERCIAL_FIRE_ALARM_WALL.get(), new Item.Properties()));

    public static final Supplier<Item> FIRE_ALARM_PULL_STATION = ITEMS.register("fire_alarm_pull_station",
            () -> new BlockItem(ModBlocks.FIRE_ALARM_PULL_STATION.get(), new Item.Properties()));

    public static final Supplier<Item> FIRE_ALARM_CONTROL_PANEL = ITEMS.register("fire_alarm_control_panel",
            () -> new BlockItem(ModBlocks.FIRE_ALARM_CONTROL_PANEL.get(), new Item.Properties()));

    public static final Supplier<Item> RESIDENTIAL_FIRE_PANEL = ITEMS.register("residential_fire_panel",
            () -> new BlockItem(ModBlocks.RESIDENTIAL_FIRE_PANEL.get(), new Item.Properties()));

    public static final Supplier<Item> TORNADO_SIREN = ITEMS.register("tornado_siren",
            () -> new BlockItem(ModBlocks.TORNADO_SIREN.get(), new Item.Properties()));

    public static final Supplier<Item> TORNADO_SIREN_CONTROL_PANEL = ITEMS.register("tornado_siren_control_panel",
            () -> new BlockItem(ModBlocks.TORNADO_SIREN_CONTROL_PANEL.get(), new Item.Properties()));

    // Tools
    public static final Supplier<Item> DEVICE_LINKING_TOOL = ITEMS.register("device_linking_tool",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> GOVERNMENT_KEY = ITEMS.register("government_key",
            () -> new Item(new Item.Properties().stacksTo(1)));

    // Crafting Components
    public static final Supplier<Item> ELECTRONIC_CIRCUIT = ITEMS.register("electronic_circuit",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SPEAKER_COMPONENT = ITEMS.register("speaker_component",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SMOKE_DETECTOR_SENSOR = ITEMS.register("smoke_detector_sensor",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ALARM_HORN = ITEMS.register("alarm_horn",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CONTROL_BOARD = ITEMS.register("control_board",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PLASTIC_CASING = ITEMS.register("plastic_casing",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> METAL_HOUSING = ITEMS.register("metal_housing",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
