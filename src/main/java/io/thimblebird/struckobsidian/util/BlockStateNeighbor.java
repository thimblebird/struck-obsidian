package io.thimblebird.struckobsidian.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.thimblebird.struckobsidian.StruckObsidian.CONFIG;
import static io.thimblebird.struckobsidian.StruckObsidian.LOGGER;

@SuppressWarnings("unused")
public class BlockStateNeighbor {
    public List<Direction> DIRECTIONS_ALLOWLIST;

    private World world;
    public BlockPos pos;
    public BlockState sourceState;

    public Direction defaultDirection = Direction.NORTH;
    public Direction direction = defaultDirection;

    public int offset;

    public int attemptsInitial;
    public int attemptsRemaining;
    public int attemptsCount = 0;

    private void init(@NotNull World world, @NotNull BlockPos pos, Direction direction, int offset, int attempts) {
        this.sourceState = world.getBlockState(pos);
        this.world = world;

        DIRECTIONS_ALLOWLIST = new ArrayList<>();
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionDown())    DIRECTIONS_ALLOWLIST.add(Direction.DOWN);
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionUp())      DIRECTIONS_ALLOWLIST.add(Direction.UP);
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionNorth())   DIRECTIONS_ALLOWLIST.add(Direction.NORTH);
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionSouth())   DIRECTIONS_ALLOWLIST.add(Direction.SOUTH);
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionWest())    DIRECTIONS_ALLOWLIST.add(Direction.WEST);
        if (CONFIG.spreadDirectionsAllowlist.spreadDirectionEast())    DIRECTIONS_ALLOWLIST.add(Direction.EAST);

        this
                .setPos(pos)
                .setDirection(direction)
                .setAttempts(attempts)
                .setOffset(offset);
    }

    public BlockStateNeighbor(@NotNull World world, @NotNull BlockPos pos) {
        init(world, pos, this.defaultDirection, this.offset, this.attemptsInitial);
    }

    @SuppressWarnings("unused")
    public BlockStateNeighbor(@NotNull World world, @NotNull BlockPos pos, Direction direction, int offset) {
        init(world, pos, direction, offset, this.attemptsInitial);
    }

    @SuppressWarnings("unused")
    public BlockStateNeighbor(@NotNull World world, @NotNull BlockPos pos, Direction direction, int offset, int attempts) {
        init(world, pos, direction, offset, attempts);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    @SuppressWarnings("all")
    public BlockStateNeighbor setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    public BlockPos getNextPos() {
        return this.pos.offset(direction, offset);
    }

    @SuppressWarnings("all")
    public BlockStateNeighbor setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    @SuppressWarnings("all")
    public BlockStateNeighbor setAttempts(int i) {
        this.attemptsInitial = i;
        this.attemptsRemaining = i;
        return this;
    }

    @SuppressWarnings("all")
    public BlockStateNeighbor setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    private void updateAttemptsCount() {
        this.attemptsCount++;
    }

    public BlockState getBlockState(Direction direction, int offset) {
        return world.getBlockState(pos.offset(direction, offset));
    }

    public Block getBlock(BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    public Block getBlock() {
        return world.getBlockState(pos).getBlock();
    }

    @Nullable
    public Block getNextBlock() {
        if (attemptsRemaining > 0) {
            --attemptsRemaining;
            updateAttemptsCount();

            BlockPos nextPos = pos.offset(direction, offset);
            setPos(nextPos);

            if (CONFIG.debugLogAttempts()) {
                LOGGER.info("ATTEMPT " + attemptsCount + ": [" + getPos().toShortString() + "]");
            }

            return getBlock(nextPos);
        }

        return null;
    }

    public void skipNextBlock() {
        if (attemptsRemaining > 0) {
            --attemptsRemaining;
            updateAttemptsCount();

            if (CONFIG.debugLogAttempts()) {
                LOGGER.info("ATTEMPT " + attemptsCount + " SKIPPED: [" + getPos().toShortString() + "]");
            }
        }
    }

    public Block getPrevBlock() {
        attemptsRemaining++;
        updateAttemptsCount();

        BlockPos prevPos = pos.offset(direction, -offset);
        setPos(prevPos);

        return getBlock(prevPos);
    }

    public void setDirectionsAllowlist(List<Direction> allowlist) {
        DIRECTIONS_ALLOWLIST = allowlist;
    }

    public void setDirectionsHorizontal() {
        DIRECTIONS_ALLOWLIST = List.of(
                Direction.NORTH,
                Direction.SOUTH,
                Direction.WEST,
                Direction.EAST
        );
    }

    public void setDirectionsVertical() {
        DIRECTIONS_ALLOWLIST = List.of(
                Direction.UP,
                Direction.DOWN
        );
    }
}
