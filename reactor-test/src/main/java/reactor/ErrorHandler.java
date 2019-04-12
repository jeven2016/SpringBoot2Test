/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

public class ErrorHandler {

  @Test
  public void do_finally() {
    Flux.create(sink -> {
      throw new RuntimeException("interrupt");
    })
        .doOnError(thr -> {
          System.out.println("doOnError");
        })
        .doOnTerminate(() -> {
          System.out.println("doOnTerminate");
        })
        .doFinally((type) -> {
          System.out.println("doFinally");
        }).subscribe();
  }

  @Test
  public void test_retry() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    Flux.generate(() -> Tuples.of(-2L, 2L), (state, sink) -> {
      System.out.println("enter........");
      if (state.getT1() < 0) {
        throw new RuntimeException();
      }
      sink.next(state.getT1());
      return Tuples.of(-1L, 4L);
    })
        .retry(1)
        .doOnError(thr -> System.out.println("error"))
        .subscribe(System.out::println);
    countDownLatch.await();
  }

  /**
   * Before you learn about error-handling operators, you must keep in mind that any error in a
   * reactive sequence is a terminal event. Even if an error-handling operator is used, it does not
   * allow the original sequence to continue. Rather, it converts the onError signal into the start
   * of a new sequence (the fallback one). In other words, it replaces the terminated sequence
   * upstream of it.
   */
  @Test
  public void test_eror() {
    /*
      For replacing :
      try {
        return doSomethingDangerous(10);
      }
      catch (Throwable error) {
        return "RECOVERED";
      }
     */
    Flux<Integer> flux = Flux.range(0, 5).map(val -> {
      if (val > 3) {
        throw new RuntimeException();
      }
      return val;
    }).onErrorReturn(11); //or try onErrorResume() to handle returned value

    flux.subscribe(System.out::println);

  }

  /**
   * An exception occurs while value equals to number 3, but the code will continue to execute
   */
  @Test
  public void test_error_continue() {
    Flux<Integer> flux = Flux.range(0, 9).map(val -> {
      if (val == 3) {
        throw new RuntimeException();
      }
      return val;
    }).onErrorContinue((excep, val) -> {
      //exception
      System.out.println(excep.getMessage());

      //exception value
      System.out.println("exceptionValue=" + val);
    });

    flux.subscribe(System.out::println);
  }

  @Test
  public void handle_2() {
    /*
     * For replacing:
     *
     * try {
     *   v1 = throwException("key1");
     * }
     * catch (Throwable error) {
     *   v1 = getFromCache("key1");
     * }
     *System.out.println(v1);
     *
     * String v2;
     * try {
     *   v2 = throwException("key2");
     * }
     * catch (Throwable error) {
     *   v2 = getFromCache("key2");
     * }
     * System.out.println(v2);
     */

    Flux.just("key1", "key2").flatMap(val -> this.throwException(val).onErrorResume((thr) -> {
      return Mono.just("output--");
    })).doOnNext(System.out::println).subscribe();
  }

  @Test
  public void catch_and_rethrow() {
    /*
     * try {
     *   return throwException(k);
     * }
     * catch (Throwable error) {
     *   throw new RuntimeException("oops, SLA exceeded", error);
     * }
     */
    Flux.just("ke1").flatMap(this::throwException).onErrorMap(exception -> {
      return new RuntimeException("new exception", exception);
    })
        //print the info of the new exception
        .doOnError(thr -> System.out.println(thr.getMessage()))
        .subscribe();

  }


  Mono<String> throwException(String key) {
    if (key != null) {
      return Mono.error(new RuntimeException());
    } else {
      return Mono.just("");
    }
  }
}
