package startravellertesting.mixin;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.RecordComponentNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import startravellertesting.annotation.RecordComponent;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public class ModMixinConfigPlugin implements IMixinConfigPlugin {
    private static final List<String> MIXINS_TO_PROCESS = List.of(
            "startravellertesting.mixin.FireworkExplosionComponentMixin"
    );
    private static final String ANNOTATION_DESC = Type.getDescriptor(RecordComponent.class);
    private static final String RECORD_NAME = Type.getInternalName(Record.class);

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (!MIXINS_TO_PROCESS.contains(mixinClassName))
            return;
        if (!RECORD_NAME.equals(targetClass.superName))
            throw new RuntimeException("RecordComponent can only be applied in records");
        for (var field : targetClass.fields) {
            if (field.visibleAnnotations == null || field.visibleAnnotations.stream().noneMatch(a -> ANNOTATION_DESC.equals(a.desc))) {
                continue;
            }
            if (Modifier.isStatic(field.access)) {
                throw new RuntimeException("RecordComponent annotation is not allowed on static fields");
            }
            field.visibleAnnotations.removeIf(a -> ANNOTATION_DESC.equals(a.desc));
            targetClass.recordComponents.add(new RecordComponentNode(
                    field.name,
                    field.desc,
                    field.signature
            ));
            // getter done through mixin
        }
    }
}
