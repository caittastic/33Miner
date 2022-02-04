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
        superBreaker(context);
        return super.onItemUseFirst(stack, context);
    }
    /*  helpful :)  */
    //breaks blocks in a 3x3 tunnel based on facing direction
    private void superBreaker (UseOnContext context){
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos newBlockPos = offsetBlockPos(clickedFace, blockPos, true);
        int depth = 9;


        for (int count = 0; count < depth; count++) {
            newBlockPos = offsetBlockPos(clickedFace, newBlockPos,false);
            BlockPos targetPos = newBlockPos;
            for (int w = -1; w <= 1; w++) { // across the row
                for (int h = -1; h <= 1; h++) { // across the column
                    BlockState newClickedBlock = world.getBlockState(targetPos);
                    if (blockValidToBreak(newClickedBlock, context, world)) {
                        world.destroyBlock(targetPos, true);
                    }
                    targetPos = offsetForArea(clickedFace, newBlockPos, w, h);
                }
            }

        }
    }


    private BlockPos offsetForArea( Direction dir, BlockPos currentPosition, int row, int column ) {
        // no default case - this covers all possible values
        switch (dir) {
            case NORTH:
            case SOUTH:
                return currentPosition.offset(row, column, 0);
            case EAST:
            case WEST:
                return currentPosition.offset(0, row, column);
            case UP:
            case DOWN:
                return currentPosition.offset(row, 0, column);
        }
        return currentPosition;
    }

    private BlockPos offsetBlockPos(Direction direction, BlockPos blockPos, Boolean towardsPlayer){
        //ofsets the blockpos in the direction of the clicked face if towardsPlayer is true
        if(towardsPlayer == true){
            return switch (direction) {
                case NORTH -> blockPos.north();
                case SOUTH -> blockPos.south();
                case EAST -> blockPos.east();
                case WEST -> blockPos.west();
                case UP -> blockPos.above();
                case DOWN -> blockPos.below();
            };
        }else{
            return switch (direction) {
                case NORTH -> blockPos.south();
                case SOUTH -> blockPos.north();
                case EAST -> blockPos.west();
                case WEST -> blockPos.east();
                case UP -> blockPos.below();
                case DOWN -> blockPos.above();
            };
        }

    }

    //breaks blocks in a 1x1x9 tunnel in one direction
    //is not supposed to be used, just example code
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

            if (blockValidToBreak(newClickedBlock,context,world)) {
                world.destroyBlock(newBlockPos, true);
            }
        }
    }
    //checks if a block should be breakable based on vanilla rules
    private boolean blockValidToBreak(BlockState clickedBlock, UseOnContext context, Level world) {
        BlockPos blockpos = context.getClickedPos();
        return clickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f;
    }

}



