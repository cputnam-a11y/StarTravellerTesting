package startravellertesting.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
// put whatever data you need in this class.
public record ExtraParams(boolean something) {
    //the ComposedFireworkComponentCodec takes care of making sure the output is map like, so this codec can output whatever
    public static final Codec<ExtraParams> CODEC =
            // doesn't matter
            Codec.BOOL.xmap(ExtraParams::new, ExtraParams::something);

    public static final PacketCodec<RegistryByteBuf, ExtraParams> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN,
            ExtraParams::something,
            ExtraParams::new
    );
}
