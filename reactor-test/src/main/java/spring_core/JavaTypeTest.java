/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package spring_core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.junit.Test;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.SynthesizingMethodParameter;

public class JavaTypeTest {

  @Test
  public void testType() throws NoSuchMethodException {
    Stream<Method> tMethodStream = Arrays.stream(Proxy.class.getDeclaredMethods())
        .filter(md -> md.getName().contains("tMethod"));

    tMethodStream.findFirst().ifPresent(method1 -> {
      SynthesizingMethodParameter parameter = new SynthesizingMethodParameter(method1, 0);
      Type type = parameter.getGenericParameterType();
      System.out.println(type.getTypeName());
      System.out.println(parameter.getDeclaringClass());
      System.out.println(parameter.getConstructor());
      System.out.println(parameter.getMethodAnnotations());
      System.out.println(parameter.getExecutable().getName());
      System.out.println(parameter.getMember());
    });
  }
}

class Proxy {
  @Order
  public <T> T tMethod(@Valid T person) {
    return person;
  }
}
