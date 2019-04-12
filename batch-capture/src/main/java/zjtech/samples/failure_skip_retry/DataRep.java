/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

@JobScope
@Component
public class DataRep {

  private AtomicInteger value = new AtomicInteger();

  public int getAndIncrement() {
    return value.getAndIncrement();
  }
}
