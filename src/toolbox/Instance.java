package toolbox;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Target;

@Target(FIELD)
public @interface Instance {

	Class<?> value();

}
