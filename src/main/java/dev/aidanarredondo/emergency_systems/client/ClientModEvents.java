package dev.aidanarredondo.emergency_systems.client;

import dev.aidanarredondo.emergency_systems.EmergencySystemsMod;
import dev.aidanarredondo.emergency_systems.client.gui.FireAlarmControlPanelScreen;
import dev.aidanarredondo.emergency_systems.client.gui.TornadoSirenControlPanelScreen;
import dev.aidanarredondo.emergency_systems.init.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = EmergencySystemsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Client setup code here
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.FIRE_ALARM_CONTROL_PANEL.get(), FireAlarmControlPanelScreen::new);
        event.register(ModMenuTypes.TORNADO_SIREN_CONTROL_PANEL.get(), TornadoSirenControlPanelScreen::new);
    }
}
