package dev.aidanarredondo.emergency_systems;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Emergency_systems.MODID)
public class Emergency_systems {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "emergency_systems";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Create a Deferred Register to hold Blocks which will all be registered under the "emergency_systems" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "emergency_systems" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "emergency_systems" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // ===== FIRE ALARM SYSTEM BLOCKS =====
    public static final DeferredBlock<Block> RESIDENTIAL_FIRE_ALARM = BLOCKS.registerSimpleBlock("residential_fire_alarm",
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(2.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> RESIDENTIAL_FIRE_ALARM_ITEM = ITEMS.registerSimpleBlockItem("residential_fire_alarm", RESIDENTIAL_FIRE_ALARM);

    public static final DeferredBlock<Block> COMMERCIAL_FIRE_ALARM_CEILING = BLOCKS.registerSimpleBlock("commercial_fire_alarm_ceiling",
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(2.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> COMMERCIAL_FIRE_ALARM_CEILING_ITEM = ITEMS.registerSimpleBlockItem("commercial_fire_alarm_ceiling", COMMERCIAL_FIRE_ALARM_CEILING);

    public static final DeferredBlock<Block> COMMERCIAL_FIRE_ALARM_WALL = BLOCKS.registerSimpleBlock("commercial_fire_alarm_wall",
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(2.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> COMMERCIAL_FIRE_ALARM_WALL_ITEM = ITEMS.registerSimpleBlockItem("commercial_fire_alarm_wall", COMMERCIAL_FIRE_ALARM_WALL);

    public static final DeferredBlock<Block> FIRE_ALARM_PULL_STATION = BLOCKS.registerSimpleBlock("fire_alarm_pull_station",
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(3.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> FIRE_ALARM_PULL_STATION_ITEM = ITEMS.registerSimpleBlockItem("fire_alarm_pull_station", FIRE_ALARM_PULL_STATION);

    public static final DeferredBlock<Block> FIRE_ALARM_CONTROL_PANEL = BLOCKS.registerSimpleBlock("fire_alarm_control_panel",
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> FIRE_ALARM_CONTROL_PANEL_ITEM = ITEMS.registerSimpleBlockItem("fire_alarm_control_panel", FIRE_ALARM_CONTROL_PANEL);

    public static final DeferredBlock<Block> RESIDENTIAL_FIRE_PANEL = BLOCKS.registerSimpleBlock("residential_fire_panel",
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(3.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> RESIDENTIAL_FIRE_PANEL_ITEM = ITEMS.registerSimpleBlockItem("residential_fire_panel", RESIDENTIAL_FIRE_PANEL);

    // ===== TORNADO SIREN SYSTEM BLOCKS =====
    public static final DeferredBlock<Block> TORNADO_SIREN = BLOCKS.registerSimpleBlock("tornado_siren",
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> TORNADO_SIREN_ITEM = ITEMS.registerSimpleBlockItem("tornado_siren", TORNADO_SIREN);

    public static final DeferredBlock<Block> TORNADO_SIREN_CONTROL_PANEL = BLOCKS.registerSimpleBlock("tornado_siren_control_panel",
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0f).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> TORNADO_SIREN_CONTROL_PANEL_ITEM = ITEMS.registerSimpleBlockItem("tornado_siren_control_panel", TORNADO_SIREN_CONTROL_PANEL);

    // ===== TOOLS =====
    public static final DeferredItem<Item> DEVICE_LINKING_TOOL = ITEMS.registerSimpleItem("device_linking_tool",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> GOVERNMENT_KEY = ITEMS.registerSimpleItem("government_key",
            new Item.Properties().stacksTo(1));

    // ===== CRAFTING COMPONENTS =====
    public static final DeferredItem<Item> ELECTRONIC_CIRCUIT = ITEMS.registerSimpleItem("electronic_circuit",
            new Item.Properties());

    public static final DeferredItem<Item> SPEAKER_COMPONENT = ITEMS.registerSimpleItem("speaker_component",
            new Item.Properties());

    public static final DeferredItem<Item> SMOKE_DETECTOR_SENSOR = ITEMS.registerSimpleItem("smoke_detector_sensor",
            new Item.Properties());

    public static final DeferredItem<Item> ALARM_HORN = ITEMS.registerSimpleItem("alarm_horn",
            new Item.Properties());

    public static final DeferredItem<Item> CONTROL_BOARD = ITEMS.registerSimpleItem("control_board",
            new Item.Properties());

    public static final DeferredItem<Item> PLASTIC_CASING = ITEMS.registerSimpleItem("plastic_casing",
            new Item.Properties());

    public static final DeferredItem<Item> METAL_HOUSING = ITEMS.registerSimpleItem("metal_housing",
            new Item.Properties());

    // ===== ORIGINAL EXAMPLE ITEMS (keeping for reference) =====
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // ===== CREATIVE TABS =====
    // Emergency Systems Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EMERGENCY_SYSTEMS_TAB = CREATIVE_MODE_TABS.register("emergency_systems_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.emergency_systems.emergency_systems"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> FIRE_ALARM_CONTROL_PANEL_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Fire Alarm Systems
                output.accept(RESIDENTIAL_FIRE_ALARM_ITEM.get());
                output.accept(COMMERCIAL_FIRE_ALARM_CEILING_ITEM.get());
                output.accept(COMMERCIAL_FIRE_ALARM_WALL_ITEM.get());
                output.accept(FIRE_ALARM_PULL_STATION_ITEM.get());
                output.accept(FIRE_ALARM_CONTROL_PANEL_ITEM.get());
                output.accept(RESIDENTIAL_FIRE_PANEL_ITEM.get());

                // Tornado Sirens
                output.accept(TORNADO_SIREN_ITEM.get());
                output.accept(TORNADO_SIREN_CONTROL_PANEL_ITEM.get());

                // Tools
                output.accept(DEVICE_LINKING_TOOL.get());
                output.accept(GOVERNMENT_KEY.get());

                // Components (crafting materials)
                output.accept(ELECTRONIC_CIRCUIT.get());
                output.accept(SPEAKER_COMPONENT.get());
                output.accept(SMOKE_DETECTOR_SENSOR.get());
                output.accept(ALARM_HORN.get());
                output.accept(CONTROL_BOARD.get());
                output.accept(PLASTIC_CASING.get());
                output.accept(METAL_HOUSING.get());
            }).build());

    // Original Example Tab (keeping for reference)
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.emergency_systems.example"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    public Emergency_systems(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("Emergency Systems Mod - Loading emergency systems...");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        // Emergency Systems specific setup
        if (Config.enableFireDetection) {
            LOGGER.info("Fire detection enabled with range: {}", Config.alarmRange);
        }
        if (Config.requireGovernmentKey) {
            LOGGER.info("Government key required for tornado sirens");
        }
        LOGGER.info("Tornado siren range: {}", Config.sirenVolumeRange);
    }

    // Add items to existing creative tabs
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add some key items to the building blocks tab
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
            event.accept(FIRE_ALARM_CONTROL_PANEL_ITEM);
            event.accept(TORNADO_SIREN_ITEM);
        }

        // Add tools to the tools tab
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(DEVICE_LINKING_TOOL);
            event.accept(GOVERNMENT_KEY);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        LOGGER.info("Emergency Systems Mod - Server starting with emergency systems active");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("Emergency Systems Mod - Client setup complete");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
