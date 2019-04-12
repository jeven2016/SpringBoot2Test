package zjtech.basic.reactor;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;

public class FluxCreation {

  /**
   * This is for synchronous and one-by-one emissions, meaning that the sink is a SynchronousSink
   * and that its next() method can only be called at most once per callback invocation. You can
   * then additionally call error(Throwable) or complete(), but this is optional.
   */
  @Test
  public void generate() {

    //generate a initial state and generate many more then, finally print all
    Flux.generate(() -> 0, (state, sink) -> {
      sink.next("3 x " + state + " = " + 3 * state);
      if (state == 10) {
        sink.complete();
      }
      return state + 1;
    }, System.out::println);
  }

  /**
   * create can work asynchronously or synchronously and is suitable for multiple emissions per
   * round.
   *
   * It exposes a FluxSink, with its next, error, and complete methods. Contrary to generate, it
   * doesn’t have a state-based variant. On the other hand, it can trigger multiple events in the
   * callback (even from any thread at a later point in time).
   *
   * create can be very useful to bridge an existing API with the reactive world - such as an
   * asynchronous API based on listeners.
   */
  @Test
  public void create() {
    final Random random = new Random();
    AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * IGNORE to Completely ignore downstream backpressure requests. This may yield IllegalStateException when queues get full downstream.
     *
     * ERROR to signal an IllegalStateException when the downstream can’t keep up.
     *
     * DROP to drop the incoming signal if the downstream is not ready to receive it.
     *
     * LATEST to let downstream only get the latest signals from upstream.
     *
     * BUFFER (the default) to buffer all signals if the downstream can’t keep up. (this does unbounded buffering and may lead to OutOfMemoryError).
     */
    Flux<String> flux = Flux.create(sink -> {
      new Listener() {

        @Override
        public void process() {
          System.out.println("process this value");
          if (atomicInteger.incrementAndGet() > 5) {
            sink.complete();
            return;
          }
          sink.next("val" + random.nextInt());
        }

        @Override
        public void completed() {
          System.out.println("flux.create: completed");
          sink.complete();
        }

      };
    }, OverflowStrategy.LATEST);

    flux.subscribe(val -> System.out.println(val));
  }

  private static interface Listener {

    void process();

    void completed();
  }

  /**
   * Clean up
   */
  @Test
  public void cleanUp() {
    Flux<String> bridge = Flux.create(sink -> {
      sink.onRequest(n -> System.out.println("onrequest"))
          .onCancel(() -> System.out.println("oncancel"))
          .onDispose(() -> System.out.println("ondispose"));
    });

  }

  /**
   * Handler The handle method is a bit different. It is present in both Mono and Flux. Also, it is
   * an instance method, meaning that it is chained on an existing source (as are the common
   * operators).
   *
   * It is close to generate, in the sense that it uses a SynchronousSink and only allows one-by-one
   * emissions. However, handle can be used to generate an arbitrary value out of each source
   * element, possibly skipping some elements. In this way, it can serve as a combination of map and
   * filter. The signature of handle is as follows:
   */
  @Test
  public void handle() {
    Flux.just("234", "uud", "2222222222").handle((strValue, sink) -> {
      if (strValue.length() > 5) {
        return;
      }
      sink.next(strValue);
    }).subscribe(System.out::println);

  }

  /**
   * Return a default value while exception occured
   */
  @Test
  public void onErrorReturn() {
    Flux<?> fluxVal = Flux.just(3).map((val) -> {
      throw new RuntimeException("error return");
    }).onErrorReturn("exption occured");

    fluxVal.subscribe(val -> System.out.println(val.toString()));

  }

  /**
   * Like onErrorReturn, onErrorResume has variants that let you filter which exceptions to fallback
   * on, based either on the exception’s class or a Predicate. The fact that it takes a Function
   * also allows you to choose a different fallback sequence to switch to, depending on the error
   * encountered:
   */
  @Test
  public void onErrorResume() {
    Flux.just("timeout1", "unknown", "key2")
        .onErrorResume(error -> {
          return Flux.error(error);
        });
  }

  //todo: 4.7 handling errors
  @Test
  public void catchAndThrowExcep() {
    Flux.just("timeout1")
//        .flatMap(k -> callExternalService(k))
        .doOnError(e -> {
//          log("uh oh, falling back, service failed for key " + k);
        })
        .onErrorResume(original -> Flux.error(
            new RuntimeException("oops, SLA exceeded", original)
        ));
  }

}
