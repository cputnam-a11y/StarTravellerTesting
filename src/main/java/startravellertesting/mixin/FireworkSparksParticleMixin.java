package startravellertesting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.component.type.FireworkExplosionComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import startravellertesting.duck.FireworkExplosionComponentDuck;

@Mixin(FireworksSparkParticle.class)
public class FireworkSparksParticleMixin {
    @Mixin(FireworksSparkParticle.FireworkParticle.class)
    public static class FireworkParticleMixin { // could be a single mixin, but for debugging purposes, it's split into two
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
                return -1; // -1 is not a valid index into the enum constant array located in this class, thus the switch becomes a no-op, allowing us to skip straight to the default case, which in this case is empty. note that this only works for switch statements, not expressions
            }
            return original; // it doesn't have custom type, so just return the original value
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
            //noinspection StatementWithEmptyBody
            if (isCustomType.get()) {
                // idk. spawn whatever you need to like vanilla does here
            }
        }
    }
}
