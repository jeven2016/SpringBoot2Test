package zjtech.basic.common;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

@Component
public class CustomSubscriber extends BaseSubscriber<Integer> {

  private AtomicInteger subCount = new AtomicInteger();
  private AtomicInteger nextCount = new AtomicInteger();

  public CustomSubscriber() {

  }

  @Override
  protected void hookOnSubscribe(Subscription subscription) {
    System.out.println(getThreadName() + "++ hookOnSubscribe, count=" + subCount.incrementAndGet());
    //can consume 1 event at every turn
    request(5);
  }

  @Override
  protected void hookOnNext(Integer value) {
    System.out
        .println(getThreadName() + "++ hookOnNext, count=" + nextCount.incrementAndGet());
    request(5);
  }

  private String getThreadName() {
    return Thread.currentThread().getName();
  }
}
