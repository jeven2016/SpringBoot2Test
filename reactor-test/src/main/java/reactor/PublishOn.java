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
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * 6.1 Schedulers.immediate() - 使用当前线程 6.2 Schedulers.elastic() - 使用线程池 6.3
 * Schedulers.newElastic("test1") - 使用(新)线程池(可以指定名称，更方便调试) 6.4 Schedulers.single() - 单个线程 6.5
 * Schedulers.newSingle("test2") - (新)单个线程(可以指定名称，更方便调试) 6.6 Schedulers.parallel() -
 * 使用并行处理的线程池（取决于CPU核数) 6.7 Schedulers.newParallel("test3")  - 使用并行处理的线程池（取决于CPU核数，可以指定名称，方便调试) 6.8
 * Schedulers.fromExecutorService(Executors.newScheduledThreadPool(5)) - 使用Executor（这个最灵活)
 */
@Slf4j
public class PublishOn {

  @Test
  public void newElastic() throws InterruptedException {
//    Scheduler scheduler = Schedulers.newElastic("elastic", 30000);
    Scheduler scheduler = Schedulers.newElastic("elastic");
    this.runSub(scheduler);
  }



  /**
   * a fixed pool of workers that is tuned for parallel work (Schedulers.parallel()).
   * It creates as many workers as you have CPU cores.
   */
  @Test
  public void runPara() {
    Scheduler scheduler = Schedulers.parallel();
    this.runScheduler(scheduler);
  }

  @Test
  public void with_single_thread() {
    //Creates a new Scheduler backed by 4 Thread
    Scheduler scheduler = Schedulers.newParallel("para", 4);

    //generate 5 items
    Flux<String> flux = Flux.create(sink -> {
      IntStream.range(0, 5).forEach(val -> {
        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
          log.warn("exception", e);
        }
        sink.next("sink" + new Random().nextInt(1000));
      });
    })
        //The publishOn switches the whole sequence on a Thread picked from scheduler
        .publishOn(scheduler)

        // map runs on said Thread from scheduler
        .map(value -> Thread.currentThread().getName() + "-" + value);

    //subscribe and consume messages
    flux.subscribe(val -> {
      System.out.println(Thread.currentThread().getName() + ":" + val);
    });
  }

  /**
   * using publishOn
   * 在publishOn之后的filter或map等将使用publishOn配置的线程；之前的话，使用的是main线程或subscribeOn配置的线程
   * @param scheduler
   */
  private void runScheduler(Scheduler scheduler) {
    CountDownLatch latch = new CountDownLatch(1);

    //The interval operator ticks every x units of time with an increasing Long value
    Flux<String> flux = Flux.interval(Duration.ofSeconds(3))
        .map(val -> Thread.currentThread().getName() + "-" + val)
        .publishOn(scheduler);

    flux.subscribe(val -> {
//      latch.countDown();
      System.out.println(Thread.currentThread().getName() + ":" + val);
    });

    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Subscribeon
   * 在没有配置publishOn，只配置subscribeOn的话，则所有execution in created thread instead main thread
   * output:
   * parallel-1:parallel-2-0
   * parallel-1:parallel-2-1
   * parallel-1:parallel-2-2
   * @param scheduler
   */
  private void runSub(Scheduler scheduler) {
    CountDownLatch latch = new CountDownLatch(1);

    //The interval operator ticks every x units of time with an increasing Long value
    Flux<String> flux = Flux.interval(Duration.ofSeconds(3))
        .map(val -> Thread.currentThread().getName() + "-" + val)
        .subscribeOn(scheduler);

    flux.subscribe(val -> {
//      latch.countDown();
      System.out.println(Thread.currentThread().getName() + ":" + val);
    });

    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
