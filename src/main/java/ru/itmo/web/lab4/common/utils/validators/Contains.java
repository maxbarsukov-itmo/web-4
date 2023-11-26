package ru.itmo.web.lab4.common.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.FIELD })
@Documented
@Constraint(validatedBy = { ValidContainsCheck.class })
public @interface Contains {
  String message() default "Value is not valid";
  Class<?>[] groups() default { };
  Class<? extends Payload>[] payload() default { };

  double[] array();
}
