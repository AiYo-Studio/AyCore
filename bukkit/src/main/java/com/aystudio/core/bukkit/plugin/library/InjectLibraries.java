package com.aystudio.core.bukkit.plugin.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blank038
 * @since 2021-11-02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface InjectLibraries {
    String[] value() default {};
}
