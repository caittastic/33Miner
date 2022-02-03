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

public class DestructionCatalyst extends Item {

    public DestructionCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        context.getPlayer().getDirection();
        if (!world.isClientSide){
            simpleBreaker(context);
        }
        return super.onItemUseFirst(stack, context);
    }
    /*  helpful :)  */
    //checks if a block should be breakable based on vanilla rules
    private void superBreaker(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        int[] iterateWidthHeight = {-1, 0, 1};
        int[] itterateDepth = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] negativeItterateDepth = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9};
        Direction clickedFace = context.getClickedFace();
        BlockPos newBlockPos;

        for (int width : iterateWidthHeight) {
            for (int height : iterateWidthHeight) {
                if (clickedFace == Direction.NORTH || clickedFace == Direction.SOUTH) {
                    if(clickedFace == Direction.NORTH){
                        for(int depth : itterateDepth){
                            //width, height, depth
                            newBlockPos = blockPos.offset(width, height, depth);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);

                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);
                            }
                        }
                    }

                    else{
                        for(int depth : negativeItterateDepth){
                            //width, height, depth
                            newBlockPos = blockPos.offset(width, height, depth);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);

                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);
                            }
                        }
                    }

                } 
                else if (clickedFace == Direction.EAST || clickedFace == Direction.WEST) {

                    if(clickedFace == Direction.WEST){
                        for(int depth : itterateDepth){
                            //depth, width, height
                            newBlockPos = blockPos.offset(depth, width, height);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);
                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);
                            }
                        }
                    }

                    else{
                        for(int depth : negativeItterateDepth){
                            //depth, width, height
                            newBlockPos = blockPos.offset(depth, width, height);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);
                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);

                            }
                        }
                    }

                } 
                else {
                    if(clickedFace == Direction.DOWN){
                        for(int depth : itterateDepth){
                            //width, depth, height
                            newBlockPos = blockPos.offset(width, depth, height);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);
                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);
                            }
                        }
                    }
                    else{
                        for(int depth : negativeItterateDepth){
                            //width, depth, height
                            newBlockPos = blockPos.offset(width, depth, height);
                            BlockState newClickedBlock = world.getBlockState(newBlockPos);
                            if (blockValidToBreak(newClickedBlock, context, world)) {
                                world.destroyBlock(newBlockPos, true);
                            }
                        }
                    }
                }

                
            }
        }
    }
    private void simpleBreaker(UseOnContext context) {
        Level world = context.getLevel();
        context.getPlayer().getDirection();
        BlockPos blockPos = context.getClickedPos();
        int[] itterateDepth = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BlockPos newBlockPos;

        for(int depth : itterateDepth){
            //width, height, depth
            newBlockPos = blockPos.offset(0, 0, depth);
            BlockState newClickedBlock = world.getBlockState(newBlockPos);

            if (newClickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f) {
                world.destroyBlock(newBlockPos, true);
            }
        }
    }
    private boolean blockValidToBreak(BlockState clickedBlock, UseOnContext context, Level world) {
        BlockPos blockpos = context.getClickedPos();
        return clickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f;
    }

}



