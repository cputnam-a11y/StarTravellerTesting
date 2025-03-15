package startravellertesting.duck;

import net.minecraft.component.type.FireworkExplosionComponent;
import org.jetbrains.annotations.Nullable;
import startravellertesting.util.ExtraParams;

import java.util.function.Supplier;

public interface FireworkExplosionComponentDuck {
    ThreadLocal<ExtraParams> FAKE_CONSTRUCTOR_PARAM = ThreadLocal.withInitial(() -> null);

    @Nullable
    default ExtraParams starTravellerTesting$params() {
        throw new AssertionError("Must be Implemented through Mixin");
    }

    @Nullable
    @SuppressWarnings("DataFlowIssue")
    static ExtraParams starTravellerTesting$params(FireworkExplosionComponent component) {
        return ((FireworkExplosionComponentDuck) (Object) component).starTravellerTesting$params();
    }

    static <T> T doWithContext(ExtraParams params, Supplier<T> action) {
        var prior = FAKE_CONSTRUCTOR_PARAM.get();
        FAKE_CONSTRUCTOR_PARAM.set(params);
        try {
            return action.get();
        } finally {
            if (prior == null)
                FAKE_CONSTRUCTOR_PARAM.remove();
            else
                FAKE_CONSTRUCTOR_PARAM.set(prior);
        }
    }
}
