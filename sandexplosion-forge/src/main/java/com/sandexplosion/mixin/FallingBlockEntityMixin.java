package com.sandexplosion.mixin;

import com.sandexplosion.SandExplosionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!SandExplosionMod.explosionsEnabled) return;

        FallingBlockEntity self = (FallingBlockEntity) (Object) this;
        Level level = self.level();

        if (level.isClientSide) return;
        if (!isSandBlock(self.getBlockState())) return;

        BlockPos pos = self.blockPosition();
        BlockState below = level.getBlockState(pos.below());
        BlockState current = level.getBlockState(pos);

        if (isDirtOrGrass(below) || isDirtOrGrass(current)) {
            self.discard();
            level.explode(
                    null,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SandExplosionMod.explosionPower,
                    Level.ExplosionInteraction.TNT
            );
        }
    }

    private boolean isSandBlock(BlockState state) {
        return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND);
    }

    private boolean isDirtOrGrass(BlockState state) {
        return state.is(Blocks.DIRT)
            || state.is(Blocks.GRASS_BLOCK)
            || state.is(Blocks.COARSE_DIRT)
            || state.is(Blocks.ROOTED_DIRT)
            || state.is(Blocks.PODZOL)
            || state.is(Blocks.MYCELIUM);
    }
}
