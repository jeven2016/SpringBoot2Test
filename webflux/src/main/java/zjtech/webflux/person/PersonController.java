package zjtech.webflux.person;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/person")
public class PersonController {

  @Autowired
  private PersonService personService;

  @GetMapping
  public Flux<PersonEntity> findAllPerson() {
    Flux<PersonEntity> personEntityFlux = personService.findAll();
    return personEntityFlux;
  }

  @GetMapping("echo")
  public String say(){
    return "hello";
  }

  @PostMapping
  public void post(@Valid @RequestBody PersonEntity personEntity) {
    System.out.println(personEntity);
  }

  @GetMapping("/202")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Flux<String> flux200() {
    return Flux.just("202 accepted");
  }
/*

  @GetMapping("/wzj")
  public Mono<ServerResponse> showPersonInfo(ServerRequest request) {
    PersonEntity personEntity = new PersonEntity();
    personEntity.setName("wzj");

     Mono.justOrEmpty(personEntity);
  }
*/

}
