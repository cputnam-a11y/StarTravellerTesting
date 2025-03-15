package startravellertesting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.component.type.FireworkExplosionComponent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import startravellertesting.duck.FireworkExplosionComponentDuck;

@Mixin(FireworksSparkParticle.class)
@Debug(export = true)
public class FireworkSparksParticleMixin {
    @Debug(export = true)
    @Mixin(FireworksSparkParticle.FireworkParticle.class)
    public static class FireworkParticleMixin {
        @ModifyExpressionValue(
                method = "tick",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/component/type/FireworkExplosionComponent$Type;ordinal()I"
                )
        )
        private int skipNormalExplosionIfFaked(int original, @Local FireworkExplosionComponent component, @Share("isCustomType") LocalBooleanRef isCustomType) {
            if (FireworkExplosionComponentDuck.starTravellerTesting$params(component) != null) {
                isCustomType.set(true);
                return -1;
            }
            return original;
        }

        @Inject(
                method = "tick",
                at = @At(
                        value = "INVOKE",
                        target = "Lit/unimi/dsi/fastutil/ints/IntList;getInt(I)I",
                        remap = false
                )
        )
        private void handleSpawnCustomParticles(CallbackInfo ci, @Share("isCustomType") LocalBooleanRef isCustomType) {
            if (isCustomType.get()) {
                // idk. spawn whatever you need to like vanilla does here
            }
        }
    }
}
