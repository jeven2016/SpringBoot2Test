package zjtech.basic.person;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import zjtech.webflux.person.PersonEntity;

public class PersonControllerItest {

  private WebClient webClient;

  @Before
  public void setup() {
    webClient = WebClient.create("http://localhost:8080/");
  }

  @Test
  public void getPerson() {
    Flux<PersonEntity> flux = webClient.get().uri("/person").accept(MediaType.APPLICATION_JSON)
        .retrieve().bodyToFlux(PersonEntity.class);
//    flux.sub
  }

}
