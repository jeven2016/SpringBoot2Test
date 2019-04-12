/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import reactor.core.publisher.Flux;

public class BufferOperator {

  @Test
  public void testBuffer() {
    getFlux().take(100)
        .buffer(5)
        .subscribe(System.out::println);
  }


  @Test
  public void test_sample() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    getFlux().delayElements(Duration.ofMillis(100))
        //.take(3)
        .sample(Duration.ofSeconds(1))
        .doOnError(thr -> thr.printStackTrace())
        .subscribe(System.out::print);

    latch.await();
  }

  private Flux<Object> getFlux() {
    return Flux.generate(() -> 0,
        (state, sink) -> {
          state = state + 10;
          sink.next(state);
          return state;
        });
  }
}
