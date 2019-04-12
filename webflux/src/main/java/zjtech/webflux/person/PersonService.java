package zjtech.webflux.person;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PersonService {

  @Autowired
  private PersonRep rep;

  public Flux<PersonEntity> findAll() {
    List<PersonEntity> list = rep.findAll();
    return Flux.fromIterable(list);
  }


  //5 seconds is required before returning a person object
  public Flux<PersonEntity> getPersonInPeriod() {
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      System.out.println("it's time out now");
    }
    PersonEntity personEntity = new PersonEntity();
    personEntity.setName("timeoutName");
    return Flux.just(personEntity);
  }

}
