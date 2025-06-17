package dev.aidanarredondo.emergency_systems.blocks;

import dev.aidanarredondo.emergency_systems.blockentities.TornadoSirenControlPanelBlockEntity;
import dev.aidanarredondo.emergency_systems.init.ModBlockEntities;
import dev.aidanarredondo.emergency_systems.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TornadoSirenControlPanelBlock extends BaseEntityBlock {
    
    public TornadoSirenControlPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TornadoSirenControlPanelBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            ItemStack heldItem = player.getMainHandItem();
            
            // Check if player has government key
            if (!heldItem.is(ModItems.GOVERNMENT_KEY.get())) {
                player.sendSystemMessage(Component.literal("Access Denied: Government Key Required"));
                return InteractionResult.FAIL;
            }
            
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof TornadoSirenControlPanelBlockEntity panelEntity) {
                if (player instanceof ServerPlayer serverPlayer) {
                    panelEntity.openGUI(serverPlayer);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        
        return createTickerHelper(blockEntityType, ModBlockEntities.TORNADO_SIREN_CONTROL_PANEL.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }
}
