/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.Test;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.MonoProcessor;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.TopicProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.publisher.WorkQueueProcessor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class UnicatProcessorTest {

  /**
   * The demand from every subscriber is added to a queue, and events from a publisher are sent to
   * any of the subscribers.
   */
  @Test
  public void work_processor() {
    WorkQueueProcessor<Long> data = WorkQueueProcessor.<Long>builder().build();
    data.subscribe(t -> System.out.println("1. " + t));
    data.subscribe(t -> System.out.println("2. " + t));
    FluxSink<Long> sink = data.sink();
    sink.next(10L);
    sink.next(11L);
    sink.next(12L);
    sink.next(13L);
    sink.next(14L);
  }

  @Test
  public void mono_processor() {
    MonoProcessor<Long> data = MonoProcessor.create();
    data.subscribe(t -> System.out.println("1. " + t));
    data.subscribe(t -> System.out.println("2. " + t));

    data.onNext(10L);
    data.onNext(11L);
    data.onNext(12L);
    data.onNext(13L);
    data.onNext(14L);
  }

  /**
   * TopicProcessor is a processor capable of working with multiple subscribers, using an event loop
   * architecture. The processor delivers events from a publisher to the attached subscribers in an
   * asynchronous manner, and honors backpressure for each subscriber by using the RingBuffer data
   * structure. The processor is also capable of listening to events from multiple publishers.
   */
  @Test
  public void topic_processor() {
    TopicProcessor<Long> data = TopicProcessor.<Long>builder()
        .executor(Executors.newFixedThreadPool(5)).build();
    data.subscribe(t -> System.out.println(Thread.currentThread().getName() + "-sub1-" + t));
    data.subscribe(t -> System.out.println(Thread.currentThread().getName() + "-sub2-" + t));
    data.subscribe(t -> System.out.println(Thread.currentThread().getName() + "-sub3-" + t));
    data.subscribe(t -> System.out.println(Thread.currentThread().getName() + "-sub4-" + t));
    FluxSink<Long> sink = data.sink();
    sink.next(10L);
    sink.next(11L);
    sink.next(12L);
  }

  /**
   * ReplayProcessor is a special-purpose processor, capable of caching and replaying events to its
   * subscribers. The processor also has the capability of publishing events from an external
   * publisher. It consumes an event from the injected publisher and synchronously passes it to the
   * subscribers.
   */
  @Test
  public void test_relay_processor() {
    ReplayProcessor playProcessor = ReplayProcessor.create();
    playProcessor.subscribe(val -> System.out.println("sub1-" + val));
    playProcessor.onNext(1);
    playProcessor.onNext(2);
    playProcessor.onNext(3);
    playProcessor.onNext(4);

    playProcessor.subscribe(val -> System.out.println("sub2-" + val));
  }

  /**
   * EmitterProcessor is a processor that can be used with several subscribers. Multiple subscribers
   * can ask for the next value event, based on their individual rate of consumption. The processor
   * provides the necessary backpressure support for each subscriber. This is depicted in the
   * following diagram:
   */
  @Test
  public void sub_emit() {
    EmitterProcessor<Long> data = EmitterProcessor.create(1);
    data.subscribe(t -> System.out.println("sub1-" + t));
    FluxSink<Long> sink = data.sink();
    sink.next(10L);
    sink.next(11L);
    sink.next(12L);
    data.subscribe(t -> System.out.println("sub2-" + t));
    sink.next(13L);
    sink.next(14L);
    sink.next(15L);
  }


  @Test
  public void subscribe_twice() {
    UnicastProcessor unicatProcessor = UnicastProcessor.create();
    Scheduler scheduler = Schedulers.newParallel("par", 5);

    Flux<String> timerFlux = Flux.interval(Duration.ofSeconds(3))
        .map(val -> val + "-hello")
        .doOnNext(val -> {
          System.out.println(Thread.currentThread().getName() + "-" + val);
          unicatProcessor.sink().next(val);
        });

    unicatProcessor.subscribe(value -> {
      System.out.println("consume :" + value);
    });


  }


  /**
   * DirectProcessor is the simplest of the processors. This processor connects a processor to a
   * subscriber, and then directly invokes the Subscriber.onNext method. The processor does not
   * offer any backpressure handling.
   */
  @Test
  public void test_direct_processor() {
    DirectProcessor directProcessor = DirectProcessor.create();
    directProcessor.subscribe(System.out::println);

    directProcessor.onNext("yes1");
    directProcessor.onNext("yes2");
    directProcessor.onNext("yes3");

  }

  /**
   * there are two broad families of publishers: hot and cold.
   *
   * The description above applies to the cold family of publishers. They generate data anew for
   * each subscription. If no subscription is created, then data never gets generated.
   *
   * Think of an HTTP request: Each new subscriber will trigger an HTTP call, but no call is made if
   * no one is interested in the result.
   *
   * Hot publishers, on the other hand, do not depend on any number of subscribers. They might start
   * publishing data right away and would continue doing so whenever a new Subscriber comes in (in
   * which case said subscriber would only see new elements emitted after it subscribed). For hot
   * publishers, something does indeed happen before you subscribe.
   *
   * hot publisher behavior= These publishers keep emitting data, and, when a new subscriber
   * arrives, it receives only newly emitted data.<br/> cold publisher behavior = which also
   * publishes old data for every new subscriber. These publishers are known as hot publishers.
   */
  @Test
  public void hot() throws InterruptedException {
    final UnicastProcessor<Long> hotSource = UnicastProcessor.create();
    //We built a UnicastProcessor and converted it to a Flux by using the publish method
    final Flux<Long> hotFlux = hotSource.publish().autoConnect();
    hotFlux.take(5).subscribe(t -> System.out.println("1. " + t));
    CountDownLatch latch = new CountDownLatch(2);
    new Thread(() -> {
      int c1 = 0, c2 = 1;
      while (c1 < 200) {
        hotSource.onNext(Long.valueOf(c1));
        int sum = c1 + c2;
        c1 = c2;
        c2 = sum;
        if (c1 == 144) {
          hotFlux.subscribe(t -> System.out.println("2. " + t));

          //get 5 items from now
          hotFlux.take(5).subscribe(t -> System.out.println("3. " + t));
        }
      }
      hotSource.onComplete();
      latch.countDown();
    }).start();
    latch.await();
  }

}
