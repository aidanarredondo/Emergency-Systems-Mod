package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import dev.aidanarredondo.emergency_systems.menu.FireAlarmControlPanelMenu;
import dev.aidanarredondo.emergency_systems.menu.TornadoSirenControlPanelMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, EmergencySystemsMod.MODID);

    public static final Supplier<MenuType<FireAlarmControlPanelMenu>> FIRE_ALARM_CONTROL_PANEL =
            MENUS.register("fire_alarm_control_panel", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> {
                        var pos = data.readBlockPos();
                        var blockEntity = inv.player.level().getBlockEntity(pos);
                        return new FireAlarmControlPanelMenu(windowId, inv, (dev.aidanarredondo.emergency_systems.blockentities.FireAlarmControlPanelBlockEntity) blockEntity);
                    }));

    public static final Supplier<MenuType<TornadoSirenControlPanelMenu>> TORNADO_SIREN_CONTROL_PANEL =
            MENUS.register("tornado_siren_control_panel", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> {
                        var pos = data.readBlockPos();
                        var blockEntity = inv.player.level().getBlockEntity(pos);
                        return new TornadoSirenControlPanelMenu(windowId, inv, (dev.aidanarredondo.emergency_systems.blockentities.TornadoSirenControlPanelBlockEntity) blockEntity);
                    }));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
