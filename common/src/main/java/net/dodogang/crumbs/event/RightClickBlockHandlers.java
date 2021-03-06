package net.dodogang.crumbs.event;

import com.google.common.collect.ImmutableMap;
import net.dodogang.crumbs.CrumbsCore;
import net.dodogang.crumbs.block.CrumbsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RightClickBlockHandlers {
    private final ImmutableMap<Block, Block> logToStrippedMap;

    public RightClickBlockHandlers() {
        logToStrippedMap = new ImmutableMap.Builder<Block, Block>()
                .put(CrumbsBlocks.OAK_BUNDLED_LOG, CrumbsBlocks.STRIPPED_OAK_BUNDLED_LOG)
                .put(CrumbsBlocks.BIRCH_BUNDLED_LOG, CrumbsBlocks.STRIPPED_BIRCH_BUNDLED_LOG)
                .put(CrumbsBlocks.SPRUCE_BUNDLED_LOG, CrumbsBlocks.STRIPPED_SPRUCE_BUNDLED_LOG)
                .put(CrumbsBlocks.JUNGLE_BUNDLED_LOG, CrumbsBlocks.STRIPPED_JUNGLE_BUNDLED_LOG)
                .put(CrumbsBlocks.ACACIA_BUNDLED_LOG, CrumbsBlocks.STRIPPED_ACACIA_BUNDLED_LOG)
                .put(CrumbsBlocks.DARK_OAK_BUNDLED_LOG, CrumbsBlocks.STRIPPED_DARK_OAK_BUNDLED_LOG)
                .put(CrumbsBlocks.CRIMSON_BUNDLED_STEM, CrumbsBlocks.STRIPPED_CRIMSON_BUNDLED_STEM)
                .put(CrumbsBlocks.WARPED_BUNDLED_STEM, CrumbsBlocks.STRIPPED_WARPED_BUNDLED_STEM)
                .build();

        CrumbsCore.platform.registerOnRightClickBlockHandler(this::stripLog);
    }

    public ActionResult stripLog(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction dir) {
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(hand);

        if (CrumbsCore.platform.isAxe(stack) && logToStrippedMap.containsKey(state.getBlock())) {
            world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (!world.isClient) {
                Block strippedLog = logToStrippedMap.get(state.getBlock());

                world.setBlockState(pos, strippedLog.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)), 11);
                if (!player.isCreative()) {
                    stack.damage(1, player, (playerX) -> {
                        playerX.sendToolBreakStatus(hand);
                    });
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}