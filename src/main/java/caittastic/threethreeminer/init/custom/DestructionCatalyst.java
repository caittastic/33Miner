package caittastic.threethreeminer.init.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
    //checks if a block should be breakable based on vanilla rules
    private boolean blockValidToBreak(BlockState clickedBlock, UseOnContext context, Level world) {
        BlockPos blockpos = context.getClickedPos();
        return clickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f;
    }


    private void superBreaker(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        int[] iterateLoop = {-1, 0, 1};
        int depth = 0;
        BlockPos newBlockPos;


        for (int width : iterateLoop) {
            for (int height : iterateLoop) {
                if (context.getClickedFace() == Direction.NORTH || context.getClickedFace() == Direction.SOUTH) {
                    //north-south
                    //x y z
                    //width, height, depth
                    newBlockPos = blockPos.offset(width, height, depth);
                } else if (context.getClickedFace() == Direction.EAST || context.getClickedFace() == Direction.WEST) {
                    //east-west
                    //x y z
                    //depth, width, height
                    newBlockPos = blockPos.offset(depth, width, height);
                } else {
                    //up-down
                    //x y z
                    //width, depth, height
                    newBlockPos = blockPos.offset(width, depth, height);
                }

                BlockState newClickedBlock = world.getBlockState(newBlockPos);


                if (blockValidToBreak(newClickedBlock, context, world)) {
                    world.destroyBlock(newBlockPos, true);
                }
            }
        }
    }
}



