package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EmergencySystemsMod.MODID);

    public static final Supplier<CreativeModeTab> EMERGENCY_SYSTEMS_TAB = CREATIVE_MODE_TABS.register("emergency_systems",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.emergency_systems.emergency_systems"))
                    .icon(() -> new ItemStack(ModItems.FIRE_ALARM_CONTROL_PANEL.get()))
                    .displayItems((parameters, output) -> {
                        // Fire Alarm Systems
                        output.accept(ModItems.RESIDENTIAL_FIRE_ALARM.get());
                        output.accept(ModItems.COMMERCIAL_FIRE_ALARM_CEILING.get());
                        output.accept(ModItems.COMMERCIAL_FIRE_ALARM_WALL.get());
                        output.accept(ModItems.FIRE_ALARM_PULL_STATION.get());
                        output.accept(ModItems.FIRE_ALARM_CONTROL_PANEL.get());
                        output.accept(ModItems.RESIDENTIAL_FIRE_PANEL.get());

                        // Tornado Sirens
                        output.accept(ModItems.TORNADO_SIREN.get());
                        output.accept(ModItems.TORNADO_SIREN_CONTROL_PANEL.get());

                        // Tools
                        output.accept(ModItems.DEVICE_LINKING_TOOL.get());
                        output.accept(ModItems.GOVERNMENT_KEY.get());

                        // Components (crafting materials)
                        output.accept(ModItems.ELECTRONIC_CIRCUIT.get());
                        output.accept(ModItems.SPEAKER_COMPONENT.get());
                        output.accept(ModItems.SMOKE_DETECTOR_SENSOR.get());
                        output.accept(ModItems.ALARM_HORN.get());
                        output.accept(ModItems.CONTROL_BOARD.get());
                        output.accept(ModItems.PLASTIC_CASING.get());
                        output.accept(ModItems.METAL_HOUSING.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
