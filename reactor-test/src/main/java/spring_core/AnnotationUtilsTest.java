/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package spring_core;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;

import java.lang.reflect.Method;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

public class AnnotationUtilsTest {

  @Test
  public void annotatedMethod() throws NoSuchMethodException {
    Parent parent = new Child();
    Method method = parent.getClass().getMethod("annotatedMethod");

    assertNull(AnnotationUtils.getAnnotation(method, Order.class));
    assertNotNull(AnnotationUtils.findAnnotation(method, Order.class));
  }

}


interface PInterface {

  @Order(1)
  String annotatedMethod();
}

class Parent implements PInterface{

  public String annotatedMethod() {
    return "annotatedMethod";
  }
}

class Child extends Parent {

  @Override
  public String annotatedMethod() {
    return "ChildAnnotatedMethod";
  }

}
