package dev.aidanarredondo.emergency_systems.items;

import dev.aidanarredondo.emergency_systems.blockentities.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DeviceLinkingToolItem extends Item {

    public DeviceLinkingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.isClientSide() || player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        // Check if this is a linkable device
        if (!(blockEntity instanceof ResidentialFireAlarmBlockEntity) &&
                !(blockEntity instanceof CommercialFireAlarmBlockEntity) &&
                !(blockEntity instanceof FireAlarmControlPanelBlockEntity) &&
                !(blockEntity instanceof TornadoSirenBlockEntity) &&
                !(blockEntity instanceof TornadoSirenControlPanelBlockEntity)) {
            player.displayClientMessage(Component.literal("This device cannot be linked"), false);
            return InteractionResult.FAIL;
        }

        // Get or create custom data
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (player.isShiftKeyDown()) {
            // Clear stored position
            tag.remove("StoredX");
            tag.remove("StoredY");
            tag.remove("StoredZ");
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            player.displayClientMessage(Component.literal("Linking tool cleared"), false);
            return InteractionResult.SUCCESS;
        }

        if (!tag.contains("StoredX")) {
            // Store first device position as separate coordinates
            tag.putInt("StoredX", pos.getX());
            tag.putInt("StoredY", pos.getY());
            tag.putInt("StoredZ", pos.getZ());
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            player.displayClientMessage(Component.literal("First device selected. Right-click on second device to link."), false);
            return InteractionResult.SUCCESS;
        } else {
            // Link devices
            int x = tag.getInt("StoredX").orElse(0);
            int y = tag.getInt("StoredY").orElse(0);
            int z = tag.getInt("StoredZ").orElse(0);
            BlockPos storedPos = new BlockPos(x, y, z);
            BlockEntity storedEntity = level.getBlockEntity(storedPos);

            if (storedEntity == null) {
                player.displayClientMessage(Component.literal("First device no longer exists"), false);
                tag.remove("StoredX");
                tag.remove("StoredY");
                tag.remove("StoredZ");
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                return InteractionResult.FAIL;
            }

            boolean success = linkDevices(storedEntity, blockEntity, player);

            if (success) {
                player.displayClientMessage(Component.literal("Devices linked successfully"), false);
            }

            tag.remove("StoredX");
            tag.remove("StoredY");
            tag.remove("StoredZ");
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    private boolean linkDevices(BlockEntity first, BlockEntity second, Player player) {
        // Residential fire alarms can link to other residential fire alarms
        if (first instanceof ResidentialFireAlarmBlockEntity firstAlarm &&
                second instanceof ResidentialFireAlarmBlockEntity secondAlarm) {
            firstAlarm.linkAlarm(second.getBlockPos());
            secondAlarm.linkAlarm(first.getBlockPos());
            return true;
        }

        // Commercial fire alarms link to control panels
        if (first instanceof CommercialFireAlarmBlockEntity alarm &&
                second instanceof FireAlarmControlPanelBlockEntity panel) {
            alarm.setControlPanel(panel.getBlockPos());
            panel.linkDevice(alarm.getBlockPos());
            return true;
        }

        if (first instanceof FireAlarmControlPanelBlockEntity panel &&
                second instanceof CommercialFireAlarmBlockEntity alarm) {
            alarm.setControlPanel(panel.getBlockPos());
            panel.linkDevice(alarm.getBlockPos());
            return true;
        }

        // Tornado sirens link to control panels
        if (first instanceof TornadoSirenBlockEntity siren &&
                second instanceof TornadoSirenControlPanelBlockEntity panel) {
            panel.linkSiren(siren.getBlockPos());
            return true;
        }

        if (first instanceof TornadoSirenControlPanelBlockEntity panel &&
                second instanceof TornadoSirenBlockEntity siren) {
            panel.linkSiren(siren.getBlockPos());
            return true;
        }

        player.displayClientMessage(Component.literal("These devices cannot be linked together"), false);
        return false;
    }
}
