package com.fieldflow.api.shared.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ApiJsonExamples.class)
public @interface ApiJsonExample {
	String status() default "200";

	String description() default "Operación exitosa";

	String path();

	String mediaType() default "application/json";

	String summary() default "";
}
