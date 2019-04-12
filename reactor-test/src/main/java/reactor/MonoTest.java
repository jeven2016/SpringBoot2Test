/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

@Slf4j
public class MonoTest {

  @Test
  public void fromFu() {
    Mono.fromCallable(() -> Thread.currentThread().getName() + "-hello")
        .subscribe(val -> System.out.println(Thread.currentThread().getName() + ": " + val));
  }


  @Test
  public void fromRunable() {
    Mono<String> mono = Mono.fromRunnable(() -> {
      System.out.println("hello world");
    });

    Mono<Void> voidMono = mono.then();
    voidMono.subscribe();
  }

  @Test
  public void monoProcessor() {
    MonoProcessor<Integer> future = MonoProcessor.create();
    Consumer<Integer> checkEmp = (rowId) -> {

      System.out.println("Employee with id: " + rowId);

    };

    Mono<Integer> engine = future
        .doOnNext(checkEmp)
        .doOnSuccess(emp -> {
          System.out.println("Employee's age is ");
        })
        .doOnTerminate(() -> System.out.println("Transaction terminated with error: "))
        .doOnError(ex -> System.out.println("Error: " + ex.getMessage()));

    engine.subscribe(System.out::println);

    future.onNext(100);
    int valStream = future.block();
    System.out.println("Employee's ID again is: " + valStream);
  }
}
