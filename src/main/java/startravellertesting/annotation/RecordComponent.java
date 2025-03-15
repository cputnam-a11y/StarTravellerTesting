package startravellertesting.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A companion annotation for mixin fields in specific mixins used to indicate that this field will be transformed into a record component.
 */
@Retention(RetentionPolicy.RUNTIME) // Retain this annotation at runtime, doesn't really matter, as we remove it
@Target(ElementType.FIELD) // only valid on fields, nothing else can become record components
public @interface RecordComponent {
}
