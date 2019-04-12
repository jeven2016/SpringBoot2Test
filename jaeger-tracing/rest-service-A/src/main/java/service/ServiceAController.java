package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/service-A")
@Slf4j
public class ServiceAController {

  @Autowired
  RestTemplate restTemplate;

  @GetMapping
  public String say() {
    return "service-A says something...";
  }

  @GetMapping("/error")
  public String error() {
    return restTemplate.getForObject("http://localhost:9002/service-B/error", String.class);
  }

  @GetMapping("/slow")
  public String slow() {
    return restTemplate.getForObject("http://localhost:9002/service-B/slow", String.class);
  }

  @GetMapping("/msg")
  public void showMsg() {
    restTemplate.getForObject("http://localhost:9002/service-B/message", String.class);
  }

/*  @Scheduled(fixedRate = 4000)
  public String schedule() {
    log.info("run schedule method");
    return "schedule";
  }*/
}
