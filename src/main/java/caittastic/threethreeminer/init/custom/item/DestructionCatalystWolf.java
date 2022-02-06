package caittastic.threethreeminer.init.custom.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DestructionCatalystWolf extends Item {

    public DestructionCatalystWolf(Properties properties) {
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

    private BlockPos offsetForArea( Direction dir, BlockPos currentPosition, int row, int col ) {
        // no default case - this covers all possible values
        return switch (dir) {
            case NORTH -> currentPosition.offset(row, col, 0);
            case SOUTH -> currentPosition.offset(row, col, 0);
            case EAST -> currentPosition.offset(0, row, col);
            case WEST -> currentPosition.offset(0, row, col);
            case UP -> currentPosition.offset(row, 0, col);
            case DOWN -> currentPosition.offset(row, 0, col);
        };
    }

    private BlockPos getStartPos(Direction clicked, BlockPos clickedPos) {
        return offsetForArea(clicked, clickedPos, -1, -1);
    }

    private BlockPos getEndPos(Direction clicked, BlockPos clickedPos) {
        return offsetForArea(clicked, clickedPos.relative(clicked, 9), 1, 1);
    }

    private void superBreaker(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        AABB zone = new AABB(getStartPos(clickedFace, blockPos), getEndPos(clickedFace, blockPos));
        List<ItemStack> drops = new LinkedList<>();
        BlockPos.betweenClosedStream(zone).filter( bp -> blockValidToBreak(world.getBlockState(bp), context, world))
                .filter( bp -> playerCanBreakBlock((ServerPlayer)context.getPlayer(), bp) )
                .forEach(bp -> {
                    BlockState bs = world.getBlockState(bp);
                    drops.addAll(Block.getDrops(bs, (ServerLevel) world, bp, world.getBlockEntity(bp), context.getPlayer(), context.getItemInHand() ));
                    world.destroyBlock(bp, false);
                });

        for ( int i = 0; i < drops.size(); i++ ) {
            ItemStack w = drops.get(i);
            if (!w.isEmpty()) {
                for (int k = i; k < drops.size(); ++k) {
                    ItemStack t = drops.get(k);
                    if (ItemHandlerHelper.canItemStacksStack(w, t)) {
                        w.grow(t.getCount());
                        drops.set(k, ItemStack.EMPTY);
                    }
                }
            }
        }

        drops.removeIf(ItemStack::isEmpty);

        int posX = blockPos.getX();
        int posY = blockPos.getY();
        int posZ = blockPos.getZ();

        drops.forEach( stack -> world.addFreshEntity(new ItemEntity(world, posX, posY, posZ, stack)));
    }

    public static boolean playerCanEdit(Player player, BlockPos pos) {
        if (ServerLifecycleHooks.getCurrentServer().isUnderSpawnProtection((ServerLevel) player.getCommandSenderWorld(), pos, player)) {
            return false;
        }
        return Arrays.stream(Direction.values()).allMatch(e -> player.mayUseItemAt(pos, e, ItemStack.EMPTY));
    }

    public static boolean playerCanBreakBlock(ServerPlayer player, BlockPos pos) {
        return playerCanEdit(player, pos) &&

                ForgeHooks.onBlockBreakEvent(player.getCommandSenderWorld(), player.gameMode.getGameModeForPlayer(), player, pos) != -1;
    }

    private boolean blockValidToBreak(BlockState clickedBlock, UseOnContext context, Level world) {
        return clickedBlock.getDestroySpeed(world,context.getClickedPos()) > 0f;
    }

}
