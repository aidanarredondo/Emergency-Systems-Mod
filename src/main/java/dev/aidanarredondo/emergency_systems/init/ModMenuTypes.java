package dev.aidanarredondo.emergency_systems.init;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, EmergencySystemsMod.MODID);

    // Menu types will be added here when we implement GUIs

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
