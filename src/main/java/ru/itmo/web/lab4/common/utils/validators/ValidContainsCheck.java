package ru.itmo.web.lab4.common.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.DoubleStream;

public class ValidContainsCheck implements ConstraintValidator<Contains, Double> {
  private double[] array;

  @Override
  public void initialize(Contains contains) {
    this.array = contains.array();
  }

  @Override
  public boolean isValid(Double value, ConstraintValidatorContext context) {
    return DoubleStream.of(array).anyMatch(x -> x == value);
  }
}
