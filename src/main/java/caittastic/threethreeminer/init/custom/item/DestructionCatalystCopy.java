package caittastic.threethreeminer.init.custom.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class DestructionCatalystCopy extends Item {

    public DestructionCatalystCopy(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        context.getPlayer().getDirection();
        if (!world.isClientSide){
            superBreaker(context);
        }
        return super.onItemUseFirst(stack, context);
    }

    //  helpful :)
    //checks if a block should be breakable based on vanilla rules

    private void superBreaker(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos newBlockPos = blockPos;
        int[] iterateWidthHeight = {-1, 0, 1};
        int[] itterateDepth = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] negativeItterateDepth = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9};
        Direction clickedFace = context.getClickedFace();

        for (int width : iterateWidthHeight) {
            for (int height : iterateWidthHeight) {
                for (int depth : itterateDepth) {
                    newBlockPos = offsetBlock(world,context, newBlockPos, clickedFace,width,height, depth);
                }
            }
        }
    }

    private BlockPos offsetBlock(Level world,UseOnContext context, BlockPos blockToOffset, Direction clickedDirection, int width, int height, int depth){
        int negativeDepth;
        BlockPos newBlockPos = blockToOffset;
        BlockState newClickedBlock = world.getBlockState(blockToOffset);
        negativeDepth = depth * -1;

        if(clickedDirection == Direction.SOUTH){
            //width, height, depth
            newBlockPos = blockToOffset.offset(width, height, negativeDepth);
        }
        else if(clickedDirection == Direction.NORTH){
            //width, height, depth
            newBlockPos = blockToOffset.offset(width, height, depth);
        }
        else if(clickedDirection == Direction.EAST){
            //depth, width, height
            newBlockPos = blockToOffset.offset(negativeDepth, width, height);
        }
        else if(clickedDirection == Direction.WEST){
            //depth, width, height
            newBlockPos = blockToOffset.offset(depth, width, height);
        }
        else if(clickedDirection == Direction.UP){
            //width, depth, height
            newBlockPos = blockToOffset.offset(width, negativeDepth, height);
        }
        else if(clickedDirection == Direction.DOWN){
            //width, depth, height
            newBlockPos = blockToOffset.offset(width, depth, height);
        }

        newClickedBlock = world.getBlockState(newBlockPos);
        if(blockValidToBreak(newClickedBlock, context, world)){
            world.destroyBlock(newBlockPos, true);
        }

        return blockToOffset;
    }

    private boolean blockValidToBreak(BlockState clickedBlock, UseOnContext context, Level world) {
        BlockPos blockpos = context.getClickedPos();
        return clickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f;
    }

}



