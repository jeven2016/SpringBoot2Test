/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.listeners;

import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Component
public class MyJobListener {

  @BeforeJob //or extend JobExecutionListener
  public void beforeJob() {
    System.out.println("==========before job");
  }

  @AfterJob //or extend JobExecutionListener
  public void afterJob() {
    System.out.println("==========after job");
  }
}
