package io.thimblebird.struckobsidian.mixin;

import io.thimblebird.struckobsidian.util.BranchGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {
    @Inject(at = @At("HEAD"), method = "setPowered(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V")
    public void struckobsidian$setPowered(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (!world.isClient()) {
            new BranchGenerator(world, pos);
        }
    }
}
