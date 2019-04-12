/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples;

import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Component
public class JobListener {

  @BeforeJob
  public void beforeJob() {
    System.out.println("before job listener triggered....");
  }

  @AfterJob
  public void afterJob() {
    System.out.println("after job listener triggered....");
  }


}
