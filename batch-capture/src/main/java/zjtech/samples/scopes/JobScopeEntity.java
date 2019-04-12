/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.scopes;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

/**
 * Count won't be reset within steps
 */
@Component
@JobScope
public class JobScopeEntity {

  private AtomicInteger count = new AtomicInteger();

  public void increase() {
    count.getAndIncrement();
  }

  public int getCount() {
    return count.get();
  }

}
