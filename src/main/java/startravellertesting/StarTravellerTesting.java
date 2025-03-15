package startravellertesting;

import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.ModInitializer;
import net.minecraft.component.type.FireworkExplosionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import startravellertesting.duck.FireworkExplosionComponentDuck;
import startravellertesting.util.ExtraParams;

public class StarTravellerTesting implements ModInitializer {
    public static final String MOD_ID = "startravellertesting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        ExtraParams params = new ExtraParams(false);
        var component = FireworkExplosionComponentDuck.doWithContext(
                params,
                () -> new FireworkExplosionComponent(FireworkExplosionComponent.Type.BURST, IntList.of(1), IntList.of(3), true, true)
        );

    }
}