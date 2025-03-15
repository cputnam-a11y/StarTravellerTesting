package startravellertesting.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.component.type.FireworkExplosionComponent;
import startravellertesting.duck.FireworkExplosionComponentDuck;

import java.util.Optional;

public record ComposedFireworkComponentCodec(Codec<FireworkExplosionComponent> original) implements Codec<FireworkExplosionComponent> {
    public static final Codec<Optional<ExtraParams>> EXTRA_PARAMS_CODEC = ExtraParams.CODEC.optionalFieldOf("startraveller:extra_params").codec();

    @Override
    public <T> DataResult<Pair<FireworkExplosionComponent, T>> decode(DynamicOps<T> ops, T input) {
        // decode the extra params and then decode the original, setting the thread-local context
        return EXTRA_PARAMS_CODEC.decode(ops, input).flatMap(params -> FireworkExplosionComponentDuck.doWithContext(
                params.getFirst().orElse(null) /* convert Optional to Nullable */,
                () -> original.decode(ops, params.getSecond() /* use the partial from the extra params, idk if this matters, but it's here if it does */)
        ));
    }

    @Override
    public <T> DataResult<T> encode(FireworkExplosionComponent input, DynamicOps<T> ops, T prefix) {
        // encode the original, then encode the extra params, merging them by assuming that both are a map like structure
        return original.encode(input, ops, prefix).flatMap(out -> EXTRA_PARAMS_CODEC.encode(
                Optional.ofNullable(FireworkExplosionComponentDuck.starTravellerTesting$params(input)) /* convert Nullable to Optional */,
                ops,
                out
        ));
    }

}
