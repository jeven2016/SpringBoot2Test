/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Skip the item and configure the org.springframework.batch.core.SkipListener<T,S> listener.
 *
 * SB also provides convenience abstract classes with the Support suffix for these interfaces
 * (org.springframework.batch.core.SkipListenerSupport<T,S> and org.springframework.retry.RetryListenerSupport),
 * where all methods have empty implementations. If we use abstract classes instead of interfaces,
 * we don’t need to implement all the interception methods. These mechanisms apply only to
 * chunk-oriented processing.
 */
@Component
public class SkipListener extends SkipListenerSupport<String, String> {

  @Override
  public void onSkipInProcess(String item, Throwable t) {
    if (t instanceof IllegalAccessException) {
      System.out.println("IllegalAccessException is ignored, item=" + item);
    } else {
      throw new RuntimeException("internal error");
    }
  }
}
