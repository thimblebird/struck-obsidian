package io.thimblebird.struckobsidian.mixin;

import io.thimblebird.struckobsidian.util.BlockStateNeighbor;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.*;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningEntityMixin {
    private final Block findBlock = Blocks.GLASS;
    private final Block convertBlock = Blocks.CRYING_OBSIDIAN;
    private final Direction defaultDirection = Direction.DOWN;

    @Inject(at = @At("HEAD"), method = "setPowered(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V")
    public void struckobsidian$setPowered(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        int maxSpreadAttempts = 5;

        BlockStateNeighbor stateNeighbor = new BlockStateNeighbor(world, pos, defaultDirection, 1, maxSpreadAttempts);
        Direction spreadDirection = null;
        Direction lastDirection = null;

        for (int i = 0; i < maxSpreadAttempts; i++) {
            Direction nextDirection = defaultDirection;
            Random random = Random.create();

            if (spreadDirection != null && stateNeighbor.attemptsCount > 1) {
                if (lastDirection == defaultDirection) {
                    // checks if the next pos would be directly under an already converted block.
                    // if it is, leave the direction at default. otherwise set a random direction
                    nextDirection = Util.getRandom(stateNeighbor.DIRECTIONS_WHITELIST, random);
                    if (stateNeighbor.getBlock(
                            stateNeighbor.getNextPos()
                                    .offset(nextDirection, 1)
                                    .offset(Direction.UP, 1)
                    ) == convertBlock) {
                        nextDirection = defaultDirection;
                    }
                }
            }

            spreadDirection = stateNeighbor.setDirection(nextDirection).direction;
            lastDirection = spreadDirection;

            if (stateNeighbor.getNextBlock() == findBlock && world.canSetBlock(stateNeighbor.getPos())) {
                world.setBlockState(stateNeighbor.getPos(), convertBlock.getDefaultState());
            } else {
                break;
            }
        }
    }
}
