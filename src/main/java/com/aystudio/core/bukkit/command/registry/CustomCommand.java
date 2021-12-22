package com.aystudio.core.bukkit.command.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blank038
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface CustomCommand {

    String sub() default "";

    boolean defaultHasPermission() default false;

    String permission() default "";

    String notPermissionText() default "";
}