package com.jesus_crie.open_command_processor.annotation;

public @interface CommandInfo {

    String[] aliases();
    String description() default "";
    String shortDescription() default "";
}
