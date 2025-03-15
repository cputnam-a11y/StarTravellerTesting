package startravellertesting.util;

import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import startravellertesting.duck.FireworkExplosionComponentDuck;

import java.util.Optional;

public record ComposedFireworkComponentPacketCodec(PacketCodec<RegistryByteBuf, FireworkExplosionComponent> original) implements PacketCodec<RegistryByteBuf, FireworkExplosionComponent> {
    public static final PacketCodec<RegistryByteBuf, Optional<ExtraParams>> PACKET_CODEC = PacketCodecs.optional(
            ExtraParams.PACKET_CODEC
    );

    @Override
    public FireworkExplosionComponent decode(RegistryByteBuf buf) {
        // decode the extra params and then decode the original, setting the thread-local context
        return FireworkExplosionComponentDuck.doWithContext(
                PACKET_CODEC.decode(buf).orElse(null),
                () -> original.decode(buf)
        );
    }

    @Override
    public void encode(RegistryByteBuf buf, FireworkExplosionComponent value) {
        // encode the original, then encode the extra params, no merging needed for the packet codec
        PACKET_CODEC.encode(buf, Optional.ofNullable(FireworkExplosionComponentDuck.starTravellerTesting$params(value)));
        original.encode(buf, value);
    }
}
