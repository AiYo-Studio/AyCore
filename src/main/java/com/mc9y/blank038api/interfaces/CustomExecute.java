package com.mc9y.blank038api.interfaces;

import java.io.File;

/**
 * @author Blank038
 */
@FunctionalInterface
public interface CustomExecute<T> {

    void run(T file);
}
