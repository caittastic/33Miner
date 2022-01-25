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


    private void superBreaker(UseOnContext context){
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        int[] iterateLoop  = {-1,0,1};










        //facing north/south
        if( context.getClickedFace() == Direction.NORTH || context.getClickedFace() == Direction.SOUTH) {
            for (int _i : iterateLoop) {
                for (int _j : iterateLoop) {

                    //changing variables to match the new itterated block
                    BlockPos newBlockPos = blockPos.offset(_j, _i, 0);
                    BlockState newClickedBlock = world.getBlockState(newBlockPos);


                    if (blockValidToBreak(newClickedBlock, context, world)) {
                        world.destroyBlock(newBlockPos, true);
                    }
                }

            }
        }

        //facing east/west
        if( context.getClickedFace() == Direction.EAST || context.getClickedFace() == Direction.WEST) {
            for (int _i : iterateLoop) {
                for (int _j : iterateLoop) {

                    //changing variables to match the new itterated block
                    BlockPos newBlockPos = blockPos.offset(0, _i, _j);
                    BlockState newClickedBlock = world.getBlockState(newBlockPos);


                    if (blockValidToBreak(newClickedBlock, context, world)) {
                        world.destroyBlock(newBlockPos, true);
                    }
                }

            }
        }

        //facing up/down
        if( context.getClickedFace() == Direction.UP || context.getClickedFace() == Direction.DOWN) {
            for (int _i : iterateLoop) {
                for (int _j : iterateLoop) {

                    //changing variables to match the new itterated block
                    BlockPos newBlockPos = blockPos.offset(_i, 0, _j);
                    BlockState newClickedBlock = world.getBlockState(newBlockPos);


                    if (blockValidToBreak(newClickedBlock, context, world)) {
                        world.destroyBlock(newBlockPos, true);
                    }
                }
            }
        }
    }

}



