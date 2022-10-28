package io.thimblebird.struckobsidian.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static io.thimblebird.struckobsidian.StruckObsidian.CONFIG;

public class BranchGenerator {
    protected static final Block convertBlock = Blocks.CRYING_OBSIDIAN;
    protected static final Direction defaultDirection = Direction.DOWN;
    protected Block findBlock;

    public BranchGenerator(@NotNull World world, @NotNull BlockPos pos) {
        findBlock = !CONFIG.debugSourceGlassBlock() ? Blocks.OBSIDIAN : Blocks.GLASS;
        BlockPos sourceBlockPos = pos.down(1);
        Block sourceBlock = world.getBlockState(sourceBlockPos).getBlock();

        // prevents generating branches repeatedly in the same spot
        if (!sourceBlock.equals(convertBlock)) {
            Random random = Random.create();
            int branches = getBranches(random);
            for (int i = 0; i < branches; i++) {
                spread(world, pos, random);
            }
        } else {
            switch (CONFIG.secondStrikeSourceBlockBehavior()) {
                case DESTROY -> world.breakBlock(sourceBlockPos, false);
                //case DROP -> world.breakBlock(sourceBlockPos, true); // not working
                default -> {}
            }
        }
    }

    private int getBranches(Random random) {
        if (CONFIG.spreadBranches.minSpreadBranches() > CONFIG.spreadBranches.maxSpreadBranches()) {
            CONFIG.spreadBranches.maxSpreadBranches(CONFIG.spreadBranches.minSpreadBranches());
        }

        return random.nextBetween(CONFIG.spreadBranches.minSpreadBranches(), CONFIG.spreadBranches.maxSpreadBranches());
    }

    private int getSpreadAttempts(Random random) {
        if (CONFIG.spreadAttempts.minSpreadAttempts() > CONFIG.spreadAttempts.maxSpreadAttempts()) {
            CONFIG.spreadAttempts.maxSpreadAttempts(CONFIG.spreadAttempts.minSpreadAttempts());
        }

        return random.nextBetween(CONFIG.spreadAttempts.minSpreadAttempts(), CONFIG.spreadAttempts.maxSpreadAttempts());
    }

    private int getSpreadAfterAttempts(Random random) {
        if (CONFIG.spreadAfterAttempts.minSpreadAfterAttempts() > CONFIG.spreadAfterAttempts.maxSpreadAfterAttempts()) {
            CONFIG.spreadAfterAttempts.maxSpreadAfterAttempts(CONFIG.spreadAfterAttempts.minSpreadAfterAttempts());
        }

        return random.nextBetween(CONFIG.spreadAfterAttempts.minSpreadAfterAttempts(), CONFIG.spreadAfterAttempts.maxSpreadAfterAttempts());
    }

    public void spread(@NotNull World world, @NotNull BlockPos pos, Random random) {
        int spreadAttempts = getSpreadAttempts(random);
        BlockStateNeighbor stateNeighbor = new BlockStateNeighbor(world, pos, defaultDirection, 1, spreadAttempts);
        Direction spreadDirection = null;
        Direction lastDirection = null;

        while (stateNeighbor.attemptsCount < spreadAttempts) {
            Direction nextDirection = defaultDirection;

            if (spreadDirection != null && lastDirection == defaultDirection) {
                if (stateNeighbor.attemptsCount >= getSpreadAfterAttempts(random)) {
                    nextDirection = Util.getRandom(stateNeighbor.DIRECTIONS_ALLOWLIST, random);
                }
            }

            spreadDirection = stateNeighbor.setDirection(nextDirection).direction;
            lastDirection = spreadDirection;

            if (world.canSetBlock(stateNeighbor.getPos())) {
                Block nextBlock = stateNeighbor.getNextBlock();

                if (nextBlock == findBlock) {
                    world.setBlockState(stateNeighbor.getPos(), convertBlock.getDefaultState());
                } else if (nextBlock == convertBlock) {
                    stateNeighbor.skipNextBlock();
                } else {
                    break;
                }
            }
        }
    }
}
