/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class FluxTest {

  /**
   * then, thenEmpty and thenMany All the thenXXX methods on Mono have one semantic in common: they
   * ignore the source onNext signals and react on completion signals (onComplete and onError),
   * continuing the sequence at this point with various options. As a consequence, this can change
   * the generic type of the returned Mono:
   *
   * then will just replay the source terminal signal, resulting in a Mono<Void> to indicate that
   * this never signals any onNext.
   *
   * thenEmpty not only returns a Mono<Void>, but it takes a Mono<Void> as a parameter. It
   * represents a concatenation of the source completion signal then the second, empty Mono
   * completion signal. In other words, it completes when A then B have both completed sequentially,
   * and doesn't emit data.
   *
   * thenMany waits for the source to complete then plays all the signals from its Publisher<R>
   * parameter, resulting in a Flux<R> that will "pause" until the source completes, then emit the
   * many elements from the provided publisher before replaying its completion signal as well.
   */
  @Test
  public void testInterval() throws InterruptedException {
    Flux<String> flux = Flux.interval(Duration.ofSeconds(3)).map(val -> "val is " + val)
        .doOnNext(val -> {
          System.out.printf("%s-%s%n", Thread.currentThread().getName(), val);
        });

    Scheduler scheduler = Schedulers.elastic();
    Flux<String> flux2 = Flux.interval(Duration.ofSeconds(3)).map(val -> "val2 is " + val)
        .takeUntil(text -> text.contains("is 3")) //stop while value is 3
        .subscribeOn(scheduler)
        .doOnNext(val -> {
          System.out.printf("%s-%s%n", Thread.currentThread().getName(), val);
        });

    //first handle flux2 and while flux2 is completed continue flux
    flux2.thenMany(flux).subscribe();

    CountDownLatch countDownLatch = new CountDownLatch(1);
    countDownLatch.await();


  }

  @Test
  public void test_repeat() {
    Random random = new Random();
    getFlux()
        .take(2)
        .repeat(1)
        .doOnNext(System.out::print)
        .blockLast();
  }


  private Flux<Object> getFlux() {
    return Flux.generate(() -> 0,
        (state, sink) -> {
          state = state + 100;
          sink.next(state);
          return state;
        });
  }

}
