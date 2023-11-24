package me.geuxy.emu.api.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInfo {

    String name();
    String description();
    String type();

}
