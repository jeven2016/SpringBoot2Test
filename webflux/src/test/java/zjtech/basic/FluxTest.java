package zjtech.basic;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import zjtech.basic.common.CustomSubscriber;
import zjtech.webflux.WebFluxApp;
import zjtech.webflux.person.PersonEntity;
import zjtech.webflux.person.PersonService;

@SpringBootTest(classes = WebFluxApp.class, webEnvironment = WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CustomSubscriber.class})
public class FluxTest {

  @Autowired
  PersonService service;

  @Autowired
  CustomSubscriber customSubscriber;

  @Test
  public void testFluxJust() {
    Flux.just("hello1", "hello2").subscribe(System.out::print);
    Integer a[] = new Integer[]{1, 2, 3};
    Flux.fromArray(a);

    Flux.fromStream(Stream.of(1, 23, 343, 33));
    Flux.range(1, 3);
//    Flux.just("123").fl
  }

  @Test
  public void customSubscriber() {
    Flux.range(1, 100)
        .publishOn(Schedulers.newElastic("my-thread", 20))
        .publishOn(Schedulers.parallel())
        .subscribe(customSubscriber);

    //similar implementation in java8
//    IntStream.range(1, 10).parallel().forEach(System.out::println);
//    IntStream.range(1, 10).parallel().forEach(val -> {
//      System.out.println(Thread.currentThread().getName() + "+++" + val);
//    });
  }


  //time out exception occurred
  @Test
  public void timeout() {
    Objects.requireNonNull(service);
    service.getPersonInPeriod()
        .timeout(Duration.ofSeconds(3))
        .onErrorResume(throwable -> {
          System.out.println("exception: " + throwable.getCause());
          return Flux.just(new PersonEntity());
        })
//        .take(1)
        .publishOn(Schedulers.newParallel("thread-pool", 3))
        .subscribe(System.out::println);

  }
}
