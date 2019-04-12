/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class RetryListener extends RetryListenerSupport {

  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
      Throwable throwable) {
    try {
      System.out.println("---- will retry for " + throwable.getClass());
      callback.doWithRetry(context);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    super.onError(context, callback, throwable);
  }
}
