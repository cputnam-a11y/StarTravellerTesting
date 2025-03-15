package startravellertesting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import startravellertesting.annotation.RecordComponent;
import startravellertesting.duck.FireworkExplosionComponentDuck;
import startravellertesting.util.ComposedFireworkComponentCodec;
import startravellertesting.util.ComposedFireworkComponentPacketCodec;
import startravellertesting.util.ExtraParams;

@Mixin(FireworkExplosionComponent.class)
@Debug(export = true)
public class FireworkExplosionComponentMixin implements FireworkExplosionComponentDuck {
    @SuppressWarnings("MissingUnique") // Unique not used to easier fulfill record requirements
    @RecordComponent
    private ExtraParams starTravellerTesting$params;

    // this name needs to match the name of the record component, and consequently, the field defined above
    @Unique // Unique can't mangle public method's names
    public ExtraParams starTravellerTesting$params() {
        return starTravellerTesting$params;
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
                    remap = false // dfu class
            )
    )
    private static Codec<FireworkExplosionComponent> wrapCodec(Codec<FireworkExplosionComponent> original) {
        return new ComposedFireworkComponentCodec(original); // main work done in codec class
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function5;)Lnet/minecraft/network/codec/PacketCodec;"
            )
    )
    private static PacketCodec<RegistryByteBuf, FireworkExplosionComponent> wrapPacketCodec(PacketCodec<RegistryByteBuf, FireworkExplosionComponent> original) {
        return new ComposedFireworkComponentPacketCodec(original); // main work done in codec class
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initExtraParams(FireworkExplosionComponent.Type type, IntList intList, IntList intList2, boolean bl, boolean bl2, CallbackInfo ci) {
        // this thread local is set by all codecs before calling the constructor through the record FireworkExplosionComponentDuck.doWithContext
        starTravellerTesting$params = FAKE_CONSTRUCTOR_PARAM.get();
    }
}
