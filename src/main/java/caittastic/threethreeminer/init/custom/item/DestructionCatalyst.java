package caittastic.threethreeminer.init.custom.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.server.ServerLifecycleHooks;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DestructionCatalyst extends Item {

    public DestructionCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        context.getPlayer().getDirection();
        if(!world.isClientSide){
            superBreaker(context);
        }
        return super.onItemUseFirst(stack, context);
    }

    //  helpful :)
    //checks if a block should be breakable based on vanilla rules

    private void superBreaker(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos newBlockPos;
        int[] iterateWidthHeight = {-1, 0, 1};
        int depth = 9;
        Direction clickedFace = context.getClickedFace();

        for (int width : iterateWidthHeight) {
            for (int height : iterateWidthHeight) {
                for (int d = 0; d <= (depth - 1); d++) {
                    newBlockPos = offsetBlock(blockPos, clickedFace, width, height, d);
                    BlockState newClickedBlock = world.getBlockState(newBlockPos);
                    breakAndGivePlayerItems(newBlockPos, context);

                }
            }
        }
    }

    private BlockPos offsetBlock(BlockPos blockToOffset, Direction clickedDirection, int width, int height, int depth) {
        int negativeDepth = depth * -1;
        BlockPos newBlockPos = blockToOffset;
        switch (clickedDirection) {
            case SOUTH:
                newBlockPos = blockToOffset.offset(width, height, negativeDepth);break;
            case NORTH:
                newBlockPos = blockToOffset.offset(width, height, depth);break;
            case EAST:
                newBlockPos = blockToOffset.offset(negativeDepth, width, height);break;
            case WEST:
                newBlockPos = blockToOffset.offset(depth, width, height);break;
            case UP:
                newBlockPos = blockToOffset.offset(width, negativeDepth, height);break;
            case DOWN:
                newBlockPos = blockToOffset.offset(width, depth, height);break;
        }
        return newBlockPos;
    }

    private boolean blockValidToBreak(BlockPos clickedBlockPos, UseOnContext context, Level world) {
        ServerPlayer player = (ServerPlayer)context.getPlayer();
        BlockState clickedBlock = world.getBlockState(clickedBlockPos);

        return clickedBlock.getDestroySpeed(world, context.getClickedPos()) > 0f &&
                isBlockSpawnProtected(context.getPlayer(),context.getClickedPos())&&
                ForgeHooks.onBlockBreakEvent(player.getCommandSenderWorld(), player.gameMode.getGameModeForPlayer(), player, clickedBlockPos) != -1;
    }

    //i took this code basically exactly from dshadowwolfs bit
    private boolean isBlockSpawnProtected(Player player, BlockPos pos) {
        if (ServerLifecycleHooks.getCurrentServer().isUnderSpawnProtection((ServerLevel) player.getCommandSenderWorld(), pos, player)) {
            return false;
        }
        return Arrays.stream(Direction.values()).allMatch(e -> player.mayUseItemAt(pos, e, ItemStack.EMPTY));
    }

    private void breakAndGivePlayerItems( BlockPos bopo, UseOnContext context){
        List<ItemStack> drops = new LinkedList<>();
        Level world = context.getLevel();
        BlockState newClickedBlock = world.getBlockState(bopo);
        Player player = context.getPlayer();
        if(blockValidToBreak(bopo, context, world)){
            drops.addAll(Block.getDrops(newClickedBlock, (ServerLevel) world, bopo, world.getBlockEntity(bopo), context.getPlayer(), context.getItemInHand() ));
            world.destroyBlock(bopo, false);
        }
        for(int i = 0; i < drops.size(); i++){
            ItemStack itemToStackOn = drops.get(i);
            if(!itemToStackOn.isEmpty()){
                for(int k = i; k <drops.size(); k++){
                    ItemStack itemToCheckWith = drops.get(k);
                    if(ItemHandlerHelper.canItemStacksStack(itemToStackOn, itemToCheckWith)){
                        itemToStackOn.grow(itemToCheckWith.getCount());
                        drops.set(k,ItemStack.EMPTY);
                    }
                }
            }
        }





        for (ItemStack item :drops) {
            if (!player.getInventory().add(item)) {
                player.drop(item, false);
            }
        }
    }
}



